package com.yoga.utility.mail.dto;

import com.yoga.core.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailSettingSaveDto extends BaseDto {

    @NotBlank(message = "请填写SMTP服务器地址")
    private String smtpServer;
    private int smtpPort = 25;
    private String replyAddress;
    @NotBlank(message = "请填写发件人账号")
    private String sendAccount;
    @NotBlank(message = "请填写发件人密码")
    private String sendPassword;
    private boolean useSsl = false;
}
