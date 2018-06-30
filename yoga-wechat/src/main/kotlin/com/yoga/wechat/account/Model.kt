package com.yoga.wechat.account

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "wx_account")
data class Account(@Id var id: Long = 0,
                            @Column(name = "tenant_id") var tenantId: Long = 0,
                            @Column(name = "name") var name: String = "",
                            @Column(name = "token") var token: String = "",
                            @Column(name = "number") var number: String = "",
                            @Column(name = "raw_id") var rawId: String = "",
                            @Column(name = "app_id") var appId: String = "",
                            @Column(name = "app_secret") var appSecret: String = "",
                            @Column(name = "aes_key") var aesKey: String = "",
                            @Column(name = "remark") var remark: String? = null,
                            @Column(name = "access_token") var accessToken: String? = null,
                            @Column(name = "token_expire") var tokenExpire: Date? = null)
