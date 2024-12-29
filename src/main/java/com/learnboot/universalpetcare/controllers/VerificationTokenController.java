package com.learnboot.universalpetcare.controllers;

import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.models.VerificationToken;
import com.learnboot.universalpetcare.repository.UserRepository;
import com.learnboot.universalpetcare.request.VerificationTokenRequest;
import com.learnboot.universalpetcare.response.ApiResponse;
import com.learnboot.universalpetcare.services.token.IVerificationTokenService;
import com.learnboot.universalpetcare.services.token.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/verification")
@RequiredArgsConstructor
public class VerificationTokenController {

    private final IVerificationTokenService verificationTokenService;
    private final UserRepository userRepository;

    @GetMapping("/validate-token")
    public ResponseEntity<ApiResponse> validateToken(String token) {
        String validatedToken = verificationTokenService.validateToken(token);
        ApiResponse result = switch (validatedToken){
            case "INVALID"-> new ApiResponse("INVALID", null);
            case "VERIFIED"-> new ApiResponse("VERIFIED", null);
            case "EXPIRED"-> new ApiResponse("EXPIRED", null);
            case "VALID"-> new ApiResponse("VALID", null);
            default -> new ApiResponse("ERROR", null);
        };
        return ResponseEntity.ok(result);
    }

    @GetMapping("/check-token-expiration")
    public ResponseEntity<ApiResponse> checkTokenExpiration(String token) {
            boolean isExpired = verificationTokenService.isTokenExpired(token);
            ApiResponse response;
            if (isExpired) {
                response = new ApiResponse("EXPIRED", null);
            }else{
                response = new ApiResponse("VALID", null);
            }
            return ResponseEntity.ok(response);
    }

    @PostMapping("/save-verification-token")
    public ResponseEntity<ApiResponse> saveVerificationTokenForUser(@RequestBody VerificationTokenRequest request) {
        User user = userRepository.findById(request.getUser().getId())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        verificationTokenService.saveVerificationTokenForUser(request.getToken(), user);
        return ResponseEntity.ok(new ApiResponse("SAVED", null));
    }

    @PutMapping("/generate-new-token")
    public ResponseEntity<ApiResponse> generateNewVerificationToken(@RequestParam String oldToken) {
        VerificationToken verificationToken = verificationTokenService.generateNewVerificationToken(oldToken);
        return ResponseEntity.ok(new ApiResponse("", verificationToken));
    }

    @DeleteMapping("/delete-token")
    public ResponseEntity<ApiResponse> deleteVerificationTokenForUser(@RequestParam long tokenId) {
        verificationTokenService.deleteVerificationToken(tokenId);
        return ResponseEntity.ok(new ApiResponse("User Token Deleted Successfully", null));
    }


}
