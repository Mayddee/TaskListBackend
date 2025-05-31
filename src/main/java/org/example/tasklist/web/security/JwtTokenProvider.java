package org.example.tasklist.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.tasklist.domain.exception.AccessDeniedException;
import org.example.tasklist.domain.user.Role;
import org.example.tasklist.domain.user.User;
import org.example.tasklist.service.UserService;
import org.example.tasklist.service.props.JwtProperties;
import org.example.tasklist.web.dto.auth.JwtResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private Key key;

    @PostConstruct //после конструктора
    public void init(){
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()); //нужны 3 библиотеки с jjwt

    }



    public String createAccessToken(Long userId, String username, Set<Role> roles) {
        Instant now = Instant.now();
        Instant validity = now.plusMillis(jwtProperties.getAccess());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("roles", resolveRoles(roles));

        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(validity))
                .signWith(key)
                .compact();
    }


    private List<String> resolveRoles(Set<Role> roles){
        return roles.stream().map(Enum::name).collect(Collectors.toList());
    }

    //для рефреш токена не нужно передавть роли тк он нам нужен просто апдетить пару токенов
    public String createRefreshToken(Long userId, String username) {
        Instant now = Instant.now();
        Instant validity = now.plusMillis(jwtProperties.getRefresh());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);

        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(validity))
                .signWith(key)
                .compact();
    }




    //для получения новую пару из 2 токенов нужен метод
    public JwtResponse refreshToken(String refreshToken) {
        JwtResponse jwtResponse = new JwtResponse();
        if(!validateToken(refreshToken)){
            throw new AccessDeniedException();
        }
        //получаем id из токена для создания новых
        Long userId = Long.valueOf(getId(refreshToken));
        User user = userService.getById(userId);
        jwtResponse.setId(userId);
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(createAccessToken(userId, user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(createRefreshToken(userId, user.getUsername()));
        return jwtResponse;

    }

    //проверяем токен на валидацию правильный ли он сравнивая дату сейчас с датой expiration time
    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts
                .parser() //for jjwt v 0.12.*
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return claims.getBody().getExpiration().after(new Date());
    }

    private String getId(String token){
        return Jwts
                .parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id")
                .toString();
    }
    private String getUsername(String token){
        return Jwts
                .parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject()
                .toString();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}

