package com.tktsour.csnotifier.repository;

import com.tktsour.csnotifier.entity.Announcement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class AnnouncementRepositoryTest {

    @Autowired
    AnnouncementRepository announcementRepository;

    @Test
    public void test(){
        Announcement announcement = Announcement.builder()
                .content("Content")
                .title("Title")
                .id(100L)
                .dateTime(LocalDateTime.now())
                .build();

        announcementRepository.save(announcement);
    }

}