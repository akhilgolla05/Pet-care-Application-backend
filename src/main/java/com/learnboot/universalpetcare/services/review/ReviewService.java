package com.learnboot.universalpetcare.services.review;

import com.learnboot.universalpetcare.enums.AppointmentStatus;
import com.learnboot.universalpetcare.exceptions.AlreadyExistsException;
import com.learnboot.universalpetcare.exceptions.ResourceNotFoundException;
import com.learnboot.universalpetcare.exceptions.UserAlreadyExistsException;
import com.learnboot.universalpetcare.models.Review;
import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.repository.AppointmentRepository;
import com.learnboot.universalpetcare.repository.ReviewRepository;
import com.learnboot.universalpetcare.repository.UserRepository;
import com.learnboot.universalpetcare.request.ReviewUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Override
    public Review saveReview(Review review,Long veterinarianId, Long reviewerId) {

        //check if reviewer is same as the docker being reviewed
        if(reviewerId.equals(veterinarianId)) {
            throw new IllegalArgumentException("Veterinarian cannot review himself");
        }
        //check if reviewer has previously submitted a review for this doctor?
//        Optional<Review> existingReview = reviewRepository.findByVeterinarianIdAndPatientId(veterinarianId,reviewerId);
//        log.info("Existing review: {}", existingReview.isPresent());
//        if(existingReview.isPresent()) {
//            throw new AlreadyExistsException("Review has been provided already for this Veterinarian, You may Edit Previous Review!");
//        }
        //check if reviewer has gotten a completed appointment with the Doctor;
//        boolean hadAppointmentCompleted = appointmentRepository.existsByVeterinarianIdAndPatientIdAndStatus(veterinarianId,reviewerId, AppointmentStatus.COMPLETED);
//        if(!hadAppointmentCompleted) {
//            throw new IllegalArgumentException("Only Patients With Completed Appointment can Leave Review");
//        }
        //get the reviewer (patient) from the DB
        User patient = userRepository.findById(reviewerId)
                .orElseThrow(()-> new ResourceNotFoundException("Patient/User Not Found"));
        //get the reviewer (veterinarin) from the DB
        User veterinarian = userRepository.findById(veterinarianId)
                .orElseThrow(()-> new ResourceNotFoundException("Veterinarian/User Not Found"));
        //set both to the Review
        review.setPatient(patient);
        review.setVeterinarian(veterinarian);
        return reviewRepository.save(review);
        //save the Review
    }

    @Transactional
    @Override
    public double getAverageRatingForVeterinarian(Long veterinarianId) {
        List<Review> reviews = reviewRepository.findByVeterinarianId(veterinarianId);
        return reviews.isEmpty() ? 0 : reviews.stream()
                .mapToInt(Review::getStars).average().orElse(0.0);
    }

    @Override
    public Review editReview(ReviewUpdateRequest review, long reviewerId) {
        return reviewRepository.findById(reviewerId)
                .map(existing->{
                    existing.setStars(review.getStars());
                    existing.setFeedback(review.getFeedback());
                    return reviewRepository.save(existing);
                })
                .orElseThrow(()-> new ResourceNotFoundException("Review Not Found"));
    }
    @Override
    public Page<Review> findAllReviewsByUserId(long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return reviewRepository.findAllByUserId(userId,pageRequest);
    }
    @Override
    public void deleteReview(long reviewId) {
        reviewRepository.findById(reviewId)
                .ifPresentOrElse(Review::removeRelationShip,()->{
                    throw new ResourceNotFoundException("Review Not Found");
                });
//                .map(rev-> {
//                    rev.setPatient(null);
//                    rev.setVeterinarian(null);
//                    return reviewRepository.save(rev);
//                }).orElseThrow(()-> new ResourceNotFoundException("Review Not Found"));
        reviewRepository.deleteById(reviewId);
    }
}
