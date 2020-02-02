package pro.antonshu.market.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pro.antonshu.market.entities.Mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

@Component
public class MailServiceImpl implements MailService {

    private JavaMailSender mailSender;

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(Mail mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(mail.getMailFrom());
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(mail.getMailContent());
            List<File> list = mail.getAttachments();
            if (mail.getAttachments() != null) {
                mail.getAttachments().stream().forEach(e -> {
                    try {
                        mimeMessageHelper.addAttachment(e.getName(), e);
                    } catch (MessagingException ex) {
                        ex.printStackTrace();
                    }
                });
            }


            mailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
