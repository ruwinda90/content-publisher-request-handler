package com.example.contentpub.reqhandler.domain.service.auth.impl;

import com.example.contentpub.reqhandler.domain.dto.TokenData;
import com.example.contentpub.reqhandler.domain.exception.DomainException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.contentpub.reqhandler.domain.constants.AuthConstants.AUTHORITIES_KEY;
import static com.example.contentpub.reqhandler.domain.constants.AuthConstants.USER_ID;
import static com.example.contentpub.reqhandler.domain.constants.StatusCodes.REFRESH_TOKEN_EXPIRED;
import static com.example.contentpub.reqhandler.domain.constants.StatusCodes.REFRESH_TOKEN_INVALID;

@Component
public class TokenUtilService {

    private final Long accessTokenValidity;
    private final String accessTokenSigningKey;
    private final Long refreshTokenValidity;
    private final String refreshTokenSigningKey;

    public TokenUtilService(@Value("${token.access.validity-period}") Long accessTokenValidity,
                            @Value("${token.access.secret}") String accessTokenSigningKey,
                            @Value("${token.refresh.validity-period}") Long refreshTokenValidity,
                            @Value("${token.refresh.secret}") String refreshTokenSigningKey) {
        this.accessTokenValidity = accessTokenValidity;
        this.accessTokenSigningKey = accessTokenSigningKey;
        this.refreshTokenValidity = refreshTokenValidity;
        this.refreshTokenSigningKey = refreshTokenSigningKey;
    }

    public String getUsernameFromAccessToken(String token) {
        return getDataFromToken(token, accessTokenSigningKey, Claims.SUBJECT, String.class);
    }

    public Integer getUserIdFromAccessToken(String token) {
        return getDataFromToken(token, accessTokenSigningKey, USER_ID, Integer.class);
    }

    public TokenData generateAccessToken(String userName, Integer userId,
                                         Collection<? extends GrantedAuthority> roles) {

        String authorities = roles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .setSubject(userName)
                .claim(AUTHORITIES_KEY, authorities)
                .claim(USER_ID, userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity * 1000))
                .signWith(SignatureAlgorithm.HS256, accessTokenSigningKey)
                .compact();

        return new TokenData(userId, accessToken);
    }

    public boolean validateAccessToken(String token, UserDetails userDetails) { // todo - can throw runtime exp.
        // todo - handle them properly

        final String username = getUsernameFromAccessToken(token);
        final Date expiration = getDataFromToken(token, accessTokenSigningKey, Claims.EXPIRATION, Date.class);
        boolean isExpired = expiration.before(new Date());

        return (username.equals(userDetails.getUsername()) && !isExpired);
    }

    public TokenData generateRefreshToken(String userName, Integer userId) {

        String refreshToken = Jwts.builder()
                .setSubject(userName)
                .claim(USER_ID, userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity * 1000))
                .signWith(SignatureAlgorithm.HS256, refreshTokenSigningKey)
                .compact();

        return new TokenData(userId, refreshToken);
    }

    public String validateRefreshToken(String token) throws DomainException {

        String userName;
        try {
            userName = getDataFromToken(token, refreshTokenSigningKey, Claims.SUBJECT, String.class);
        } catch (MalformedJwtException | SignatureException ex) {
            throw new DomainException(REFRESH_TOKEN_INVALID);
        } catch (ExpiredJwtException ex) {
            throw new DomainException(REFRESH_TOKEN_EXPIRED);
        }

        return userName;
    }

    private <T> T getDataFromToken(String token, String secretKey, String claimName, Class<T> claimClz) {

        return getClaimFromToken(token, secretKey, claims -> claims.get(claimName, claimClz));
    }

    private <T> T getClaimFromToken(String token, String secretKey, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token, secretKey);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token, String secretKey) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

}