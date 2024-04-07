package ua.com.agroswit.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ua.com.agroswit.model.User;
import ua.com.agroswit.model.enums.MailType;
import ua.com.agroswit.service.MailSenderService;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {


    private final JavaMailSender mailSender;


    @Override
    public void sendEmail(User user, MailType type) {
        switch (type) {
            case REGISTRATION -> sendRegistrationMessage(user);
            default -> {}
        }
    }

    private void sendRegistrationMessage(User user) {
        var message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setText("Welcome to the club buddy");

        log.info("Sending registration message {} to user email: {}", message, user.getEmail());
        mailSender.send(message);
    }
}
