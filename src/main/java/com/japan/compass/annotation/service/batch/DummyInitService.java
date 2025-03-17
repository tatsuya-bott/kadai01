package com.japan.compass.annotation.service.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("!cron")
@Slf4j
@Service
public class DummyInitService implements ApplicationInitializer{

    @Override
    public void execStartup(String[] args) {
        log.info("dummy init service complete.");
    }
}
