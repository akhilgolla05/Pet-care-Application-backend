package com.learnboot.universalpetcare.factory;

import com.learnboot.universalpetcare.models.Admin;
import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.repository.AdminRepository;
import com.learnboot.universalpetcare.request.RegistrationRequest;
import com.learnboot.universalpetcare.services.user.UserAttributesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminFactory {
    private final AdminRepository adminRepository;
    private final UserAttributesMapper userAttributesMapper;

    public User createAdmin(RegistrationRequest request) {
        Admin admin = new Admin();
        userAttributesMapper.setCommonAttributes(request, admin);
        return adminRepository.save(admin);
    }
}
