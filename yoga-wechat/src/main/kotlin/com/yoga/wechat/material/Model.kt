package com.yoga.wechat.material

import com.fasterxml.jackson.annotation.JsonInclude
import com.yoga.core.data.BaseEnum
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult
import java.io.Serializable
import java.util.*
import javax.persistence.*

enum class MaterialType(val type: String, val desc: String) : BaseEnum<String> {
    image("image", "图片"),
    video("video", "视频"),
    voice("voice", "音频"),
    thumb("thumb", "头图"),
    file("file", "文件"),
    news("news", "图文");

    override fun getCode(): String = type
    override fun getName(): String = desc

    companion object {
        fun getEnum(type: String?): MaterialType {
            if (type == null) return MaterialType.image
            enumValues<MaterialType>().forEach {
                if (it.type == type) return@getEnum it
            }
            return MaterialType.image
        }
    }
}

@Entity(name = "wechatMaterial")
@Table(name = "wx_material")
data class Material(@Id var id: Long = 0,
                    @Column(name = "tenant_id") var tenantId: Long = 0,
                    @Column(name = "account_id") var accountId: Long = 0,
                    @Column(name = "batch_index") var batchIndex: Long = 0,
                    @Enumerated(EnumType.STRING) @Column(name = "type") var type: MaterialType = MaterialType.image,
                    @Column(name = "media_id") var mediaId: String = "",
                    @Column(name = "update_time") var updateTime: Date = Date(),
                    @Column(name = "url") var url: String? = null,
                    @Column(name = "file") var file: String? = null,
                    @Column(name = "title") var title: String? = null,
                    @Column(name = "intro") var intro: String? = null,
                    @Column(name = "is_uploaded") var uploaded: Boolean = false) {
    constructor(id: Long, tenantId: Long, accountId: Long, batchIndex: Long, type: MaterialType, url: String?, file: String?, title: String?, intro: String?, wxMedia: WxMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem) :
            this(id, tenantId, accountId, batchIndex, type, wxMedia.mediaId, wxMedia.updateTime, url, file, title, intro)

    constructor(id: Long, tenantId: Long, accountId: Long, batchIndex: Long, wxNews: WxMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem) :
            this(id, tenantId, accountId, batchIndex, MaterialType.news, wxNews.mediaId, wxNews.updateTime)
}

@Entity
@Table(name = "wx_material_group")
data class MaterialGroup(@Id var id: Long = 0,
                         @Column(name = "tenant_id") var tenantId: Long = 0,
                         @Column(name = "name") var name: String = "",
                         @Column(name = "remark") var remark: String? = null)

@SqlResultSetMappings(SqlResultSetMapping(name = "ReturnWxMaterialGroupCount",
        entities = arrayOf(EntityResult(entityClass = MaterialGroupCount::class))))
@Entity
data class MaterialGroupCount(@Id var id: Long = 0,
                              @Column(name = "tenant_id") var tenantId: Long = 0,
                              @Column(name = "name") var name: String = "",
                              @Column(name = "remark") var remark: String? = null,
                              @Column(name = "count") var count: Int = 0)

@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
data class MaterialExPK(@Id @Column(name = "tenant_id") var tenantId: Long = 0,
                             @Id @Column(name = "media_id") var mediaId: String = "") : Serializable
@Entity
@IdClass(MaterialExPK::class)
@Table(name = "wx_material_ex")
data class MaterialEx(@Id @Column(name = "tenant_id") var tenantId: Long = 0,
                      @Id @Column(name = "media_id") var mediaId: String = "",
                      @Column(name = "group_id") var groupId: Long = 0,
                      @Column(name = "name") var name: String? = null,
                      @Column(name = "remark") var remark: String? = null)

@SqlResultSetMappings(SqlResultSetMapping(name = "ReturnMaterialAndEx",
        entities = arrayOf(EntityResult(entityClass = MaterialAndEx::class))))
@Entity
data class MaterialAndEx(@Id var id: Long = 0,
                         @Column(name = "tenant_id") var tenantId: Long = 0,
                         @Column(name = "account_id") var accountId: Long = 0,
                         @Column(name = "batch_index") var batchIndex: Long = 0,
                         @Enumerated(EnumType.STRING) @Column(name = "type") var type: MaterialType = MaterialType.image,
                         @Column(name = "media_id") var mediaId: String = "",
                         @Column(name = "update_time") var updateTime: Date = Date(),
                         @Column(name = "url") var url: String? = null,
                         @Column(name = "file") var file: String? = null,
                         @Column(name = "title") var title: String? = null,
                         @Column(name = "intro") var intro: String? = null,
                         @Column(name = "group_id") var groupId: Long? = 0L,
                         @Column(name = "group_name") var groupName: String? = null,
                         @Column(name = "name") var name: String? = null,
                         @Column(name = "remark") var remark: String? = null,
                         @Column(name = "is_uploaded") var uploaded: Boolean = false,
                         @Transient var articles: List<MaterialArticle>? = null)

@Entity
@Table(name = "wx_article")
data class MaterialArticle(@Id var id: Long = 0,
                           @Column(name = "tenant_id") var tenantId: Long = 0,
                           @Column(name = "account_id") var accountId: Long = 0,
                           @Column(name = "batch_index") var batchIndex: Long = 0,
                           @Column(name = "material_id") var materialId: Long = 0,
                           @Column(name = "title") var title: String? = null,
                           @Column(name = "author") var author: String? = null,
                           @Column(name = "thumb_url") var thumbUrl: String? = null,
                           @Column(name = "thumb_media_id") var thumbMediaId: String? = null,
                           @Column(name = "digest") var digest: String? = null,
                           @Column(name = "content") var content: String? = null,
                           @Column(name = "source_url") var sourceUrl: String? = null,
                           @Column(name = "show_conver_image") var showConverImage: Boolean = true,
                           @Column(name = "click_url") var clickUrl: String? = null) {
    constructor(id: Long, tenantId: Long, accountId: Long, batchIndex: Long, materialId: Long, thumbUrl: String?, article: WxMpMaterialNews.WxMpMaterialNewsArticle) :
            this(id, tenantId, accountId, batchIndex, materialId, article.title, article.author, thumbUrl, article.thumbMediaId, article.digest, article.content, article.contentSourceUrl, article.isShowCoverPic, article.url)
}





