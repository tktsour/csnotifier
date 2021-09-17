package com.tktsour.csnotifier.service;

import com.tktsour.csnotifier.repository.AnnouncementRepository;
import com.tktsour.csnotifier.util.StringConstants;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class IdProvider {

    AnnouncementRepository announcementRepository;

    @Autowired
    IdProvider(AnnouncementRepository announcementRepository){
        this.announcementRepository = announcementRepository;
    }

    private Long fetchId(){
        try {
            log.info("Attempting connection with %s fetchId()",StringConstants.ANNOUNCEMENTS_URL);
            Document document = Jsoup.connect(StringConstants.ANNOUNCEMENTS_URL).get();
            log.info("Connection established successfully");
            log.info("Parsing necessary html elements");
            Element element = document.getElementsByAttributeValue("data-ri","0").get(0);
            String url = element.select("a").attr("href");
            return idExtractor(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public Queue<Long> produceQueue(){
        log.info("Inside of produceQueue()");
        Queue<Long> queue = new ArrayDeque<>();
        Long id = fetchId();
        log.info("Attempting database connection..");
        Optional<Long> maxId = announcementRepository.getMaxId();
        log.info("Database query executed successfully.");


        // TODO: 17-Sep-21 Improve this code
        if(maxId.isPresent()) {
            if(maxId.get().equals(id)){
                return queue;
            }
            long i = maxId.get() + 1;
            while(id>=i){
                queue.add(i++);
            }
        } else {
            queue.add(id);
        }

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
