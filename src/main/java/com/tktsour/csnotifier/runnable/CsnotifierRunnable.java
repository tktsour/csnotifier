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
            List<Announcement> announcements = announcementHtml
                    .crawlAnnouncements(idQueue);

            if(announcements.isEmpty()){
                log.info("Announcements queue is empty.");
            }else{
                log.info("Attempting to send announcements");
                for (int i = 0; i < announcements.size(); i++) {
                    mailService.sendSimpleMessage(announcements.get(i));
                    log.info("Mail with id:%d sent successfully",announcements.get(i).getId());
                }
                log.info("Attempting to persist all announcements");
                announcementRepository.saveAll(announcements);
                log.info("Announcements in Queue persisted successfully");
            }
            log.info("Announcements that were processed %s", announcements);
            log.info("One cycle has completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
