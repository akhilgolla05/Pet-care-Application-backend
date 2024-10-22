package com.learnboot.universalpetcare.services.user;

import com.learnboot.universalpetcare.dto.UserDto;
import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.request.RegistrationRequest;
import com.learnboot.universalpetcare.request.UserUpdateRequest;

import java.sql.SQLException;
import java.util.List;

public interface IUserService {

    User addUser(RegistrationRequest request);


    User updateUser(long userId, UserUpdateRequest request);

    User findById(long userId);

    void deleteUser(long userId);

    List<UserDto> getAllUsers();

    UserDto getUserWithDetails(long userId) throws SQLException;
}
