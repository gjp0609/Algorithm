package com.onysakura.algorithm.spring.security;

import com.onysakura.algorithm.spring.common.result.Result;
import com.onysakura.algorithm.spring.security.model.JwtAuthenticationRequest;
import com.onysakura.algorithm.spring.security.service.AuthService;
import com.onysakura.algorithm.spring.security.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<String> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {
        String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        return Result.ok(token);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public Result<String> refreshAndGetAuthenticationToken(HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(JwtUtil.HEADER_STRING);
        token = token.substring(JwtUtil.TOKEN_PREFIX.length());
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            return Result.fail();
        } else {
            return Result.ok(refreshedToken);
        }
    }
}
