package com.learnboot.universalpetcare.services.user;

import com.learnboot.universalpetcare.dto.AppointmentDto;
import com.learnboot.universalpetcare.dto.EntityConverter;
import com.learnboot.universalpetcare.dto.ReviewDto;
import com.learnboot.universalpetcare.dto.UserDto;
import com.learnboot.universalpetcare.exceptions.ResourceNotFoundException;
import com.learnboot.universalpetcare.factory.UserFactory;
import com.learnboot.universalpetcare.models.Review;
import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.repository.ReviewRepository;
import com.learnboot.universalpetcare.repository.UserRepository;
import com.learnboot.universalpetcare.request.RegistrationRequest;
import com.learnboot.universalpetcare.request.UserUpdateRequest;
import com.learnboot.universalpetcare.services.appointment.AppointmentService;
import com.learnboot.universalpetcare.services.photo.IPhotoService;
import com.learnboot.universalpetcare.services.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final EntityConverter<User,UserDto> entityConverter;
    private final AppointmentService appointmentService;
    private final IPhotoService photoService;
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    @Override
    public User addUser(RegistrationRequest request) {
       return userFactory.createUser(request);
    }

    @Override
    public User updateUser(long userId, UserUpdateRequest request) {
        User user = findById(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setSpecialization(request.getSpecialization());
        return userRepository.save(user);
    }

    @Override
    public User findById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete,
                        ()-> {
                    throw new ResourceNotFoundException("User Not Found");
                        });
    }

    @Override
    public List<UserDto> getAllUsers(){

        List<User> users = userRepository.findAll();
        return users.stream().map(user->entityConverter.convertEntityToDto(user,UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserWithDetails(long userId) throws SQLException {
        //get the user
        User user = findById(userId);
        //convert user to userDto
        UserDto userDto = entityConverter.convertEntityToDto(user,UserDto.class);

        //set all reviewers
        userDto.setTotalReviewers(reviewRepository.countByVeterinarianId(userId));
        //get the User Appointment
        setUserAppointments(userDto);
        //set user photo
        setUserPhoto(userDto,user);
        //user Reviews (has Users)
        setUserReviews(userDto,userId);
        return userDto;

    }
    private void setUserAppointments(UserDto userDto) {
        List<AppointmentDto> appointments = appointmentService.getAllUserAppointments(userDto.getId());
        userDto.setAppointments(appointments);
    }
    private void setUserPhoto(UserDto userDto, User user) throws SQLException {
        if(user.getPhoto() != null) {
            userDto.setPhotoId(user.getPhoto().getId());
            userDto.setPhoto(photoService.getImageData(user.getPhoto().getId()));
        }
    }

    //@SneakyThrows
    private void setUserReviews(UserDto userDto, long userId) {
        Page<Review> reviewPage = reviewService.findAllReviewsByUserId(userId,0,Integer.MAX_VALUE);
        List<ReviewDto> reviewDtos = reviewPage.getContent()
                .stream().map(this::mapReviewToDto).toList();

        if(!reviewDtos.isEmpty()) {
            double averageRating = reviewService.getAverageRatingForVeterinarian(userId);
            userDto.setAverageRating(averageRating);
        }
        userDto.setReviews(reviewDtos);
    }

    private ReviewDto mapReviewToDto(Review review)  {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setStars(review.getStars());
        reviewDto.setFeedback(review.getFeedback());
        try{
            mapVeterinarianInfo(reviewDto,review);
            mapPatientInfo(reviewDto,review);
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        return reviewDto;

    }

    private void mapPatientInfo(ReviewDto reviewDto, Review review) throws SQLException {
        if(review.getPatient() != null) {
            reviewDto.setReviewerId(review.getPatient().getId());
            reviewDto.setReviewerName(review.getPatient().getFirstName() + " " + review.getPatient().getLastName());
            //set the photo
            setReviewerPhoto(reviewDto,review);
        }
    }

    private void mapVeterinarianInfo(ReviewDto reviewDto, Review review) throws SQLException {
        if(review.getVeterinarian() != null) {
            reviewDto.setVetId(review.getVeterinarian().getId());
            reviewDto.setVetName(review.getVeterinarian().getFirstName() + " " + review.getVeterinarian().getLastName());
            //set the photo
            setVeterinarianPhoto(reviewDto,review);
        }
    }
    private void setReviewerPhoto(ReviewDto reviewDto, Review review) {
        if(review.getPatient().getPhoto() != null) {
            try{
                reviewDto.setReviewerImage(photoService.getImageData(review.getPatient().getPhoto().getId()));
            }catch (SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            reviewDto.setReviewerImage(null);
        }
    }
    private void setVeterinarianPhoto(ReviewDto reviewDto, Review review) {
        if(review.getVeterinarian().getPhoto() != null) {
           try{
               reviewDto.setVetImage(photoService.getImageData(review.getVeterinarian().getPhoto().getId()));
           }catch (SQLException e){
               throw new RuntimeException(e.getMessage());
           }
        }else{
            reviewDto.setVetImage(null);
        }
    }
}
