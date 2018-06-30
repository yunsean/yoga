package com.yoga.wechat.users

import com.yoga.core.data.BaseEnum
import me.chanjar.weixin.mp.bean.result.WxMpUser
import java.io.Serializable
import java.util.*
import javax.persistence.*


enum class UserSex(val type: String, val desc: String) : BaseEnum<String> {
    unknown("", "保密"),
    male("男", "男"),
    female("女", "女");

    override fun getCode(): String = type
    override fun getName(): String = desc

    companion object {
        fun getEnum(type: String?): UserSex {
            if (type == null) return UserSex.unknown
            enumValues<UserSex>().forEach {
                if (it.type == type) return@getEnum it
            }
            return UserSex.unknown
        }
    }
}

@Entity(name = "wechatUser")
@Table(name = "wx_users")
data class User(@Id var id: Long = 0,
                @Column(name = "tenant_id") var tenantId: Long = 0,
                @Column(name = "account_id") var accountId: Long = 0,
                @Column(name = "batch_index") var batchIndex: Long = 0,
                @Column(name = "subscribe") var subscribe: Boolean = false,
                @Column(name = "open_id") var openId: String = "",
                @Column(name = "union_id") var unionId: String? = null,
                @Column(name = "group_id") var groupId: Int? = null,
                @Column(name = "nickname") var nickname: String? = null,
                @Enumerated(EnumType.STRING) @Column(name = "sex") var sex: UserSex = UserSex.unknown,
                @Column(name = "city") var city: String? = null,
                @Column(name = "province") var province: String? = null,
                @Column(name = "country") var country: String? = null,
                @Column(name = "language") var language: String? = null,
                @Column(name = "avatar") var headImgUrl: String? = null,
                @Column(name = "subscribe_time") var subscribeTime: Date? = null,
                @Column(name = "tagid_list") var tagIdList: String? = null,
                @Column(name = "remark") var remark: String? = null) {
    constructor(id: Long, tenantId: Long, accountId: Long, batchIndex: Long, wxUser: WxMpUser) :
            this(id, tenantId, accountId, batchIndex, wxUser.subscribe, wxUser.openId, wxUser.unionId,
                    wxUser.groupId, wxUser.nickname, UserSex.getEnum(wxUser.sex), wxUser.city,
                    wxUser.province, wxUser.country, wxUser.language, wxUser.headImgUrl, Date(wxUser.subscribeTime),
                    "|" + wxUser.tagIds.joinToString("|") + "|", wxUser.remark)

    fun setTagIdList(tagIds: List<String>?) {
        this.tagIdList = "|" + (tagIds?.joinToString("|") ?: "") + "|";
    }
}


@Embeddable
data class UserBindingPK(@Id @Column(name = "tenant_id") var tenantId: Long = 0,
                         @Id @Column(name = "local_user_id") var userId: Long = 0): Serializable

@Entity(name = "wechatUserBinding")
@Table(name = "wx_user_binding")
@IdClass(UserBindingPK::class)
data class UserBinding(@Id @Column(name = "tenant_id") var tenantId: Long = 0,
                       @Id @Column(name = "local_user_id") var userId: Long = 0,
                       @Column(name = "account_id") var accountId: Long = 0,
                       @Column(name = "open_id") var openId: String = "")


@SqlResultSetMappings(SqlResultSetMapping(name = "ReturnWxUserBinding",
        entities = arrayOf(EntityResult(entityClass = BindUser::class))))
@Entity
data class BindUser(@Id var id: Long = 0,
                       @Column(name = "tenant_id") var tenantId: Long = 0,
                       @Column(name = "account_id") var accountId: Long = 0,
                       @Column(name = "batch_index") var batchIndex: Long = 0,
                       @Column(name = "subscribe") var subscribe: Boolean = false,
                       @Column(name = "open_id") var openId: String = "",
                       @Column(name = "union_id") var unionId: String? = null,
                       @Column(name = "group_id") var groupId: Int? = null,
                       @Column(name = "nickname") var nickname: String? = null,
                       @Enumerated(EnumType.STRING) @Column(name = "sex") var sex: UserSex = UserSex.unknown,
                       @Column(name = "city") var city: String? = null,
                       @Column(name = "province") var province: String? = null,
                       @Column(name = "country") var country: String? = null,
                       @Column(name = "language") var language: String? = null,
                       @Column(name = "avatar") var headImgUrl: String? = null,
                       @Column(name = "subscribe_time") var subscribeTime: Date? = null,
                       @Column(name = "tagid_list") var tagIdList: String? = null,
                       @Column(name = "remark") var remark: String? = null,
                       @Column(name = "local_user_id") var userId: Long? = null,
                       @Column(name = "fullname") var fullname: String? = null,
                       @Column(name = "username") var username: String? = null)





