package com.tktsour.csnotifier.runnable;

import com.tktsour.csnotifier.entity.Announcement;
import com.tktsour.csnotifier.repository.AnnouncementRepository;
import com.tktsour.csnotifier.service.AnnouncementHtml;
import com.tktsour.csnotifier.service.IdProvider;
import com.tktsour.csnotifier.service.MailService;
import com.tktsour.csnotifier.service.MailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;

@Slf4j
@Component
public class CsnotifierRunnable implements Runnable{

    private AnnouncementHtml announcementHtml;
    private AnnouncementRepository announcementRepository;
    private IdProvider idProvider;
    private MailServiceImpl mailService;


    @Autowired
    public CsnotifierRunnable(AnnouncementHtml announcementHtml, AnnouncementRepository announcementRepository,
                              IdProvider idProvider, MailServiceImpl mailService) {
        this.announcementHtml = announcementHtml;
        this.announcementRepository = announcementRepository;
        this.idProvider = idProvider;
        this.mailService = mailService;
    }

    @Override
    public void run() {
        try {
            Queue<Long> idQueue = idProvider.produceQueue();
            System.out.println(idQueue);
            Queue<Announcement> announcements = announcementHtml.crawlAnnouncements(idQueue);
            if(announcements.isEmpty()){
                log.info("Announcements queue is empty.");
            }else{
                log.info("Attempting to send announcements");
                while (!announcements.isEmpty()){
                    Announcement announcement = announcements.remove();
                    String id = announcement.getId().toString();
                    log.info("Attempting to send announcement %s", id);

                    mailService.sendSimpleMessage(announcement);
                    log.info("Announcement sent successfully, now attempting to persist %s",id);

                    announcementRepository.save(announcement);
                    log.info("Announcement %s persisted successfully",id);
                    log.info("Queue's state: %s", announcements.toString());
                }
                log.info("Announcements in Queue processed successfully");
            }
            log.info("One cycle has completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
