package kr.co.wasp.api.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.co.wasp.api.user.domain.SimpleUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Key;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class JwtSimpleUserService {
    private final Key key;
    private final Long accessTokenExpiretime;

    public JwtSimpleUserService(@Value("${jwt.secret}") String key, @Value("${jwt.expiration_time}") Long accessTokenExpiretime) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
        this.accessTokenExpiretime = accessTokenExpiretime;
    }

    public String createAccessToken(SimpleUser simpleUser) {
        return createToken(simpleUser, accessTokenExpiretime);
    }

    public Boolean validate(String token) {
        try {
            return this.parseClaims(token).isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    private String createToken(SimpleUser simpleUser, Long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("id", simpleUser.getId());
        claims.put("userId", simpleUser.getUserId());
        claims.put("userName", simpleUser.getUserName());
        claims.put("userRole", simpleUser.getUserRole());
//        claims.put("privateKey", simpleUser.getPrivateKey());
        claims.put("email", simpleUser.getEmail());
//        claims.put("ipv4", simpleUser.getIpv4());
//        claims.put("guid", simpleUser.getGuid());

        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime expireDateTime = currentDateTime.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(currentDateTime.toInstant()))
                .setExpiration(Date.from(expireDateTime.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Optional<String> parseToken(String key, String token) {
        Optional<Claims> claims = parseClaims(token);
        if (claims.isPresent() && !claims.get().isEmpty()) {
            Object value = claims.get().get(key);
            if (!ObjectUtils.isEmpty(value)) {
                return Optional.of(String.valueOf(value));
            }
        }
        return Optional.empty();
    }

    private Optional<Claims> parseClaims(String token) {
        try {
            return Optional.of(Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody());
        } catch (ExpiredJwtException | MalformedJwtException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }
}
