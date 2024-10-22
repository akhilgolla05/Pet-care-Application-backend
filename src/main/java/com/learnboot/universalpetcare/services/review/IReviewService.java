package com.learnboot.universalpetcare.services.review;

import com.learnboot.universalpetcare.models.Review;
import com.learnboot.universalpetcare.request.ReviewUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IReviewService {

    Review saveReview(Review review,Long veterinarianId, Long reviewerId);
    double getAverageRatingForVeterinarian(Long veterinarianId);
    Review editReview(ReviewUpdateRequest review, long reviewerId);
    Page<Review> findAllReviewsByUserId(long userId, int page, int size);
    void deleteReview(long reviewId);
}
