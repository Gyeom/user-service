package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static com.example.userservice.global.fixture.TestInfoFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    CircuitBreakerFactory circuitBreakerFactory;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .userId(USER_ID)
                .password(PASSWORD)
                .name(USER_NAME)
                .email(EMAIL)
                .build();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        userEntity = mapper.map(userDto, UserEntity.class);

        // Using in UserService
        userEntity.setEncryptedPassword(passwordEncoder.encode(PASSWORD));
    }


    @Test
    void createUser() {
        // given
        given(userRepository.save(any())).willReturn(userEntity);

        // when
        final UserDto result = userService.createUser(userDto);

        // then
        verify(userRepository).save(any());
        assertThat(result.getUserId()).isEqualTo(USER_ID);
        assertThat(result.getEmail()).isEqualTo(EMAIL);
        assertThat(result.getPassword()).isNull();
    }

    @Test
    void getUserByUserId() {
        // given
        given(userRepository.findByUserId(any())).willReturn(userEntity);

        // when
        userService.getUserByUserId(USER_ID);

        // then
        verify(userRepository).findByUserId(any());
    }

    @Test
    void getUserByAll() {
        // given
        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(userEntity);
        given(userRepository.findAll()).willReturn(userEntities);

        // when
        final List<UserEntity> results = (List<UserEntity>) userService.getUserByAll();

        // then
        verify(userRepository).findAll();
        assertThat(results).contains(userEntity);
    }

    @Test
    void loadUserByUsername() {
        // given
        given(userRepository.findByEmail(any())).willReturn(userEntity);

        // when
        final UserDetails results =  userService.loadUserByUsername(USER_NAME);

        // when
        verify(userRepository).findByEmail(any());
        assertThat(results.getUsername()).isEqualTo(EMAIL);
        assertThat(results.getPassword()).isEqualTo(userEntity.getEncryptedPassword());
    }

    @Test
    void getUserDetailsByEmail() {
        // given
        given(userRepository.findByEmail(any())).willReturn(userEntity);

        // when
        final UserDto results = userService.getUserDetailsByEmail(EMAIL);

        // when
        verify(userRepository).findByEmail(any());
        assertThat(results.getUserId()).isEqualTo(USER_ID);
        assertThat(results.getEmail()).isEqualTo(EMAIL);
    }
}