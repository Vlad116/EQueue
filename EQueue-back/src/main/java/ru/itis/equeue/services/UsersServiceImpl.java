package ru.itis.equeue.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.equeue.dto.LoginDto;
import ru.itis.equeue.dto.TokenDto;
import ru.itis.equeue.dto.UserDto;
import ru.itis.equeue.models.User;
import ru.itis.equeue.repositories.UsersRepository;
import ru.itis.equeue.security.providers.JwtTokenProvider;
import ru.itis.equeue.security.role.Role;


import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void registration(UserDto userDto) {
        String confirmString = UUID.randomUUID().toString();

        System.out.println(userDto.getHashPassword());
        if (!usersRepository.existsByEmailIgnoreCase(userDto.getEmail())) {
            User userToSave = User.builder()
                    .login(userDto.getLogin())
                    .email(userDto.getEmail())
                    .hashPassword(passwordEncoder.encode(userDto.getHashPassword()))
                    .lastName(userDto.getLastName())
                    .firstName(userDto.getFirstName())
                    .phoneNumber(userDto.getPhoneNumber())
                    .role(Role.USER)
                    .isUserNonLocked(true)
                    .isEmailConfirmed(false)
                    .confirmString(confirmString)
                    .build();

            User user = usersRepository.save(userToSave);
            System.out.println(user.getId());
            amqpTemplate.convertAndSend("confirm_registration", user.getId());
            return;
        }
        throw new IllegalArgumentException("Registration attempt failed");
    }

    @Override
    public TokenDto login(LoginDto loginDto) {
        Optional<User> candidate = usersRepository.getByLoginIgnoreCase(loginDto.getLogin());
        if (candidate.isPresent()) {
            User user = candidate.get();
            if (passwordEncoder.matches(loginDto.getPassword(), user.getHashPassword()) && user.getIsUserNonLocked()) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDto.getLogin(),
                                loginDto.getPassword()
                        )
                );
                String token = jwtTokenProvider.generateToken(authentication);
                return TokenDto.from(token, user.getId());
            }
        }
        throw new IllegalArgumentException("Login attempt failed");
    }

//    @Override
//    public void addEvent(Long userId, Event event) {
//
//    }

//    @Override
//    public void update(ProfileForm userDto, Authentication authentication) {
//
//            User user = (User) authentication.getPrincipal();
//            if (!userDto.getPassword().isEmpty()){
//                user.setPasswordHash(encoder.encode(userDto.getPassword()));
//            }
//            user.setLastName(userDto.getLastName());
//            user.setFirstName(userDto.getFirstName());
//            user.setPhone(userDto.getPhone());
//            user.setAddress(userDto.getAddress());
//            usersRepository.save(user);
//    }

//    @Override
//    @Transactional
//    public ProfileDto getUser(Authentication authentication, Long id) {
//
//        Optional<User> userOptional = usersRepository.findUserById(id);
//        if (userOptional.isPresent()){
//            User user = userOptional.get();
//            ProfileDto profileDto = ProfileDto.builder()
//                    .firstName(user.getFirstName())
//                    .lastName(user.getLastName())
//                    .address(user.getAddress())
//                    .phone(user.getPhone())
//                    .build();
//            List<Photo> photos = user.getProfilePhoto();
//            if (!photos.isEmpty()){
//                profileDto.setImage(photos.get(photos.size()-1).getTitle());
//            }
//            return profileDto;
//        }
//
//        throw new IllegalArgumentException();
//    }
//
//    @Override
//    @Transactional
//    public ProfileDto getUser(Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
//        Optional<User> userOptional = usersRepository.findUserById(user.getId());
//        if (userOptional.isPresent()){
//            List<Photo> photos = userOptional.get().getProfilePhoto();
//            user = userOptional.get();
//            ProfileDto profileDto = ProfileDto.builder()
//                    .login(user.getLogin())
//                    .firstName(user.getFirstName())
//                    .lastName(user.getLastName())
//                    .address(user.getAddress())
//                    .phone(user.getPhone())
//                    .build();
//            if (!photos.isEmpty()){
//                profileDto.setImage(photos.get(photos.size()-1).getTitle());
//            }
//            return profileDto;
//        }
//        throw new IllegalArgumentException();
//    }
}
