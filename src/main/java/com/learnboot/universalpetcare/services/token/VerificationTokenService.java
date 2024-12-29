package com.learnboot.universalpetcare.services.token;

import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.models.VerificationToken;
import com.learnboot.universalpetcare.repository.UserRepository;
import com.learnboot.universalpetcare.repository.VerificationTokenRepository;
import com.learnboot.universalpetcare.utils.SystemUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService implements IVerificationTokenService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public String validateToken(String token) {
        Optional<VerificationToken> verificationToken = findByVerificationToken(token);
        if (verificationToken.isEmpty()) {
            return "INVALID";
        }
        User user = verificationToken.get().getUser();
        if(user.isActive()){
            return "VERIFIED";
        }
        if(isTokenExpired(token)){
            return "EXPIRED";
        }
        user.setActive(true);
        userRepository.save(user);
        return "VALID";
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {

        var verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        Optional<VerificationToken> verificationToken = findByVerificationToken(oldToken);
        if (verificationToken.isPresent()) {
            verificationToken.get().setToken(UUID.randomUUID().toString());
            verificationToken.get().setExpirationDate(SystemUtil.getExpirationDate());
            return verificationTokenRepository.save(verificationToken.get());
        }
        throw new IllegalArgumentException("Invalid verification token");
    }

    @Override
    public Optional<VerificationToken> findByVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public void deleteVerificationToken(long tokenId) {
        verificationTokenRepository.deleteById(tokenId);
    }

    @Override
    public boolean isTokenExpired(String token) {
        Optional<VerificationToken> verificationToken = findByVerificationToken(token);
        if (verificationToken.isEmpty()) {
            return true;
        }
        var verToken = verificationToken.get();
        return verToken.getExpirationDate().getTime() <= Calendar.getInstance().getTime().getTime();
    }
}
