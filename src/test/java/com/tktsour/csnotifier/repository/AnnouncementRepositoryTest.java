package com.tktsour.csnotifier.repository;

import com.tktsour.csnotifier.entity.Announcement;
import com.tktsour.csnotifier.service.AnnouncementHtml;
import com.tktsour.csnotifier.service.IdProvider;
import com.tktsour.csnotifier.service.MailServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class AnnouncementRepositoryTest {

    @Autowired
    IdProvider idProvider;

    @Autowired
    AnnouncementHtml announcementHtml;

    @Autowired
    AnnouncementRepository announcementRepository;

    @Autowired
    MailServiceImpl mailService;

    @Test
    public void test(){
        Announcement announcement = announcementRepository.findById(370L).get();
        mailService.sendSimpleMessage(announcement);

    }

    @Test
    public void test2(){
        announcementRepository.deleteAll();

        Announcement announcement =
                announcementHtml.crawlAnnouncement(370l);
        announcementRepository.save(announcement);

    }

}