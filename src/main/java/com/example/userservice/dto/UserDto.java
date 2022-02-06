package com.example.userservice.dto;

import com.example.userservice.vo.ResponseOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String email;
    private String name;
    private String password;
    private String userId;
    private Date createdAt;

    private String encryptedPassword;
    private List<ResponseOrder> orders;
}
