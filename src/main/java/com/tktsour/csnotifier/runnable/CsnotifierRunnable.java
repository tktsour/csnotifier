package com.tktsour.csnotifier.runnable;

import com.tktsour.csnotifier.entity.Announcement;
import com.tktsour.csnotifier.repository.AnnouncementRepository;
import com.tktsour.csnotifier.service.AnnouncementHtml;
import com.tktsour.csnotifier.service.IdProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;

@Component
public class CsnotifierRunnable implements Runnable{

    AnnouncementHtml announcementHtml;
    AnnouncementRepository announcementRepository;
    IdProvider idProvider;

    @Autowired
    public CsnotifierRunnable(AnnouncementHtml announcementHtml, AnnouncementRepository announcementRepository, IdProvider idProvider) {
        this.announcementHtml = announcementHtml;
        this.announcementRepository = announcementRepository;
        this.idProvider = idProvider;
    }

    @Override
    public void run() {
        Queue<Long> idQueue = idProvider.produceQueue();

        List<Announcement> announcements = announcementHtml
                .crawlAnnouncements(idQueue);

        if(announcements.isEmpty()){
            System.out.println("no new announcement");
        }else{
            announcementRepository.saveAll(announcements);
        }
        System.out.println(announcements);
        System.out.println("completed one cycle");
    }
}
