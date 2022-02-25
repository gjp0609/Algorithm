package fun.onysakura.algorithm.spring.security.utils;

import fun.onysakura.algorithm.spring.security.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    public static final long EXPIRATION_TIME = 5 * 24 * 60 * 60; // 5天
    public static final String TOKEN_PREFIX = "Bearer"; // Token前缀
    public static final String HEADER_STRING = "Authorization"; // 存放Token的Header Key

    @Value("custom.jwt.signing-key")
    private String signingKey;

    public Authentication getAuthentication(HttpServletRequest request) {
        String authHeader = request.getHeader(JwtUtil.HEADER_STRING);
        if (authHeader != null && authHeader.startsWith(JwtUtil.TOKEN_PREFIX)) {
            String authToken = authHeader.substring(JwtUtil.TOKEN_PREFIX.length()); // The part after "Bearer "
            Claims claims = getClaimsFromToken(authToken);
            String username = claims.getSubject();
            log.info("checking authentication " + username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = getUserDetails(claims);
                if (!isTokenExpired(claims)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    return authentication;
                }
            }
        }
        return null;
    }

    public UserDetails getUserDetails(Claims claims) {
        // return userService.loadUserByUsername(claims.getSubject());
        String username = claims.getSubject();
        String[] authorityArray = ((String) claims.get("authorities")).split(",");
        HashSet<GrantedAuthority> authorities = new HashSet<>();
        for (String authority : authorityArray) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        UserModel userModel = new UserModel();
        userModel.setUsername(username);
        userModel.setAuthorities(authorities);
        return userModel;
    }

    private Boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody();
    }


    public String generateToken(UserModel userModel, List<GrantedAuthority> authorities) {
        Claims claims = Jwts.claims().setSubject(userModel.getUsername());
        return doGenerateToken(claims, authorities);
    }

    private String doGenerateToken(Claims claims, List<GrantedAuthority> authorities) {
        claims.put("authorities", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
        claims.put("created", new Date());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("https://onysakura.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }

    public boolean canTokenBeRefreshed(Claims claims, Date lastPasswordResetDate) {
        Date created = new Date((Long) claims.get("created"));
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordResetDate) && !isTokenExpired(claims);
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String refreshToken(Claims claims, List<GrantedAuthority> authorities) {
        String refreshedToken;
        try {
            refreshedToken = doGenerateToken(claims, authorities);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }
}
