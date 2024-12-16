package com.learnboot.universalpetcare.controllers;

import com.learnboot.universalpetcare.dto.PatientDto;
import com.learnboot.universalpetcare.models.Patient;
import com.learnboot.universalpetcare.response.ApiResponse;
import com.learnboot.universalpetcare.services.PatientService;
import com.learnboot.universalpetcare.utils.FeedBackMessage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class PatientController {

    private final PatientService patientService;
    private final ModelMapper modelMapper;

    @GetMapping("/get-all-patients")
    public ResponseEntity<ApiResponse> getAllPatients(){
        List<Patient> patientList = patientService.findAllPatients();
        List<PatientDto> patientDtos = patientList.stream()
                .map(pat->modelMapper.map(pat, PatientDto.class))
                .toList();
        return ResponseEntity.ok(new ApiResponse(FeedBackMessage.FOUND,patientDtos));
    }

}
