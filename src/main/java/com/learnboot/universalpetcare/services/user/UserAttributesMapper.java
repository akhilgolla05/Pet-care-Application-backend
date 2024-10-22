package com.learnboot.universalpetcare.services.user;

import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.request.RegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public class UserAttributesMapper {

    public void setCommonAttributes(RegistrationRequest source, User target){

        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setEmail(source.getEmail());
        target.setPhoneNumber(source.getPhoneNumber());
        target.setPassword(source.getPassword());
        target.setGender(source.getGender());
        target.setActive(source.isActive());
        target.setUserType(source.getUserType());

    }
}
