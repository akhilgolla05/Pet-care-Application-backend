package com.learnboot.universalpetcare.controllers;

import com.learnboot.universalpetcare.dto.UserDto;
import com.learnboot.universalpetcare.exceptions.ResourceNotFoundException;
import com.learnboot.universalpetcare.response.ApiResponse;
import com.learnboot.universalpetcare.services.veterinarians.IVeterinarianService;
import com.learnboot.universalpetcare.utils.FeedBackMessage;
import com.learnboot.universalpetcare.utils.UrlMapping;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @GetMapping(UrlMapping.SEARCH_VETERINARIAN_FOR_APPOINTMENT)
    public ResponseEntity<ApiResponse> searchVeterinariansForAppointment(@RequestParam(required = false) LocalDate date,
                                                                         @RequestParam(required = false) LocalTime time,
                                                                         @RequestParam String specialization){
       try{
           List<UserDto> availableVeterinarians = veterinarianService.findAvailableVetForAppointment(specialization,date,time);
           if(availableVeterinarians.isEmpty()){
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Veterinarians Available for the Requested Date and Time",null));
           }
           return ResponseEntity.ok(new ApiResponse(FeedBackMessage.FOUND, availableVeterinarians));
       }catch(ResourceNotFoundException ex){
           return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body(new ApiResponse(ex.getMessage(),null));
       }catch (Exception ex){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(new ApiResponse("Something Went Wrong!",null));
       }
    }
}
