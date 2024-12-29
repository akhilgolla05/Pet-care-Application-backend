package com.learnboot.universalpetcare.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NaturalId
    private String name;

    @ManyToMany
    private Collection<User> users = new HashSet<>();

    public Role(String name) {
        this.name = name;
    }

    public String getName(){
        return name!=null ? name : "";
    }
}
