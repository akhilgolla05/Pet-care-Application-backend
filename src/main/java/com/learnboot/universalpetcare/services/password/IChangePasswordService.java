package com.learnboot.universalpetcare.services.password;

import com.learnboot.universalpetcare.request.ChangePasswordRequest;

public interface IChangePasswordService {
    void changePassword(long userId, ChangePasswordRequest request);
}
