package com.learnboot.universalpetcare.factory;

import com.learnboot.universalpetcare.exceptions.UserAlreadyExistsException;
import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.repository.UserRepository;
import com.learnboot.universalpetcare.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleUserFactory implements UserFactory {

    private final UserRepository userRepository;
    private final AdminFactory adminFactory;
    private final PatientFactory patientFactory;
    private final VeterinarianFactory veterinarianFactory;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(RegistrationRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistsException(request.getEmail()+" Already Exists!");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));


        switch (request.getUserType()){

            case "VET"->{
                return veterinarianFactory.createVeterinarian(request);
            }
            case "ADMIN"->{
                return adminFactory.createAdmin(request);
            }
            case "PATIENT"->{
                return patientFactory.createPatient(request);
            }
            default ->{
                return null;
            }
        }

    }
}
