package com.learnboot.universalpetcare.services.veterinarians;

import com.learnboot.universalpetcare.dto.EntityConverter;
import com.learnboot.universalpetcare.dto.UserDto;
import com.learnboot.universalpetcare.exceptions.ResourceNotFoundException;
import com.learnboot.universalpetcare.models.Appointment;
import com.learnboot.universalpetcare.models.Veterinarian;
import com.learnboot.universalpetcare.repository.AppointmentRepository;
import com.learnboot.universalpetcare.repository.ReviewRepository;
import com.learnboot.universalpetcare.repository.UserRepository;
import com.learnboot.universalpetcare.repository.VeterinarianRepository;
import com.learnboot.universalpetcare.services.photo.PhotoService;
import com.learnboot.universalpetcare.services.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VeterinarianService implements IVeterinarianService {

    private final VeterinarianRepository veterinarianRepository;
    private final EntityConverter<Veterinarian,UserDto> entityConverter;
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final PhotoService photoService;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public List<UserDto> getAllVeterinariansWithDetails(){
        List<Veterinarian> veterinarians = userRepository.findAllByUserType("VET");
        return veterinarians.stream()
                .map(this::mapVeterinarianToUserDto)
                .toList();
    }

    private UserDto mapVeterinarianToUserDto(Veterinarian veterinarian) {
        UserDto userDto = entityConverter.convertEntityToDto(veterinarian, UserDto.class);
        double averageRating = reviewService.getAverageRatingForVeterinarian(veterinarian.getId());
        Long totalReviewers = reviewRepository.countByVeterinarianId(veterinarian.getId());
        userDto.setAverageRating(averageRating);
        userDto.setTotalReviewers(totalReviewers);
        if(veterinarian.getPhoto() != null) {
            try{
                byte[] photoBytes = photoService.getImageData(veterinarian.getPhoto().getId());
                userDto.setPhoto(photoBytes);
            }catch (SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return userDto;
    }


    @Override
    public List<UserDto> findAvailableVetForAppointment(String specialization, LocalDate date, LocalTime time){
        List<Veterinarian> filteredVets = getAvailableVeterinarians(specialization, date, time);
        return filteredVets.stream()
                .map(this::mapVeterinarianToUserDto)
                .toList();
    }


    @Override
    public List<Veterinarian> getAllVeterinariansBySpecialization(String specialization){
        if(!veterinarianRepository.existsBySpecialization(specialization)){
            throw new ResourceNotFoundException("No Veterinarian with specialization " + specialization + " found");
        }
        return veterinarianRepository.findBySpecialization(specialization);
    }

    private List<Veterinarian> getAvailableVeterinarians(String specialization, LocalDate date, LocalTime time){
        List<Veterinarian> veterinarians = getAllVeterinariansBySpecialization(specialization);
        return veterinarians
                .stream()
                .filter(vet->isVetAvailable(vet,date,time))
                .toList();
    }


    private boolean isVetAvailable(Veterinarian veterinarian, LocalDate requestedDate, LocalTime requestedTime) {
        if(requestedTime!=null && requestedDate!=null) {
            LocalTime requestedEndTime = requestedTime.plusHours(2);
            return appointmentRepository.findByVeterinarianAndDate(veterinarian,requestedDate)
                    .stream()
                    .noneMatch(existingAppointment->doesAppointmentOverlap(existingAppointment,requestedTime,requestedEndTime));
        }
        return true;
    }


    private boolean doesAppointmentOverlap(Appointment existingAppointment, LocalTime requestedStartTime,
                                           LocalTime requestedEndTime) {
        LocalTime existingStartTime = existingAppointment.getTime();
        LocalTime existingEndTime = existingStartTime.plusHours(2);
        LocalTime unAvailableStartTime = existingStartTime.minusHours(1);
        LocalTime unAvailableEndTime = existingEndTime.plusMinutes(110);

        return !requestedStartTime.isBefore(unAvailableStartTime) && !requestedEndTime.isAfter(unAvailableEndTime);
    }

    @Override
    public List<String> getAllVetSpecializations(){
        return veterinarianRepository.findAllVetSpecialization();
    }
}
