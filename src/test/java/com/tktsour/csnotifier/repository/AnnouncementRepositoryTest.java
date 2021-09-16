package com.tktsour.csnotifier.repository;

import com.tktsour.csnotifier.entity.Announcement;
import com.tktsour.csnotifier.service.AnnouncementHtml;
import com.tktsour.csnotifier.service.IdProvider;
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

    @Test
    public void test(){
        List<Announcement> announcements = announcementHtml
                .crawlAnnouncements(idProvider.produceQueue());

        announcementRepository.saveAll(announcements);
    }

    @Test
    public void test2(){
        Announcement announcement =
                announcementHtml.crawlAnnouncement(350l);
        announcementRepository.save(announcement);

    }

}