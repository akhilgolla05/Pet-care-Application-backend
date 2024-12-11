package com.learnboot.universalpetcare.utils;

public class UrlMapping {

    public static final String API = "/api/v1";
    public static final String USERS = API+"/users";
    public static final String REGISTER_USER = "/register" ;
    public static final String UPDATE_USER = "user/{userId}/update";
    public static final String GET_USER_BY_ID = "/user/{userId}";
    public static final String DELETE_USER_BY_ID = "/user/{userId}/delete" ;
    public static final String GET_ALL_USERS = "/all-users";

    public static final String APPOINTMENTS = API+"/appointments";
    public static final String ALL_APPOINTMENTS = "/all";
    public static final String BOOK_APPOINTMENT = "/book-appointment";
    public static final String APPOINTMENT_BY_ID = "/appointment/{id}/fetch/appointment";
    public static final String APPOINTMENTS_BY_APPOINTMENT_NO = "/appointment/{appointmentNo}/appointment";
    public static final String DELETE_APPOINTMENT_BY_ID = "/appointment/{id}/delete";
    public static final String UPDATE_APPOINTMENT_BY_ID = "/appointment/{id}/update";
    public static final String CANCEL_APPOINTMENT = "/appointment/{id}/cancel";
    public static final String DECLINE_APPOINTMENT = "/appointment/{id}/decline";
    public static final String APPROVE_APPOINTMENT = "/appointment/{id}/approve";

    public static final String PETS = API+"/pets";
    public static final String SAVE_PETS = "/save-pets" ;
    public static final String GET_PET_BY_ID = "/pet/{petId}/get";
    public static final String DELETE_PET_BY_ID = "/pet/{petId}/delete" ;
    public static final String UPDATE_PET = "/pet/{petId}/update";
    public static final String GET_PET_TYPES = "/get-types";
    public static final String GET_PET_COLORS = "/get-pet-colors";
    public static final String GET_PET_BREEDS = "/get-pet-breeds";

    public static final String PHOTOS = API+"/photos";
    public static final String GET_PHOTO_BY_ID = "/photo/{photoId}/photo";
    public static final String DELETE_PHOTO = "/photo/{photoId}/user/{userId}/delete";
    public static final String UPDATE_PHOTO = "/photo/{photoId}/update";
    public static final String UPLOAD_PHOTO = "/photo/upload";

    public static final String REVIEWS = API+"/reviews" ;
    public static final String SUBMIT_REVIEW = "/submit-review" ;
    public static final String GET_USER_REVIEWS = "/user/{userId}/reviews";
    public static final String UPDATE_REVIEW = "/review/{reviewId}/update";
    public static final String DELETE_REVIEW = "/review/{reviewId}/delete";
    public static final String GET_AVG_RATING_FOR_VET = "/vet/{vetId}/get-average-rating";

    public static final String VETERINARIANS = API+"/veterinarians";
    public static final String GET_ALL_VETS ="/get-all-veterinarians" ;
    public static final String SEARCH_VETERINARIAN_FOR_APPOINTMENT = "/search-veterinarian" ;

    public static final String CHANGE_PASSWORD = "/user/{userId}/change-password";


}
