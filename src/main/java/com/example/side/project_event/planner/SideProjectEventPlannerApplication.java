package com.example.side.project_event.planner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SideProjectEventPlannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SideProjectEventPlannerApplication.class, args);
    }
}
