package com.tktsour.csnotifier.service;

import com.tktsour.csnotifier.entity.Announcement;
import com.tktsour.csnotifier.util.StringConstants;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

@Service
@Slf4j
public class AnnouncementHtml {

    Announcement error = Announcement.builder()
            .id(404l)
            .title("ERROR")
            .content("ERROR")
            .lecturer("ERROR")
            .dateTime(LocalDateTime.now())
            .build();
    public Queue<Announcement> crawlAnnouncements(Queue<Long> ids){
        Queue<Announcement> announcements = new ArrayDeque<>();
        while(!ids.isEmpty()){
            announcements.add(crawlAnnouncement(ids.remove()));
        }
        return announcements;
    }

    public Announcement crawlAnnouncement(Long id){
        try {
            log.info("Attempting connection with %s crawlAnnouncement(Long id)",StringConstants.ID_URL.concat(id.toString()));
            Document document = SSLHelper.getConnection(StringConstants.ID_URL+id).get();
            log.info("Connection established successfully");
            log.info("Parsing necessary html elements");
            Element bodyElement = document.getElementById("j_idt33_editor");
            Element titleElement = document.select("h3").get(0);
            Element lecturerElement = document.getElementById("headerColumn");
            Announcement announcement =
                    Announcement.builder()
                            .id(id)
                            .title(titleElement.text())
                            .content(bodyElement.text())
                            .lecturer(nameExtractor(lecturerElement.child(0).text()))
                            .dateTime(LocalDateTime.now())
                            .build();
            log.info("Announcement object created with id:%d",announcement.getId());
            log.info(announcement.toString());
            return announcement;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return error;
    }

    public String nameExtractor(String str){
        String[] arr = str.split(" ");
        String name = arr[arr.length-2].concat(" " + arr[arr.length-1]);
        return name;
    }
}
