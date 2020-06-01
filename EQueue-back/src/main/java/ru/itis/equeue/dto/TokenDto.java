package ru.itis.equeue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDto {
    private String value;
    private Long userId;

    public static TokenDto from(String token, Long id) {
        return TokenDto.builder()
                .userId(id)
                .value(token)
                .build();
    }
}