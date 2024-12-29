package com.learnboot.universalpetcare.services.token;

import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.models.VerificationToken;

import java.util.Optional;

public interface IVerificationTokenService {

    String validateToken(String token);
    void saveVerificationTokenForUser(String token, User user);
    VerificationToken generateNewVerificationToken(String oldToken);
    Optional<VerificationToken> findByVerificationToken(String token);
    void deleteVerificationToken(long tokenId);
    boolean isTokenExpired(String token);

}
