package com.learnboot.universalpetcare.controllers;
import com.learnboot.universalpetcare.dto.ReviewDto;
import com.learnboot.universalpetcare.exceptions.AlreadyExistsException;
import com.learnboot.universalpetcare.exceptions.ResourceNotFoundException;
import com.learnboot.universalpetcare.models.Review;
import com.learnboot.universalpetcare.request.ReviewUpdateRequest;
import com.learnboot.universalpetcare.response.ApiResponse;
import com.learnboot.universalpetcare.services.review.IReviewService;
import com.learnboot.universalpetcare.utils.FeedBackMessage;
import com.learnboot.universalpetcare.utils.UrlMapping;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlMapping.REVIEWS)
@RequiredArgsConstructor
public class ReviewController {

    private final IReviewService reviewService;
    private final ModelMapper modelMapper;

    @PostMapping(UrlMapping.SUBMIT_REVIEW)
    public ResponseEntity<ApiResponse> saveReview(@RequestBody Review review,
                                                  @RequestParam Long veterinarianId,
                                                  @RequestParam Long reviewerId) {
        try{
            Review savedReview = reviewService.saveReview(review, veterinarianId,reviewerId );
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.SUCCESS, savedReview.getId()));
        }catch (IllegalArgumentException | AlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ApiResponse(e.getMessage(), null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMapping.GET_USER_REVIEWS)
    public ResponseEntity<ApiResponse> getReviewsByUserId(@PathVariable Long userId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size) {
        Page<Review> reviewPage = reviewService.findAllReviewsByUserId(userId, page, size);
        Page<ReviewDto> reviewsDt0 = reviewPage.map(review -> modelMapper.map(review, ReviewDto.class));
        return ResponseEntity.ok(new ApiResponse(FeedBackMessage.FOUND, reviewsDt0));
    }

    @PutMapping(UrlMapping.UPDATE_REVIEW)
    public ResponseEntity<ApiResponse> updateReview(@RequestBody ReviewUpdateRequest request,
                                                    @PathVariable long reviewId){
        try{
            Review updatedReview = reviewService.editReview(request,reviewId);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.UPDATED, updatedReview.getId()));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping(UrlMapping.DELETE_REVIEW)
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable long reviewId){
        try{
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.DELETE_SUCCESS, null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMapping.GET_AVG_RATING_FOR_VET)
    public ResponseEntity<ApiResponse> getAverageRatingForVet(@PathVariable Long vetId) {
        double averageRating = reviewService.getAverageRatingForVeterinarian(vetId);
        return ResponseEntity.ok(new ApiResponse(FeedBackMessage.FOUND, averageRating));
    }
}
