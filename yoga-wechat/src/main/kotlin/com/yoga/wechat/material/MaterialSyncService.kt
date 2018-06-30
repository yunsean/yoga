package com.yoga.wechat.material

import com.yoga.core.property.PropertiesService
import com.yoga.core.service.BaseService
import com.yoga.core.utils.StrUtil
import com.yoga.wechat.sequence.SequenceNameEnum
import com.yoga.wechat.util.WxSyncAction
import com.yoga.wechat.weixin.WeixinService
import com.yoga.wechat.weixin.WeixinServiceFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

@Service(value = "wechatMaterialSyncService")
open class MaterialSyncService @Autowired constructor(
        val weixinServiceFactory: WeixinServiceFactory,
        val materialRepository: MaterialRepository,
        val materialArticleRepository: MaterialArticleRepository,
        val materialExRepository: MaterialExRepository,
        val propertiesService: PropertiesService,
        val jdbcTemplate: JdbcTemplate) : BaseService() {

    @Transactional
    open fun syncMaterial(type: MaterialType, syncIndex: Long, tenantId: Long, accountId: Long, action: WxSyncAction) {
        val service = weixinServiceFactory.getService(accountId)
        jdbcTemplate.update("set names utf8mb4;")
        if (action == WxSyncAction.news) {
            var totalCount = 0
            do {
                logger.info("开始拉取${action.desc}，从${totalCount}开始")
                val wxMedias = service.materialService.materialNewsBatchGet(totalCount, 20)
                logger.info("拉取到 ${wxMedias.itemCount} 条记录")
                if (wxMedias.itemCount < 1) break
                wxMedias.items.forEach {
                    val mid = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_MATERIAL_ID)
                    val medias = mutableListOf<Material>()
                    medias.add(Material(mid, tenantId, accountId, syncIndex, it))
                    it.content?.articles?.forEach {
                        val aid = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_ARTICLE_ID)
                        val media = materialRepository.findFirstByTenantIdAndMediaId(tenantId, it.thumbMediaId)
                        var thumbUrl: String? = it.thumbUrl
                        if (media == null) {
                            val file = downloadFile(it.thumbUrl, tenantId, it.thumbMediaId, "jpg")
                            if (file != null) {
                                val id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_MATERIAL_ID)
                                thumbUrl = file.replace(propertiesService.fileLocalPath, propertiesService.fileRemotePath, false)
                                medias.add(Material(id, tenantId, accountId, syncIndex, MaterialType.image, it.thumbMediaId, Date(), thumbUrl, file))
                            }
                        }
                        val article = MaterialArticle(aid, tenantId, accountId, syncIndex, mid, thumbUrl, it)
                        logger.info("content length = ${article.content?.length}")
                        materialArticleRepository.save(article)
                    }
                    materialRepository.save(medias)
                }
                totalCount += wxMedias.itemCount
            } while (totalCount < wxMedias.totalCount)
            logger.info("共拉取到 ${totalCount} 条记录")
            materialRepository.deleteByTenantIdAndAccountIdAndTypeAndBatchIndexNot(tenantId, accountId, type, syncIndex)
            materialArticleRepository.deleteByTenantIdAndAccountIdAndBatchIndexNot(tenantId, accountId, syncIndex)
        } else {
            var totalCount = 0
            do {
                logger.info("开始拉取${action.desc}，从${totalCount}开始")
                val wxMedias = service.materialService.materialFileBatchGet(action.type, totalCount, 20)
                logger.info("拉取到 ${wxMedias.itemCount} 条记录")
                if (wxMedias.itemCount < 1) break
                val medias = mutableListOf<Material>()
                val existMediaExs = materialExRepository.findByTenantIdAndMediaIdIn(tenantId, wxMedias.items.map { it.mediaId })
                val mediaExs = mutableListOf<MaterialEx>()
                wxMedias.items.forEach {
                    var title: String? = null
                    var intro: String? = null
                    var file: String?
                    var url: String?
                    if (action == WxSyncAction.video) {
                        val info = service.materialService.materialVideoInfo(it.mediaId)
                        title = info.title
                        intro = info.description
                        file = downloadFile(info.downUrl, tenantId, it.mediaId)
                        url = file?.replace(propertiesService.fileLocalPath, propertiesService.fileRemotePath, false)
                    } else {
                        title = it.name
                        file = downloadVoiceOrImage(service, action, tenantId, it.mediaId)
                        url = file?.replace(propertiesService.fileLocalPath, propertiesService.fileRemotePath, false)
                    }
                    val id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_MATERIAL_ID)
                    service.materialService.materialImageOrVoiceDownload(it.mediaId)
                    medias.add(Material(id, tenantId, accountId, syncIndex, type, url, file, title, intro, it))
                    var ex = existMediaExs.find { ex -> it.mediaId == ex.mediaId }
                    if (ex == null) ex = MaterialEx(tenantId, it.mediaId, 0, it.name)
                    mediaExs.add(ex)
                }
                materialRepository.save(medias)
                materialExRepository.save(mediaExs)
                totalCount += wxMedias.itemCount
            } while (totalCount < wxMedias.totalCount)
            logger.info("共拉取到 ${totalCount} 条记录")
            val old = materialRepository.findByTenantIdAndAccountIdAndTypeAndBatchIndexNot(tenantId, accountId, type, syncIndex)
            old.forEach {
                try {
                    File(it.file).delete()
                } catch (ex: Exception) {
                }
            }
            materialRepository.deleteByTenantIdAndAccountIdAndTypeAndBatchIndexNot(tenantId, accountId, type, syncIndex)
        }
    }

    private fun downloadFile(url: String?, tenantId: Long, mediaId: String, extName: String = "mp4"): String? {
        if (url.isNullOrBlank()) return null;
        var filePath = propertiesService.getFileLocalPath(tenantId)
        File(filePath).apply { if (!this.exists()) this.mkdirs() }
        filePath += StrUtil.guid()
        try {
            val uri = URL(url)
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
            val fis = urlConnection.inputStream
            val b = ByteArray(1024)
            var read = fis.read(b)
            if (read < 20) return null
            filePath += "." + getFileType(b, extName)
            val fos = FileOutputStream(filePath)
            while (read != -1) {
                fos.write(b, 0, read)
                read = fis.read(b)
            }
            fis.close()
            fos.close()
            return filePath
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
    }

    private fun downloadVoiceOrImage(weixinService: WeixinService, action: WxSyncAction, tenantId: Long, mediaId: String): String? {
        var filePath = propertiesService.getFileLocalPath(tenantId)
        File(filePath).apply {
            if (!this.exists()) this.mkdirs()
        }
        filePath += StrUtil.guid()
        try {
            val fis = weixinService.materialService.materialImageOrVoiceDownload(mediaId)
            val b = ByteArray(1024)
            var read = fis.read(b)
            if (read < 20) return null
            filePath += "." + getFileType(b, when (action) {
                WxSyncAction.image -> "jpg"
                WxSyncAction.voice -> "mp3"
                else -> "dat"
            })
            val fos = FileOutputStream(filePath)
            while (read != -1) {
                fos.write(b, 0, read)
                read = fis.read(b)
            }
            fis.close()
            fos.close()
            return filePath
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
    }


    val fileTypes = mapOf(
            "ffd8ffe000104a464946" to "jpg", //JPEG (jpg)
            "89504e470d0a1a0a0000" to "png", //PNG (png)
            "47494638396126026f01" to "gif", //GIF (gif)
            "49492a00227105008037" to "tif", //TIFF (tif)
            "424d228c010000000000" to "bmp", //16色位图(bmp)
            "424d8240090000000000" to "bmp", //24位位图(bmp)
            "424d8e1b030000000000" to "bmp", //256色位图(bmp)
            "41433130313500000000" to "dwg", //CAD (dwg)
            "3c21444f435459504520" to "html", //HTML (html)
            "3c21646f637479706520" to "htm", //HTM (htm)
            "48544d4c207b0d0a0942" to "css", //css
            "696b2e71623d696b2e71" to "js", //js
            "7b5c727466315c616e73" to "rtf", //Rich Text Format (rtf)
            "38425053000100000000" to "psd", //Photoshop (psd)
            "46726f6d3a203d3f6762" to "eml", //Email [Outlook Express 6] (eml)
            "d0cf11e0a1b11ae10000" to "doc", //MS Excel 注意：word、msi 和 excel的文件头一样
            "d0cf11e0a1b11ae10000" to "vsd", //Visio 绘图
            "5374616E64617264204A" to "mdb", //MS Access (mdb)
            "252150532D41646F6265" to "ps",
            "255044462d312e350d0a" to "pdf", //Adobe Acrobat (pdf)
            "2e524d46000000120001" to "rmvb", //rmvb/rm相同
            "464c5601050000000900" to "flv", //flv与f4v相同
            "00000020667479706d70" to "mp4",
            "49443303000000002176" to "mp3",
            "000001ba210001000180" to "mpg",
            "3026b2758e66cf11a6d9" to "wmv", //wmv与asf相同
            "52494646e27807005741" to "wav", //Wave (wav)
            "52494646d07d60074156" to "avi",
            "4d546864000000060001" to "mid", //MIDI (mid)
            "504b0304140000000800" to "zip",
            "526172211a0700cf9073" to "rar",
            "235468697320636f6e66" to "ini",
            "504b03040a0000000000" to "jar",
            "4d5a9000030000000400" to "exe", //可执行文件
            "3c25402070616765206c" to "jsp", //jsp文件
            "4d616e69666573742d56" to "mf", //MF文件
            "3c3f786d6c2076657273" to "xml", //xml文件
            "494e5345525420494e54" to "sql", //xml文件
            "7061636b616765207765" to "java", //java文件
            "406563686f206f66660d" to "bat", //bat文件
            "1f8b0800000000000000" to "gz", //gz文件
            "6c6f67346a2e726f6f74" to "properties", //bat文件
            "cafebabe0000002e0041" to "class", //bat文件
            "49545346030000006000" to "chm", //bat文件
            "04000000010000001300" to "mxp", //bat文件
            "504b0304140006000800" to "docx", //docx文件
            "d0cf11e0a1b11ae10000" to "wps", //WPS文字wps、表格et、演示dps都是一样的
            "6431303a637265617465" to "torrent",
            "6D6F6F76" to "mov", //Quicktime (mov)
            "FF575043" to "wpd", //WordPerfect (wpd)
            "CFAD12FEC5FD746F" to "dbx", //Outlook Express (dbx)
            "2142444E" to "pst", //Outlook (pst)
            "AC9EBD8F" to "qdf", //Quicken (qdf)
            "E3828596" to "pwl", //Windows Password (pwl)
            "2E7261FD" to "ram"                 //Real Audio (ram)
    )

    fun getFileType(datas: ByteArray, def: String): String {
        val hex = bytesToHexString(datas)
        var result: String? = null
        if (hex == null) return def
        fileTypes.forEach { t, u ->
            if (hex.startsWith(t)) {
                result = u
                return@forEach
            }
        }
        return result ?: def
    }

    private fun bytesToHexString(src: ByteArray?): String? {
        val builder = StringBuilder()
        if (src == null || src.size <= 0) return null
        var hv: String
        for (i in 0..minOf(20, src.size)) {
            hv = Integer.toHexString(src[i].toInt() and 0xFF).toUpperCase()
            if (hv.length < 2) builder.append(0)
            builder.append(hv)
        }
        return builder.toString()
    }
}