package ru.itis.equeue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Properties;


@Configuration
@PropertySource("classpath:smtp.properties")
public class MailConfiguration {

    @Resource
    private Environment environment;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(environment.getProperty("spring.mail.host"));
        mailSender.setPort(Integer.parseInt(Objects.requireNonNull(environment.getProperty("spring.mail.port"))));
        mailSender.setUsername(environment.getProperty("spring.mail.username"));
        mailSender.setPassword(environment.getProperty("spring.mail.password"));
        mailSender.setDefaultEncoding("UTF-8");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", environment.getProperty("spring.mail.transport.protocol"));
        props.put("mail.smtp.auth", environment.getProperty("spring.mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", environment.getProperty("spring.mail.smtp.starttls.enable"));
        props.put("mail.debug", environment.getProperty("spring.mail.debug"));
        props.put("mail.smtp.ssl.trust", environment.getProperty("spring.mail.smtp.ssl.trust"));
        props.put("mail.smtp.allow8bitmime", environment.getProperty("spring.mail.smtp.allow8bitmime"));

        return mailSender;
    }
}
