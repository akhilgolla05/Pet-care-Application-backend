package com.learnboot.universalpetcare.controllers;

import com.learnboot.universalpetcare.exceptions.ResourceNotFoundException;
import com.learnboot.universalpetcare.models.Appointment;
import com.learnboot.universalpetcare.request.AppointmentUpdateRequest;
import com.learnboot.universalpetcare.request.BookAppointmentRequest;
import com.learnboot.universalpetcare.response.ApiResponse;
import com.learnboot.universalpetcare.services.appointment.AppointmentService;
import com.learnboot.universalpetcare.utils.FeedBackMessage;
import com.learnboot.universalpetcare.utils.UrlMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.ResponseEntity.ok;
@CrossOrigin(origins = {"http://localhost:5173"})
@RestController
@RequestMapping(UrlMapping.APPOINTMENTS)
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping(UrlMapping.ALL_APPOINTMENTS)
    public ResponseEntity<ApiResponse> getAllAppointments() {
        try{
            List<Appointment> appointmentList = appointmentService.getAllAppoints();
            return ResponseEntity.status(FOUND)
                    .body(new ApiResponse(FeedBackMessage.FOUND, appointmentList));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping(UrlMapping.BOOK_APPOINTMENT)
    public ResponseEntity<ApiResponse> bookAppointment(@RequestBody BookAppointmentRequest request,
                                                        @RequestParam long senderId,
                                                       @RequestParam long recipientId) {
        try{
            log.info("AppointmentController :: bookAppointment");
           Appointment appointment1 =  appointmentService.createAppointment(request, senderId, recipientId);
           return ok(new ApiResponse(FeedBackMessage.SUCCESS, appointment1));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMapping.APPOINTMENT_BY_ID)
    public ResponseEntity<ApiResponse> getAppointmentById(@PathVariable long id) {
        try{
            Appointment appointment = appointmentService.getAppointmentById(id);
            return ResponseEntity.status(FOUND)
                    .body(new ApiResponse(FeedBackMessage.FOUND, appointment));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMapping.APPOINTMENTS_BY_APPOINTMENT_NO)
    public ResponseEntity<ApiResponse> getAppointmentByAppointmentNo(@PathVariable String appointmentNo) {
        try{
            Appointment appointment = appointmentService.getAppointmentByAppointmentNo(appointmentNo);
            return ResponseEntity.status(FOUND)
                    .body(new ApiResponse(FeedBackMessage.FOUND, appointment));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping(UrlMapping.DELETE_APPOINTMENT_BY_ID)
    public ResponseEntity<ApiResponse> deleteAppointmentById(@PathVariable long id) {
        try{
            appointmentService.deleteAppointment(id);
            return ResponseEntity.status(FOUND)
                    .body(new ApiResponse(FeedBackMessage.DELETE_SUCCESS, null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping(UrlMapping.UPDATE_APPOINTMENT_BY_ID)
    public ResponseEntity<ApiResponse> updateAppointment(@PathVariable long id,
                                                         @RequestBody AppointmentUpdateRequest request) {
        try{
            Appointment appointment = appointmentService.updateAppointment(id,request);
            return ResponseEntity.status(FOUND)
                    .body(new ApiResponse(FeedBackMessage.UPDATED, appointment));
        }catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping(UrlMapping.CANCEL_APPOINTMENT)
    public ResponseEntity<ApiResponse> cancelAppointment(@PathVariable long id){
        try {
            Appointment appointment = appointmentService.cancelAppointment(id);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.UPDATED, appointment));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping(UrlMapping.APPROVE_APPOINTMENT)
    public ResponseEntity<ApiResponse> approveAppointment(@PathVariable long id){
        try {
            Appointment appointment = appointmentService.approveAppointment(id);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.UPDATED, appointment));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping(UrlMapping.DECLINE_APPOINTMENT)
    public ResponseEntity<ApiResponse> declineAppointment(@PathVariable long id){
        try {
            Appointment appointment = appointmentService.declineAppointment(id);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.UPDATED, appointment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMapping.COUNT_ALL_APPOINTMENTS)
    public long countAppointments(){
        return appointmentService.countAppointments();
    }

    @GetMapping(UrlMapping.GET_APPOINTMENTS_SUMMARY)
    public ResponseEntity<ApiResponse> getAppointmentsSummary(){
        try{
            List<Map<String,Object>> summary = appointmentService.getAppointmentsSummary();
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.FOUND, summary));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
