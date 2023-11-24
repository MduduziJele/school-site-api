package school.site.api.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailSenderService {
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender mailSender){
        this.javaMailSender = mailSender;
    }
    @Async
    public void sendEmail(SimpleMailMessage mail) {
        javaMailSender.send(mail);
    }
}