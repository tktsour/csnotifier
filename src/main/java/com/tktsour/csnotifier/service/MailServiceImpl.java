package com.tktsour.csnotifier.service;

import com.tktsour.csnotifier.entity.Announcement;
import com.tktsour.csnotifier.util.StringConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

@Component
public class MailServiceImpl implements MailService{


    private JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Override
    public void sendSimpleMessage(Announcement announcement) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(StringConstants.EMAIL_TO);
        message.setFrom(StringConstants.EMAIL_FROM);
        message.setSubject(StringConstants.SUBJECT_PREFIX + " " +announcement.getTitle() + " " + announcement.getLecturer());
        message.setText(announcement.getContent());
        javaMailSender.send(message);
    }
}
