package com.example.demo.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class User {
    
 
    private final int id;
    
    private final String mail;
    
    private final String password;
    
    private final String roles;
    
    private final LocalDateTime created;
    
    private final LocalDateTime lastLogined;
    private final Boolean enabled;

}
