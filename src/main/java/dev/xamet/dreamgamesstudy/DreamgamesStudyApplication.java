package dev.xamet.dreamgamesstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class  DreamgamesStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamgamesStudyApplication.class, args);
    }

}
