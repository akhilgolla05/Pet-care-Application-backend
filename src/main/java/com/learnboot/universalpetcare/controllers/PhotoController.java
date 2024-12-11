package com.learnboot.universalpetcare.controllers;

import com.learnboot.universalpetcare.exceptions.ResourceNotFoundException;
import com.learnboot.universalpetcare.models.Photo;
import com.learnboot.universalpetcare.response.ApiResponse;
import com.learnboot.universalpetcare.services.photo.IPhotoService;
import com.learnboot.universalpetcare.utils.FeedBackMessage;
import com.learnboot.universalpetcare.utils.UrlMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
@CrossOrigin(origins = {"http://localhost:5173"})
@RestController
@RequestMapping(UrlMapping.PHOTOS)
@RequiredArgsConstructor
public class PhotoController {

    private final IPhotoService photoService;

    @PostMapping(UrlMapping.UPLOAD_PHOTO)
    public ResponseEntity<ApiResponse> uploadPhoto(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("userId") long userId) {
        try{
            Photo photo =photoService.savePhoto(file, userId);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.SUCCESS, null));
        }catch(IOException | SQLException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(FeedBackMessage.SERVER_ERROR, null));
        }
    }

    @PutMapping(UrlMapping.UPDATE_PHOTO)
    public ResponseEntity<ApiResponse> updatePhoto(@RequestParam("file") MultipartFile file,
                                                   @PathVariable long photoId){
        try{
            Photo photo = photoService.updatePhoto(file,photoId);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.UPDATED, null));
        } catch (ResourceNotFoundException | SQLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping(UrlMapping.DELETE_PHOTO)
    public ResponseEntity<ApiResponse> deletePhoto(@PathVariable long photoId,
                                                   @PathVariable long userId){
        try{
            photoService.deletePhoto(photoId, userId);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.DELETE_SUCCESS, null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMapping.GET_PHOTO_BY_ID)
    public ResponseEntity<ApiResponse> getPhoto(@PathVariable long photoId){
        try{
            Photo photo = photoService.getPhotoById(photoId);
            if(photo!=null) {
                byte[] photoByte = photoService.getImageData(photoId);
                return ResponseEntity.ok(new ApiResponse(FeedBackMessage.FOUND, photoByte));
            }
        } catch (ResourceNotFoundException | SQLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(FeedBackMessage.SERVER_ERROR, null));
    }

}
