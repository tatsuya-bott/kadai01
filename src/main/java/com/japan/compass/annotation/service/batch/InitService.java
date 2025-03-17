package com.japan.compass.annotation.service.batch;

import com.japan.compass.annotation.domain.entity.Application;
import com.japan.compass.annotation.domain.entity.Role;
import com.japan.compass.annotation.infrastructure.ImageFileComponent;
import com.japan.compass.annotation.infrastructure.RecordFileComponent;
import com.japan.compass.annotation.repository.ApplicationRepository;
import com.japan.compass.annotation.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Profile("cron")
@Slf4j
@AllArgsConstructor
@Service
public class InitService implements ApplicationInitializer {

    private final Clock clock;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationRepository applicationRepository;
    private final ImageFileComponent imageFileComponent;
    private final RecordFileComponent recordFileComponent;

    public void execStartup(String[] args) {
        log.info("init service start.");
        try {
            imageFileComponent.init();
            recordFileComponent.init();

            Application application = applicationRepository.find();
            if (application == null) {
                applicationRepository.create(new Application("", LocalDateTime.now(clock)));
                log.info("created application record.");
            }

            if (args.length == 2) {
                String newUsername = args[0];
                String newPassword = args[1];
                userRepository.create(
                        newUsername,
                        passwordEncoder.encode(newPassword),
                        LocalDateTime.now(clock),
                        Role.ADMIN);
                log.info("created admin user.");
            }
        } catch (Exception e) {
            log.error("init service fail.");
            System.exit(1);
        }
        log.info("init service complete.");
    }
}
