package com.learnboot.universalpetcare.factory;

import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface UserFactory{
    public User createUser(RegistrationRequest request);
}
