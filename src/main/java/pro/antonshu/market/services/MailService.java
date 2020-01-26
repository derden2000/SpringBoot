package pro.antonshu.market.services;

import org.springframework.stereotype.Service;
import pro.antonshu.market.entities.Mail;

@Service
public interface MailService {

    void sendEmail(Mail mail);
}
