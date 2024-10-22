package com.learnboot.universalpetcare.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String feedback;
    private int stars;
    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    private User veterinarian;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;

    //we can also remove relationship from here also
    public void removeRelationShip(){
        Optional.ofNullable(veterinarian)
                .ifPresent(user->user.getReviews().remove(this));
        Optional.ofNullable(patient)
                .ifPresent(user->user.getReviews().remove(this));
    }
}
