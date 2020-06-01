package ru.itis.equeue.services;

public interface EmailService {
    void sendMail(String subject, String text, String email);

    void confirmRegistration(Long id);

    void notification(String eventIdPlusUserId);
    //    void registration(User user); - не работает(
}
