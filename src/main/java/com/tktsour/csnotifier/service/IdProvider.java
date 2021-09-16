package com.tktsour.csnotifier.service;

import com.tktsour.csnotifier.entity.Announcement;
import com.tktsour.csnotifier.repository.AnnouncementRepository;
import com.tktsour.csnotifier.util.StringConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IdProvider {

    AnnouncementRepository announcementRepository;

    @Autowired
    IdProvider(AnnouncementRepository announcementRepository){
        this.announcementRepository = announcementRepository;
    }

    private Long fetchId(){
        try {
            Document document = Jsoup.connect(StringConstants.ANNOUNCEMENTS_URL).get();
            Element element = document.getElementsByAttributeValue("data-ri","0").get(0);
            String url = element.select("a").attr("href");
            return idExtractor(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public Queue<Long> produceQueue(){
        Queue<Long> queue = new ArrayDeque<>();
        Long id = fetchId();
        Optional<Long> maxId = announcementRepository.getMaxId();
        if(maxId.isPresent()) {
            for (Long i = maxId.get(); i >= id; i--) {
                queue.add(i);
            }
        }
        queue.add(id);
        return queue;
    }

    private Long idExtractor(String str){
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(str);
        while(m.find()) {
            return Long.parseLong(m.group());
        }
        return 0l;
    }

}
