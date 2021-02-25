package com.onysakura.algorithm.spring.security.model;

import lombok.Data;

@Data
public class JwtAuthenticationRequest {
    
    private String username;
    private String password;
}
