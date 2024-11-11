package com.learnboot.universalpetcare.controllers;

import com.learnboot.universalpetcare.dto.EntityConverter;
import com.learnboot.universalpetcare.dto.UserDto;
import com.learnboot.universalpetcare.exceptions.ResourceNotFoundException;
import com.learnboot.universalpetcare.exceptions.UserAlreadyExistsException;
import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.request.RegistrationRequest;
import com.learnboot.universalpetcare.request.UserUpdateRequest;
import com.learnboot.universalpetcare.response.ApiResponse;
import com.learnboot.universalpetcare.services.user.UserService;
import com.learnboot.universalpetcare.utils.FeedBackMessage;
import com.learnboot.universalpetcare.utils.UrlMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173"})
@RestController
@RequestMapping(UrlMapping.USERS)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EntityConverter<User, UserDto> entityConverter;

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
    public ResponseEntity<ApiResponse> updateUser(@PathVariable("id") long userId,
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


}
