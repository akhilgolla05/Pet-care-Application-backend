package com.learnboot.universalpetcare.services.password;

import com.learnboot.universalpetcare.exceptions.ResourceNotFoundException;
import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.repository.UserRepository;
import com.learnboot.universalpetcare.request.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChangePasswordService implements IChangePasswordService {

    private final UserRepository userRepository;

    @Override
    public void changePassword(long userId, ChangePasswordRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
        if(Objects.equals(request.getCurrentPassword(),"") ||
                Objects.equals(request.getNewPassword(),"")){
            throw new IllegalArgumentException("All Fields are Required");
        }
        if(!Objects.equals(request.getCurrentPassword(),user.getPassword())){
            throw new IllegalArgumentException("Current Password Not Matched");
        }
        if(!Objects.equals(request.getNewPassword(),request.getConfirmNewPassword())){
            throw new IllegalArgumentException("New Password and Confirm New Password Not Matched");
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

}
