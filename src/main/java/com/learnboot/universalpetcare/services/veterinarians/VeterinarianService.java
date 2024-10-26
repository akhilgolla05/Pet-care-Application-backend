package com.learnboot.universalpetcare.services.veterinarians;

import com.learnboot.universalpetcare.dto.EntityConverter;
import com.learnboot.universalpetcare.dto.UserDto;
import com.learnboot.universalpetcare.models.Veterinarian;
import com.learnboot.universalpetcare.repository.ReviewRepository;
import com.learnboot.universalpetcare.repository.UserRepository;
import com.learnboot.universalpetcare.repository.VeterinarianRepository;
import com.learnboot.universalpetcare.services.photo.PhotoService;
import com.learnboot.universalpetcare.services.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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
}
