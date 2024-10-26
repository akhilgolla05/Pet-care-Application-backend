package com.learnboot.universalpetcare.controllers;

import com.learnboot.universalpetcare.dto.UserDto;
import com.learnboot.universalpetcare.response.ApiResponse;
import com.learnboot.universalpetcare.services.veterinarians.IVeterinarianService;
import com.learnboot.universalpetcare.utils.FeedBackMessage;
import com.learnboot.universalpetcare.utils.UrlMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173"})
@RestController
@RequiredArgsConstructor
@RequestMapping(UrlMapping.VETERINARIANS)
public class VeterinarianController {

    private final IVeterinarianService veterinarianService;

    @GetMapping(UrlMapping.GET_ALL_VETS)
    public ResponseEntity<ApiResponse> getAllVeterinarians() {
        try{
            List<UserDto> veterinarians = veterinarianService.getAllVeterinariansWithDetails();
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.FOUND, veterinarians));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ex.getMessage(),null));
        }
    }
}
