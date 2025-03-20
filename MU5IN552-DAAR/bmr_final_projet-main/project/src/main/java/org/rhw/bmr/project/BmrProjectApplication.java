package org.rhw.bmr.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("org.rhw.bmr.project.dao.mapper")
@EnableScheduling
public class BmrProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(BmrProjectApplication.class, args);
    }
}
