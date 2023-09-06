package com.example.king.service;

import com.example.king.DTO.MemberAuthDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
@Log4j2
public class TokenService {
    private final String SECRET_KEY = "tsIo#1sd320956$eAawuet9934r341ret#ytretyghpoiuabldksfdkwerwertwer";

    public String createToken(int expTimeMin, String id){
        if(expTimeMin <= 0) {
            throw new RuntimeException("WrongExpTimeOfToken");
        }

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        byte[] byteSecretKey = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key sigingKey = new SecretKeySpec(byteSecretKey, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setSubject(id)
                .signWith(sigingKey, signatureAlgorithm)
                .setExpiration(new Date(System.currentTimeMillis()+ (long)expTimeMin*60*1000))
                .compact();

    }

    public String getIdFromToken(String token) throws Exception{
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();

        log.info("##### getIdFromToken@TokenService => claims.getExpiraton, System.curentTimeMillis : ");
        log.info(claims.getExpiration());
        log.info(new Date(System.currentTimeMillis()).toString()) ;

        return claims.getSubject();
    }
}
