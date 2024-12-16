package com.learnboot.universalpetcare.controllers;

import com.learnboot.universalpetcare.dto.EntityConverter;
import com.learnboot.universalpetcare.dto.UserDto;
import com.learnboot.universalpetcare.exceptions.ResourceNotFoundException;
import com.learnboot.universalpetcare.exceptions.UserAlreadyExistsException;
import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.request.ChangePasswordRequest;
import com.learnboot.universalpetcare.request.RegistrationRequest;
import com.learnboot.universalpetcare.request.UserUpdateRequest;
import com.learnboot.universalpetcare.response.ApiResponse;
import com.learnboot.universalpetcare.services.password.IChangePasswordService;
import com.learnboot.universalpetcare.services.user.UserService;
import com.learnboot.universalpetcare.utils.FeedBackMessage;
import com.learnboot.universalpetcare.utils.UrlMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:5173"})
@RestController
@RequestMapping(UrlMapping.USERS)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EntityConverter<User, UserDto> entityConverter;
    private final IChangePasswordService changePasswordService;

    @PostMapping(UrlMapping.REGISTER_USER)
    public ResponseEntity<ApiResponse> register(@RequestBody RegistrationRequest request) {
        try {
            User user =  userService.addUser(request);
           UserDto userDto =  entityConverter.convertEntityToDto(user, UserDto.class);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.SUCCESS,userDto));
        } catch (UserAlreadyExistsException e) {
           return ResponseEntity.status(HttpStatus.CONFLICT)
                   .body(new ApiResponse(e.getMessage(),null));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping(UrlMapping.UPDATE_USER)
    public ResponseEntity<ApiResponse> updateUser(@PathVariable() long userId,
                                                  @RequestBody UserUpdateRequest request) {
        try{
            User user = userService.updateUser(userId, request);
            UserDto userDto = entityConverter.convertEntityToDto(user, UserDto.class);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.UPDATED,userDto));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping(UrlMapping.GET_USER_BY_ID)
    public ResponseEntity<ApiResponse> getUser(@PathVariable long userId){
        try{
           // User user = userService.findById(userId);
            UserDto userDto = userService.getUserWithDetails(userId);
            //UserDto userDto = entityConverter.convertEntityToDto(user, UserDto.class);
            return ResponseEntity
                    .ok(new ApiResponse(FeedBackMessage.FOUND,userDto));
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(ex.getMessage(),null));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping(UrlMapping.DELETE_USER_BY_ID)
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable long userId){
        try{
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.DELETE_SUCCESS,null));
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(ex.getMessage(),null));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }


    @GetMapping(UrlMapping.GET_ALL_USERS)
    public ResponseEntity<ApiResponse> getAllUsers(){
       List<UserDto> userDtoList =  userService.getAllUsers();
       return ResponseEntity.status(HttpStatus.FOUND)
               .body(new ApiResponse(FeedBackMessage.FOUND,userDtoList));
    }


    @PutMapping(UrlMapping.CHANGE_PASSWORD)
    public ResponseEntity<ApiResponse> changePassword(@PathVariable long userId,
                                                      @RequestBody ChangePasswordRequest request){
        try{
            changePasswordService.changePassword(userId,request);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.UPDATED,null));
        }catch(IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(new ApiResponse(ex.getMessage(),null));
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(ex.getMessage(),null));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping(UrlMapping.COUNT_ALL_VETS)
    public long countVeterinarians(){
        return userService.countVeterinarians();
    }
    @GetMapping(UrlMapping.COUNT_ALL_PATIENTS)
    public long countPatients(){
        return userService.countPatients();
    }

    @GetMapping(UrlMapping.COUNT_ALL_USERS)
    public long countUsers(){
        return userService.countAllUsers();
    }

    @GetMapping(UrlMapping.AGGREGATE_USERS)
    public ResponseEntity<ApiResponse> aggregateUsersByMonthAndType(){
        try{
            Map<String, Map<String,Long>> aggregate =  userService.aggregateUsersByMonthAndType();
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.FOUND,aggregate));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(new ApiResponse(ex.getMessage(),null));
        }
    }

    @GetMapping(UrlMapping.AGGREGATE_USERS_BY_STATUS)
    public ResponseEntity<ApiResponse> aggregateUsersByAccountStatusAndType(){
        try{
            Map<String, Map<String,Long>> aggregate =  userService.aggregateUsersByEnabledStatusAndType();
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.FOUND,aggregate));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(new ApiResponse(ex.getMessage(),null));
        }
    }

    @PutMapping("account/{userId}/lock-user-account")
    public ResponseEntity<ApiResponse> lockUserAccount(@PathVariable long userId){
        userService.lockUserAccount(userId);
        return ResponseEntity.ok(new ApiResponse("User Account Locked Successfully", null));
    }

    @PutMapping("account/{userId}/unLock-user-account")
    public ResponseEntity<ApiResponse> unLockUserAccount(@PathVariable long userId){
        userService.unLockUserAccount(userId);
        return ResponseEntity.ok(new ApiResponse("User Account UnLocked Successfully", null));
    }




}
