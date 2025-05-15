package com.clonecod.clone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @JsonProperty("login_id")
    private String userId;

    private String password;

    @JsonProperty("member_name")
    private String name;

    @JsonProperty("birthday")
    private String birthDate;

    private String gender;
    private String email;

    @JsonProperty("tel")
    private String phone;
}
