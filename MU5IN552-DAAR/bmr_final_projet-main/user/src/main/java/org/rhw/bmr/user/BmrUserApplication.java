package org.rhw.bmr.user;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication


@MapperScan("org.rhw.bmr.user.dao.mapper")
public class BmrUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(BmrUserApplication.class, args);
    }
}
