package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.global.template.ControllerTestTemplate;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.userservice.global.fixture.TestInfoFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest extends ControllerTestTemplate {

    @Autowired
    private UserService userService;

    @Test
    void createUser() throws Exception{
        // given
        final RequestUser requestUser = RequestUser.builder()
                .name(USER_ID)
                .password(PASSWORD)
                .email(EMAIL)
                .build();

        // when
        final ResultActions actions = mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(requestUser))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId",USER_ID).exists())
                .andExpect(jsonPath("$.email",EMAIL).exists())
                .andExpect(jsonPath("$.name").exists())
                .andDo(print());
    }

    @Test
    void getUsers() throws Exception{
        // when
        final ResultActions actions = mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getUser() throws Exception{
        //given
        UserDto userDto = new UserDto();
        userDto.setUserId(USER_ID);
        userDto.setEmail(EMAIL);
        userDto.setPassword(PASSWORD);
        userDto.setName(USER_NAME);
        userService.createUser(userDto);

        // when
        final ResultActions actions = mockMvc.perform(get("/users/{userId}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",USER_ID).exists())
                .andDo(print());
    }
}