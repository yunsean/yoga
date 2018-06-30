package com.yoga.wechat.material

import com.yoga.core.data.PageList
import com.yoga.core.exception.BusinessException
import com.yoga.core.property.PropertiesService
import com.yoga.core.service.BaseService
import com.yoga.core.utils.DateUtil
import com.yoga.core.utils.StrUtil
import com.yoga.wechat.account.AccountRepository
import com.yoga.wechat.menu.MenuEntityRepository
import com.yoga.wechat.menu.MenuType
import com.yoga.wechat.sequence.SequenceNameEnum
import com.yoga.wechat.util.WxSyncAction
import com.yoga.wechat.util.WxSyncFactory
import com.yoga.wechat.weixin.WeixinServiceFactory
import me.chanjar.weixin.mp.bean.material.WxMpMaterial
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews
import org.apache.commons.collections.map.HashedMap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.regex.Pattern
import javax.persistence.EntityManagerFactory
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@Service(value = "wechatMediaService")
open class MaterialService @Autowired constructor(
        val entityManagerFactory: EntityManagerFactory,
        val accountRepository: AccountRepository,
        val syncFactory: WxSyncFactory,
        val weixinServiceFactory: WeixinServiceFactory,
        val materialRepository: MaterialRepository,
        val materialArticleRepository: MaterialArticleRepository,
        val materialExRepository: MaterialExRepository,
        val menuEntityRepository: MenuEntityRepository,
        val materialSyncService: MaterialSyncService,
        val propertiesService: PropertiesService) : BaseService() {

    open fun sync(tenantId: Long, accountId: Long, actor: String, actorId: Long, type: MaterialType) {
        val account = accountRepository.findOneByTenantIdAndId(tenantId, accountId)
        if (account == null) throw BusinessException("公众号不存在")
        val action = when (type) {
            MaterialType.image -> WxSyncAction.image
            MaterialType.video -> WxSyncAction.video
            MaterialType.voice -> WxSyncAction.voice
            MaterialType.file -> WxSyncAction.file
            MaterialType.thumb -> WxSyncAction.thumb
            MaterialType.news -> WxSyncAction.news
        }
        syncFactory.sync(tenantId, accountId, actor, actorId, action) { syncIndex, tenantId, accountId, action ->
            materialSyncService.syncMaterial(type, syncIndex, tenantId, accountId, action)
        }
    }

    //将废弃
    open fun getMaterial(tenantId: Long, entityId: Long): HashedMap {
        val saved = menuEntityRepository.findOne(entityId) ?: throw BusinessException("菜单项不存在")
        val map = HashedMap()
        when (saved.type) {
            MenuType.media_id -> {
                if (null != saved.mediaId) {
                    val exist = materialRepository.findByTenantIdAndMediaId(tenantId, saved.mediaId!!) ?: return map
                    map.put("media", exist)
                    if (exist.type == MaterialType.news) {
                        val news = materialArticleRepository.findByTenantIdAndAccountIdAndMaterialId(exist.tenantId, exist.accountId, exist.id)
                        map.put("media", news)
                        map.put("type", "news")
                        map.put("updateTime", DateUtil.formatDateShort(exist.updateTime))
                        return map
                    }
                    map.put("type", exist.type)
                    return map
                }
            }
        }
        return map
    }

    open fun getMaterial(tenantId: Long, mediaId: String): HashedMap {
        val map = HashedMap()
        val exist = materialRepository.findByTenantIdAndMediaId(tenantId, mediaId) ?: return map
        map.put("media", exist)
        if (exist.type == MaterialType.news) {
            val news = materialArticleRepository.findByTenantIdAndAccountIdAndMaterialId(exist.tenantId, exist.accountId, exist.id)
            map.put("media", news)
            map.put("updateTime", DateUtil.formatDateShort(exist.updateTime))
            return map
        }
        return map
    }

    open fun getArticleDetail(tenantId: Long, accountId: Long, id: Long): MaterialArticle {
        return materialArticleRepository.findByTenantIdAndAccountIdAndId(tenantId, accountId, id) ?: throw BusinessException("未找到此文章")
    }

    open fun list(tenantId: Long, accountId: Long, type: MaterialType?, name: String?, groupId: Long?, mediaId: String?, pageIndex: Int, pageSize: Int): PageList<MaterialAndEx> {
        val em = entityManagerFactory.createEntityManager()
        var sql = "select ex.group_id as group_id, ex.name as name, ex.remark as remark, mg.`name` as group_name, m.* from wx_material m left join wx_material_ex ex on m.media_id = ex.media_id and ex.tenant_id = ? left join wx_material_group mg on ex.group_id = mg.id " +
                "where m.tenant_id = ? and m.account_id = ?"
        if (type != null) sql += " and m.type = ?"
        if (!name.isNullOrBlank()) sql += " and ex.name like ?"
        if (!mediaId.isNullOrBlank()) sql += " and m.media_id = ?"
        if (groupId != null && groupId == 0L) sql += " and (ISNULL(ex.group_id) or ex.group_id = 0)"
        else if (groupId != null) sql += " and ex.group_id = ?"
        sql += " order by m.id desc";
        try {
            var index = 1
            var query = em.createNativeQuery(sql, "ReturnMaterialAndEx")
                    .setParameter(index++, tenantId)
                    .setParameter(index++, tenantId)
                    .setParameter(index++, accountId)
            if (type != null) query.setParameter(index++, type?.toString())
            if (!name.isNullOrBlank()) query.setParameter(index++, "%" + name + "%")
            if (!mediaId.isNullOrBlank()) query.setParameter(index++, mediaId?.toString())
            if (groupId != null && groupId != 0L) query.setParameter(index++, groupId)
            val totalCount = query.resultList.size
            val resultList: List<MaterialAndEx> = if (pageIndex * pageSize < totalCount) {
                query.setFirstResult(pageIndex * pageSize)
                query.setMaxResults(pageSize)
                query.resultList as List<MaterialAndEx>
            } else {
                listOf()
            }
            resultList.filter {
                it.type == MaterialType.news
            }.forEach {
                it.articles = materialArticleRepository.findByTenantIdAndAccountIdAndMaterialId(tenantId, accountId, it.id)
            }
            return PageList(resultList, pageIndex, pageSize, totalCount)
        } finally {
            em.close()
        }
    }

    open fun updateName(tenantId: Long, mediaId: String, name: String?) {
        val materialEx = materialExRepository.findOneByTenantIdAndMediaId(tenantId, mediaId) ?: throw BusinessException("素材不存在")
        materialEx.name = name
        materialExRepository.save(materialEx)
    }

    @Transactional(propagation = Propagation.NEVER)
    open fun uploadNews(tenantId: Long, accountId: Long, materialId: Long) {
        val weixinService = weixinServiceFactory.getService(accountId)
        val wxNews = WxMpMaterialNews()
        val articles = materialArticleRepository.findByTenantIdAndAccountIdAndMaterialId(tenantId, accountId, materialId) ?: throw BusinessException("素材不存在")

        articles.forEach {
            if (it.thumbMediaId.isNullOrBlank()) it.thumbMediaId = uploadMedia(it.thumbUrl, accountId)
            val content = replaceImgUrl(accountId, it.content!!)
            val wxArticle = WxMpMaterialNews.WxMpMaterialNewsArticle()
            wxArticle.thumbMediaId = it.thumbMediaId
            wxArticle.thumbUrl = it.thumbUrl
            wxArticle.author = it.author
            wxArticle.title = it.title
            wxArticle.contentSourceUrl = it.sourceUrl
            wxArticle.content = content
            wxArticle.digest = it.digest
            wxArticle.isShowCoverPic = it.showConverImage
            wxArticle.url = it.clickUrl
            wxNews.addArticle(wxArticle)
        }
        materialArticleRepository.save(articles)

        val result = weixinService.materialService.materialNewsUpload(wxNews)
        val materialEx = MaterialEx(tenantId, result.mediaId, 0, null, null)
        materialExRepository.save(materialEx)
        val material = materialRepository.findOneByTenantIdAndAccountIdAndId(tenantId, accountId, materialId) ?: throw BusinessException("该图文不存在")
        try { weixinService.materialService.materialDelete(material.mediaId) } catch (ex: Exception) { ex.printStackTrace() }
        material.mediaId = result.mediaId
        material.updateTime = Date()
        material.uploaded = true
        materialRepository.save(material)
    }

    open fun replaceImgUrl(accountId: Long, content: String): String? {
        val weixinService = weixinServiceFactory.getService(accountId)
        var newContent = content
        //匹配content中的<img />标签
        val p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)")
        val m_img = p_img.matcher(newContent)
        var result_img = m_img.find()
        var localPath = propertiesService.fileTempPath
        if (result_img) {
            while (result_img) {
                //获取到匹配的<img />标签中的内容
                val str_img = m_img.group(2)
                //开始匹配<img />标签中的src
                val p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')")
                val m_src = p_src.matcher(str_img)
                if (m_src.find()) {
                    var str_src = m_src.group(3)

                    val uri = URL(str_src)
                    val urlConnection = uri.openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.connectTimeout = 5000
                    urlConnection.readTimeout = 5000
                    urlConnection.doInput = true
                    urlConnection.useCaches = false
                    urlConnection.instanceFollowRedirects = true
                    urlConnection.connect()
                    val code = urlConnection.responseCode
                    if (code != 200) return null

                    File(localPath).apply { if (!this.exists()) this.mkdir() }
                    val filename = StrUtil.guid() + ".jpg"
                    val tempFile = File(localPath, filename)
                    tempFile.createNewFile()
                    tempFile.deleteOnExit()
                    val fis = urlConnection.inputStream
                    val b = ByteArray(1024)
                    var read = fis.read(b)
                    if (read < 20) return null
                    val fos = FileOutputStream(tempFile)
                    while (read != -1) {
                        fos.write(b, 0, read)
                        read = fis.read(b)
                    }
                    fis.close()
                    fos.close()
                    val file = File(localPath + "/" + filename)
                    val result = weixinService.materialService.mediaImgUpload(file)
                    newContent = newContent.replaceFirst(str_src, result.url)
                    tempFile.delete()
                }
                //结束匹配<img />标签中的src
                //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                result_img = m_img.find()
            }
        }
        return newContent
    }

    private fun uploadMedia(url: String?, accountId: Long): String {
        val uri = URL(url)
        var localPath = propertiesService.fileTempPath
        val urlConnection = uri.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "GET"
        urlConnection.connectTimeout = 5000
        urlConnection.readTimeout = 5000
        urlConnection.doInput = true
        urlConnection.useCaches = false
        urlConnection.instanceFollowRedirects = true
        urlConnection.connect()
        val code = urlConnection.responseCode
        if (code != 200) throw BusinessException("无法读取新闻头图")
        File(localPath).apply { if (!this.exists()) this.mkdir() }
        val filename = StrUtil.guid() + ".jpg"
        val tempFile = File(localPath, filename)
        tempFile.createNewFile()
        tempFile.deleteOnExit()
        val fis = urlConnection.inputStream
        val b = ByteArray(1024)
        var read = fis.read(b)
        if (read < 20) return throw BusinessException("无效的新闻头图")
        val fos = FileOutputStream(tempFile)
        while (read != -1) {
            fos.write(b, 0, read)
            read = fis.read(b)
        }
        fis.close()
        fos.close()
        val weixinService = weixinServiceFactory.getService(accountId)
        val file = File(localPath + "/" + filename)
        val result = weixinService.materialService.materialFileUpload(MaterialType.image.type, WxMpMaterial(filename, file, null, null))
        return result.mediaId
    }

    @Transactional
    open fun delArticle(tenantId: Long, articleId: Long) {
        val exist = materialArticleRepository.findByTenantIdAndId(tenantId, articleId) ?: throw BusinessException("该图文不存在");
        materialArticleRepository.delete(exist)
        val count = materialArticleRepository.countByTenantIdAndMaterialId(tenantId, exist.materialId)
        val material = materialRepository.findOne(exist.materialId)
        if (count < 1 && material != null) {
            val service = weixinServiceFactory.getService(material.accountId)
            service.materialService.materialDelete(material.mediaId)
            materialRepository.delete(exist.materialId)
        } else {
            material.uploaded = false
            material.updateTime = Date()
            materialRepository.save(material)
        }
    }

    open fun updateArticle(tenantId: Long, accountId: Long, title: String?, intro: String?, fields: Map<String, String>): MaterialArticle {
        val exist = materialArticleRepository.findByTenantIdAndAccountIdAndId(tenantId, accountId, fields["articleId"]!!.toLong()) ?: throw BusinessException("该图文不存在")
        if (fields["title"].isNullOrBlank()) throw BusinessException("文章标题不能为空")
        if (fields["content"].isNullOrBlank()) throw BusinessException("文章内容不能为空")
        if (fields["materialId"].isNullOrBlank()) throw BusinessException("内部错误")

        val wxArticle = WxMpMaterialNews.WxMpMaterialNewsArticle()
        wxArticle.thumbMediaId = fields["thumbMediaId"]
        wxArticle.thumbUrl = fields["thumbUrl"]
        wxArticle.author = fields["author"]
        wxArticle.title = fields["title"]
        wxArticle.contentSourceUrl = fields["sourceUrl"]
        wxArticle.content = fields["content"]
        wxArticle.digest = fields["digest"]
        wxArticle.isShowCoverPic = fields["showConverImage"]!!.toBoolean()
        wxArticle.url = fields["clickUrl"]

        val article = MaterialArticle(exist.id, tenantId, accountId, 0, fields["materialId"]!!.toLong(), fields["thumbUrl"], wxArticle)
        materialArticleRepository.save(article)
        val material = materialRepository.findOneByTenantIdAndAccountIdAndId(tenantId, accountId, fields["materialId"]!!.toLong())?: throw BusinessException("该图文不存在")
        material.updateTime = Date()
        material.uploaded = false
        materialRepository.save(material)
        return article
    }

    @Transactional
    open fun addArticle(tenantId: Long, accountId: Long, title: String?, intro: String?, fields: Map<String, String>): MaterialArticle {
        if (fields["title"].isNullOrBlank()) throw BusinessException("文章标题不能为空")
        if (fields["content"].isNullOrBlank()) throw BusinessException("文章内容不能为空")
        var mid: Long = 0;
        var material = Material()
        if (fields["materialId"]!!.isNotEmpty()) {
            mid = fields["materialId"]!!.toLong()
            material = materialRepository.findOneByTenantIdAndAccountIdAndId(tenantId, accountId, mid)?: throw BusinessException("该图文不存在")
        } else {
            mid = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_MATERIAL_ID)
        }
        val id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_ARTICLE_ID)

        val wxArticle = WxMpMaterialNews.WxMpMaterialNewsArticle()
        wxArticle.thumbMediaId = fields["thumbMediaId"]
        wxArticle.thumbUrl = fields["thumbUrl"]
        wxArticle.author = fields["author"]
        wxArticle.title = fields["title"]
        wxArticle.contentSourceUrl = fields["sourceUrl"]
        wxArticle.content = fields["content"]
        wxArticle.digest = fields["digest"]
        wxArticle.isShowCoverPic = fields["showConverImage"]!!.toBoolean()
        wxArticle.url = fields["clickUrl"]

        val materialArticle = MaterialArticle(id, tenantId, accountId, 0, mid, fields["thumbUrl"], wxArticle)
        materialArticleRepository.save(materialArticle)

        if (fields["materialId"]!!.isEmpty()) {
            material = Material(mid, tenantId, accountId, 0, MaterialType.news, "${System.currentTimeMillis()}", Date(), fields["thumbUrl"], null, title, intro)
            materialRepository.save(material)
        }else{
            material.updateTime = Date()
            material.uploaded = false
            materialRepository.save(material)
        }
        return materialArticle
    }

    open fun add(tenantId: Long, accountId: Long, groupId: Long, type: MaterialType, name: String?, title: String?, intro: String?, files: List<MediaFile>): ArrayList<Map<String, String>> {
        val weixinService = weixinServiceFactory.getService(accountId)
        val index = 1;
        val list = ArrayList<Map<String, String>>()
        files.forEach {
            val file = File(it.file)
            if (!file.exists()) return@forEach
            val filename = if (name.isNullOrBlank()) file.name else name + (if (files.size > 1) "" + index else "")
            val result = weixinService.materialService.materialFileUpload(type.type, WxMpMaterial(filename, file, title, intro))

            val id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_MATERIAL_ID)
            val material = Material(id, tenantId, accountId, 0, type, result.mediaId, Date(), it.url, it.file, title, intro)
            materialRepository.save(material)
            val materialEx = MaterialEx(tenantId, result.mediaId, groupId, name, null)
            materialExRepository.save(materialEx)
            val map = HashMap<String, String>()
            map.put("url", it.url)
            map.put("mediaId", result.mediaId)
            list.add(map)
        }
        return list
    }

    open fun batchDelete(tenantId: Long, accountId: Long, mediaIds: Array<String>) {
        val materials = materialRepository.findByTenantIdAndAccountIdAndMediaIdIn(tenantId, accountId, mediaIds) ?: throw BusinessException("素材不存在")
        val service = weixinServiceFactory.getService(accountId)
        materials.forEach {
            if (it.type == MaterialType.news) {
                val exist = materialArticleRepository.findByTenantIdAndAccountIdAndMaterialId(tenantId, accountId, it.id) ?: throw BusinessException("该图文不存在");
                materialArticleRepository.delete(exist)
            }
            if (!service.materialService.materialDelete(it.mediaId)) {
                throw BusinessException("从微信删除素材失败, 素材ID=${it.id}")
            }
        }
        materialRepository.delete(materials)
    }

    @Transactional
    open fun delete(tenantId: Long, accountId: Long, id: Long) {
        val material = materialRepository.findOneByTenantIdAndAccountIdAndId(tenantId, accountId, id)
        if (material == null) throw BusinessException("素材不存在")
        if (material.type == MaterialType.news) {
            materialArticleRepository.deleteByTenantIdAndMaterialId(tenantId, id)
        }
        materialRepository.delete(material)
        val service = weixinServiceFactory.getService(accountId)
        if (!service.materialService.materialDelete(material.mediaId)) throw BusinessException("从微信删除素材失败")
    }

    open fun setGroup(tenantId: Long, ids: Array<String>, groupId: Long): Int {
        var materials = mutableListOf<MaterialEx>()
        ids.forEach {
            var exist = materialExRepository.findOneByTenantIdAndMediaId(tenantId, it)
            if (exist == null) exist = MaterialEx(tenantId, it, groupId)
            exist.groupId = groupId
            materials.add(exist)
        }
        materialExRepository.save(materials)
        return materials.size
    }
}