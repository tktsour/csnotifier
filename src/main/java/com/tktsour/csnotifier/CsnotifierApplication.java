package com.tktsour.csnotifier;

import com.tktsour.csnotifier.runnable.CsnotifierRunnable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class CsnotifierApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CsnotifierApplication.class, args);
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(context.getBean(CsnotifierRunnable.class),
                0,10, TimeUnit.MINUTES);
    }

}
