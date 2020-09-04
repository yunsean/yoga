package com.yoga.utility.mail.service;

import com.sun.mail.util.TraceInputStream;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.utils.StringUtil;
import com.yoga.setting.service.SettingService;
import com.yoga.utility.mail.model.MailSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
@EnableAsync
public class MailService {

    public static final String ModuleName = "gcf_email";
    public static final String Key_Email = "email.setting";

    @Autowired
    private SettingService settingService;

    @Bean
    public AsyncTaskExecutor mailTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("Mail-Executor");
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(250);
        executor.initialize();
        return executor;
    }

    @Async("mailTaskExecutor")
    public void send(long tenantId, String from, String to, String toEmail, String subject, String content) {
        if (StringUtil.isBlank(toEmail)) return;
        MailSetting setting = getSetting(tenantId);
        if (setting == null) throw new BusinessException("尚未配置邮件服务器！");
        if (StringUtil.hasBlank(setting.getSmtpServer(), setting.getSendAccount())) throw new BusinessException("尚未配置邮件服务器！");
        send(setting.getSmtpServer(), setting.getSmtpPort(), from, setting.getReplyAddress(), setting.getSendAccount(), setting.getSendPassword(), to, toEmail, subject, content, setting.isUseSsl(), true);
    }

    public static boolean send(String server, int port, String from, String fromEmail, String account, String password, String to, String toEmail, String subject, String content, boolean useSsl, boolean allowFail) {
        try {
            Properties properties = new Properties();
            properties.setProperty("mail.transport.protocol", "smtp");
            properties.setProperty("mail.smtp.host", server);
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.port", String.valueOf(port));
            if (useSsl) {
                properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                properties.setProperty("mail.smtp.socketFactory.fallback", "false");
                properties.setProperty("mail.smtp.socketFactory.port", String.valueOf(port));
            }
            Session session = Session.getInstance(properties);
            session.setDebug(true);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, from, "UTF-8"));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toEmail, to, "UTF-8"));
            message.setSubject(subject, "UTF-8");
            message.setContent(content, "text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();
            Transport transport = session.getTransport();
            transport.connect(account, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (!allowFail) throw new BusinessException(ex.getLocalizedMessage() == null ? "发送邮件失败" : ex.getLocalizedMessage());
            return false;
        }
    }

    public MailSetting getSetting(long tenantId) {
        return settingService.get(tenantId, ModuleName, Key_Email, MailSetting.class);
    }
    public void setSetting(long tenantId, MailSetting value) {
        settingService.save(tenantId, ModuleName, Key_Email, value, value.getSmtpServer() + "/" + value.getSendAccount());
    }
}
