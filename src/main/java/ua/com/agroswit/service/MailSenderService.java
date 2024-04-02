package ua.com.agroswit.service;

import ua.com.agroswit.model.User;
import ua.com.agroswit.model.enums.MailType;

public interface MailSenderService {
    void sendEmail(User user, MailType type);
}
