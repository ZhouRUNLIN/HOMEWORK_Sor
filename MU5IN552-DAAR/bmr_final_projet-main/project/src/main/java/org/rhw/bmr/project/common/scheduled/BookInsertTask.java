package org.rhw.bmr.project.common.scheduled;


import lombok.extern.slf4j.Slf4j;
import org.rhw.bmr.project.service.BookInsertionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookInsertTask implements SchedulingConfigurer {

    @Value("${scheduledTaskInsertBook.enabled}")
    private boolean enabled;

    @Value("${scheduledTaskInsertBook.interval}")
    private long interval;

    @Autowired
    private BookInsertionService bookInsertionService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (enabled) {
            taskRegistrar.addFixedRateTask(() -> {
                bookInsertionService.insertBook();
            }, interval);
        }
    }
}

