package com.yoga.utility.mail.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailSetting {

    private String smtpServer;
    private int smtpPort = 25;
    private String replyAddress;
    private String sendAccount;
    private String sendPassword;
    private boolean useSsl = false;
}
