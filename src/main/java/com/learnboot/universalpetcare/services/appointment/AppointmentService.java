package com.learnboot.universalpetcare.services.appointment;

import com.learnboot.universalpetcare.dto.AppointmentDto;
import com.learnboot.universalpetcare.dto.EntityConverter;
import com.learnboot.universalpetcare.dto.PetDto;
import com.learnboot.universalpetcare.enums.AppointmentStatus;
import com.learnboot.universalpetcare.exceptions.ResourceNotFoundException;
import com.learnboot.universalpetcare.models.Appointment;
import com.learnboot.universalpetcare.models.Pet;
import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.repository.AppointmentRepository;
import com.learnboot.universalpetcare.repository.UserRepository;
import com.learnboot.universalpetcare.request.AppointmentUpdateRequest;
import com.learnboot.universalpetcare.request.BookAppointmentRequest;
import com.learnboot.universalpetcare.services.pet.IPetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService implements IAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final IPetService petService;
    private final EntityConverter<Appointment,AppointmentDto> entityConverter;
    private final EntityConverter<Pet, PetDto> petEntityConverter;
    private final ModelMapper modelMapper;

//    @Override
//    public Appointment createAppointment(Appointment appointment,
//                                         Long senderId, Long recipientId) {
//        Optional<User> sender = userRepository.findById(senderId);
//        Optional<User> recipient = userRepository.findById(recipientId);
//        if (sender.isPresent() && recipient.isPresent()) {
//            appointment.addPatient(sender.get());
//            appointment.addVeterinarian(recipient.get());
//            appointment.setAppointmentNo();
//            appointment.setStatus(AppointmentStatus.WAITING_FOR_APPROVAL);
//            return appointmentRepository.save(appointment);
//        }
//        throw new ResourceNotFoundException("sender or recipient not found");
//    }

    @Transactional
    @Override
    public Appointment createAppointment(BookAppointmentRequest request,
                                         Long senderId, Long recipientId) {
        log.info("AppointmentService :: createAppointment");
        Optional<User> sender = userRepository.findById(senderId);
        log.info("Sender Found {}", sender.isPresent());
        Optional<User> recipient = userRepository.findById(recipientId);
        log.info("recipient Found {}", recipient.isPresent());

        if (sender.isPresent() && recipient.isPresent()) {

            Appointment appointment = request.getAppointment();
            List<Pet> pets = request.getPets();
            pets.forEach(pet->pet.setAppointment(appointment));
            List<Pet> savedPets = petService.savePetForAppointment(pets);
            appointment.setPets(savedPets);

            appointment.addPatient(sender.get());
            appointment.addVeterinarian(recipient.get());
            appointment.setAppointmentNo();
            appointment.setStatus(AppointmentStatus.WAITING_FOR_APPROVAL);
            return appointmentRepository.save(appointment);
        }
        throw new ResourceNotFoundException("sender or recipient not found");
    }

    @Override
    public List<Appointment> getAllAppoints() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment updateAppointment(Long appointmentId, AppointmentUpdateRequest request) {
        Appointment existingAppointment = getAppointmentById(appointmentId);
        if(!Objects.equals(existingAppointment.getStatus(), AppointmentStatus.WAITING_FOR_APPROVAL)) {
            throw new IllegalStateException("Sorry, This Appointment can no longer be updated");
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        existingAppointment.setDate(LocalDate.parse(request.getAppointmentDate(), dateFormatter));
        existingAppointment.setTime(LocalTime.parse(request.getAppointmentTime(), timeFormatter));
        //existingAppointment.setDate(request.getAppointmentDate());
        //existingAppointment.setTime(request.getAppointmentTime());
        existingAppointment.setReason(request.getReason());
        return appointmentRepository.save(existingAppointment);
    }

    @Override
    public void deleteAppointment(Long appointmentId) {
        appointmentRepository.findById(appointmentId)
                .ifPresentOrElse(appointmentRepository::delete, () -> {
                    throw new ResourceNotFoundException("appointment not found");
                });
    }

    @Override
    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("appointment not found"));
    }

    @Override
    public Appointment getAppointmentByAppointmentNo(String appointmentNo) {
        return appointmentRepository.findByAppointmentNo(appointmentNo);
    }

    public List<AppointmentDto> getAllUserAppointments(long userId) {
        List<Appointment> appointments = appointmentRepository.findAllByUserId(userId);
        return appointments.stream()
                //.map(appointment -> modelMapper.map(appointment, AppointmentDto.class))
                .map(appointment -> {
                    AppointmentDto appointmentDto = entityConverter.convertEntityToDto(appointment, AppointmentDto.class);
                    List<PetDto> petDtos = appointment.getPets().stream()
                            .map(pet->petEntityConverter.convertEntityToDto(pet, PetDto.class)).toList();
                    appointmentDto.setPets(petDtos);
                    return appointmentDto;
                }).toList();
    }

    @Override
    public Appointment cancelAppointment(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .filter(appointment -> appointment.getStatus().equals(AppointmentStatus.WAITING_FOR_APPROVAL))
                .map(appointment -> {appointment.setStatus(AppointmentStatus.CANCELLED);
                return appointmentRepository.save(appointment);}
                ).orElseThrow(()-> new IllegalStateException("Error Cancelling Appointment"));
    }

    @Override
    public Appointment approveAppointment(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .filter(appointment -> appointment.getStatus().equals(AppointmentStatus.WAITING_FOR_APPROVAL))
                .map(appointment -> {appointment.setStatus(AppointmentStatus.APPROVED);
                    return appointmentRepository.save(appointment);}
                ).orElseThrow(()-> new IllegalStateException("Operation Not Allowed"));
    }

    @Override
    public Appointment declineAppointment(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .map(appointment -> {appointment.setStatus(AppointmentStatus.NOT_APPROVED);
                    return appointmentRepository.save(appointment);}
                ).orElseThrow(()-> new ResourceNotFoundException("Operation Not Allowed"));
    }

    @Override
    public long countAppointments(){
        return appointmentRepository.count();
    }

    @Override
    public List<Map<String,Object>> getAppointmentsSummary(){
        return getAllAppoints()
                .stream()
                .collect(Collectors.groupingBy(Appointment::getStatus,Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry->entry.getValue()>0)
                .map(entry->createStatusSummaryMap(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private Map<String,Object> createStatusSummaryMap(AppointmentStatus status, long value){
        Map<String,Object> summary = new HashMap<>();
        summary.put("name",formatAppointmentStatus(status));
        summary.put("value",value);
        return summary;
    }

    private String formatAppointmentStatus(AppointmentStatus appointmentStatus) {
        return appointmentStatus.toString().replace("_","-").toLowerCase();
    }
}
