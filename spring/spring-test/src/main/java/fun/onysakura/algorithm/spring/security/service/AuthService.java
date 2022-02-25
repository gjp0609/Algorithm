package fun.onysakura.algorithm.spring.security.service;

import fun.onysakura.algorithm.spring.security.model.UserModel;
import fun.onysakura.algorithm.spring.security.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserModel userModel = userService.getUserModel(username);
        List<GrantedAuthority> authorities = userService.getAuthorities(username);
        return jwtUtil.generateToken(userModel, authorities);
    }

    public String refresh(String oldToken) {
        Claims claims = jwtUtil.getClaimsFromToken(oldToken);
        String username = claims.getSubject();
        UserModel userModel = userService.getUserModel(username);
        if (jwtUtil.canTokenBeRefreshed(claims, userModel.getLastPasswordResetDate())) {
            List<GrantedAuthority> authorities = userService.getAuthorities(username);
            return jwtUtil.refreshToken(claims, authorities);
        }
        return null;
    }

}
