package com.japan.compass.annotation.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.japan.compass.annotation.domain.entity.User;
import com.japan.compass.annotation.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        User user = userRepository.findByMail(mail);

        if (user == null) {
            log.info("user not found." + mail);
            throw new UsernameNotFoundException("username not found.");
        }

        return user;
    }
}
