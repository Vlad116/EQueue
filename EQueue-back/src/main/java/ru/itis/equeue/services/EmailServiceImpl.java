package ru.itis.equeue.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import ru.itis.equeue.models.Event;
import ru.itis.equeue.models.User;
import ru.itis.equeue.repositories.EventsRepository;
import ru.itis.equeue.repositories.UsersRepository;

import java.time.format.DateTimeFormatter;

@Service
@PropertySource("classpath:smtp.properties")
public class EmailServiceImpl implements EmailService {

    private final String HEADER = "<h3>Доброго времени суток, firstname!</h3><br>" +
            " <p>Вы зарегистрировались на сервисе электронный очередей EQuery </p>" +
            "<p>Для подтверждения регистрации </p>";
    private final String FOOTER = "<br><hr><p>Если вы не регистрировались, пожалуйста проигнорируйте данное письмо</p>";

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Override
    public void sendMail(String subject, String text, String email) {

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(mailFrom);
            messageHelper.setTo(email);
            messageHelper.setSubject(subject);
            messageHelper.setText(text, true);
        };

        javaMailSender.send(messagePreparator);
    }

    @RabbitListener(queues = "confirm_registration")
    @Override
    public void confirmRegistration(Long id) {
        User user = usersRepository.findUserById(id).get();
        String appeal = HEADER.replaceFirst("firstname", user.getFirstName());
        String text = appeal + "<a href='http://localhost:8000/confirm/" +
                user.getConfirmString() + "'>" + "пройдите по ссылке" + "</a>"
                + FOOTER;
        sendMail("Подтвреждение регистрации на сервисе Equeue", text, user.getEmail());
    }

    private final String HEADER_NOTIFICATION = "<h3>Доброго времени суток, firstname!</h3><br>" +
            " <p>Вы записались на сервисе электронных очередей EQuery на завтра в ..:..</p>";
    private final String FOOTER_NOTIFICATION = "<br><hr><p>Если вы не записывались, пожалуйста проигнорируйте данное письмо</p>";

    @RabbitListener(queues = "event_notification")
    @Override
    public void notification(String eventIdPlusUserId) {

        String[] data = eventIdPlusUserId.split(" ", 2);
        Long eventId = Long.parseLong(data[0]);
        Long userId = Long.parseLong(data[1]);

        System.out.println(userId);
        System.out.println(eventId);
        System.out.println(userId == 5L);

        User user = usersRepository.findUserById(userId).get();
        Event event = eventsRepository.findEventById(eventId).get();
        System.out.println(user.getFirstName());
        System.out.println(event.getTitle());

        // Custom format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // Format LocalDateTime
        String formattedDateTime = event.getEventStartTime().format(formatter);

        String appeal = HEADER_NOTIFICATION.replaceFirst("firstname", user.getFirstName()).replaceFirst("..:..", formattedDateTime);

        String text = appeal + "<a href='http://localhost:8000/events'>"
                + " Пройдите по ссылке для перехода в профиль и просмотра текущих записей"
                + "</a>" + FOOTER_NOTIFICATION;
        sendMail("Напоминание о записи на " + event.getTitle() + " в " + formattedDateTime + "!", text, user.getEmail());
    }
}
