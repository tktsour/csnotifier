package com.tktsour.csnotifier.service;

import com.tktsour.csnotifier.entity.Announcement;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MailService {
    public void sendSimpleMessage(String to, String subject, String text);
    public void sendSimpleMessage(Announcement announcement);
}
