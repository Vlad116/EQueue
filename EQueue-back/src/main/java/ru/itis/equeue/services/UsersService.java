package ru.itis.equeue.services;


import ru.itis.equeue.dto.LoginDto;
import ru.itis.equeue.dto.TokenDto;
import ru.itis.equeue.dto.UserDto;
import ru.itis.equeue.models.Event;

public interface UsersService {
    TokenDto login(LoginDto loginDto);

    void registration(UserDto user);

    void addAvatar(String info);

//    void addEvent(Long userId, Event event);
//    void update(ProfileForm profileForm, Authentication authentication);
//    ProfileDto getUser(Authentication authentication, Long id);
//    ProfileDto getUser(Authentication authentication);

}
