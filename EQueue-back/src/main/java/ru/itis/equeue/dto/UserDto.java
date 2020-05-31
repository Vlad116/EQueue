package ru.itis.equeue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.equeue.models.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String login;
    private String email;
    private String hashPassword;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String address;

    public static UserDto from(User user){
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .hashPassword(user.getHashPassword())
                .login(user.getLogin())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}