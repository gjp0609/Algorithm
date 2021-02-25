package com.onysakura.algorithm.spring.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class BaseController {

    String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null) {
            if (principal instanceof UserDetails) {
                UserDetails currentUser = ((UserDetails) principal);
                return currentUser.getUsername();
            } else {
                return String.valueOf(principal);
            }
        }
        return null;
    }
}
