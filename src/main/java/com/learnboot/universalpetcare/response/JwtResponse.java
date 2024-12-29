package com.learnboot.universalpetcare.response;

//to generate a JWT token

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private long id;
    private Object data;

}
