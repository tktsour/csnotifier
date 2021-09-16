package com.tktsour.csnotifier.runnable;

import com.tktsour.csnotifier.entity.Announcement;
import com.tktsour.csnotifier.repository.AnnouncementRepository;
import com.tktsour.csnotifier.service.AnnouncementHtml;
import com.tktsour.csnotifier.service.IdProvider;
import com.tktsour.csnotifier.service.MailService;
import com.tktsour.csnotifier.service.MailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;

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
            System.out.println("queue produced succuesfully");
            System.out.println(idQueue);
            List<Announcement> announcements = announcementHtml
                    .crawlAnnouncements(idQueue);
            System.out.println("announcements created");

            if(announcements.isEmpty()){
                System.out.println("no new announcement");
            }else{
                System.out.println("now sending announcements");
                for (int i = 0; i < announcements.size(); i++) {
                    mailService.sendSimpleMessage(announcements.get(i));
                    System.out.println("sent announcement" + announcements.get(i).getId());
                }
                announcementRepository.saveAll(announcements);
                System.out.println("announcements sent to db");
            }
            System.out.println(announcements);
            System.out.println("completed one cycle");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
