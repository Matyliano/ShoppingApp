package com.example.shopping.controller;

import com.example.shopping.dto.UserDto;
import com.example.shopping.entity.User;
import com.example.shopping.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "test@mail")
    void shouldGetUserByIdWithAccessToUser() throws Exception {
        User user = userRepository.save(User.builder()
                .email("test@mail")
                .password("password")
                .firstName("Test")
                .lastName("Test")
                .build());
        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser
    void shouldNotGetUserWithoutHaveAccess() throws Exception {
        User user = userRepository.save(User.builder()
                .email("test@mail")
                .password("password")
                .firstName("Test")
                .lastName("Test")
                .build());

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").doesNotExist());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetUserByIdWhileUserIsAdmin() throws Exception {
        User user = userRepository.save(User.builder()
                .email("test@mail")
                .password("password")
                .firstName("Test")
                .lastName("Test")
                .build());

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }



    @Test
    void shouldSaveUser() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(UserDto.builder()
                        .email("test@mail")
                        .password("password")
                        .confirmPassword("password")
                        .firstName("test")
                        .lastName("test1")
                        .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.email").value("test@mail"))
                .andExpect(jsonPath("$.firstName").value("test"))
                .andExpect(jsonPath("$.lastName").value("test1"));
    }

    @Test
    @WithMockUser(username = "mail@testmail")
    void shouldUpdateUserWhenUserIsLogged() throws Exception {
        User user = userRepository.save(User.builder()
                .firstName("firstNameTest")
                .lastName("lastNameTest")
                .email("mail@testmail")
                .password("testPassword")
                .build());

        mockMvc.perform(put("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(UserDto.builder()
                        .email("testmail@mail")
                        .password("password")
                        .confirmPassword("password")
                        .firstName("Test")
                        .lastName("TestLastname")
                        .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.email").value("testmail@mail"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("TestLastname"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateUserWhenUserIsAdmin() throws Exception {
        User user = userRepository.save(User.builder()
                .firstName("firstNameTest")
                .lastName("lastNameTest")
                .email("mail@testmail")
                .password("testPassword")
                .build());

        mockMvc.perform(put("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(UserDto.builder()
                        .email("testmail@mail")
                        .password("password")
                        .confirmPassword("password")
                        .firstName("Test")
                        .lastName("TestLastname")
                        .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.email").value("testmail@mail"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("TestLastname"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteUser() throws Exception {
        User user = userRepository.save(User.builder()
                .firstName("firstNameTest")
                .lastName("lastNameTest")
                .email("mail@testmail")
                .password("testPassword")
                .build());

        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }
    @Test
    void shouldNotGetUserWhenUserIsNotLogged() throws Exception {
        mockMvc.perform(get("/api/users/5"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void shouldNotUpdateUserWhenNoLogged() throws Exception {

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(UserDto.builder()
                        .email("testmail@mail")
                        .password("password")
                        .confirmPassword("password")
                        .firstName("Test")
                        .lastName("TestLastname")
                        .build())))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void shouldNotReturnUserPage() throws Exception {

        mockMvc.perform(get("/api/users")
                .queryParam("page", "0")
                .queryParam("size", "5"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void shouldNotDeleteUserWhenIsNotAdmin() throws Exception {

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnUserPage() throws Exception {
        userRepository.save(User.builder()
                .firstName("firstNameTest")
                .lastName("lastNameTest")
                .email("mail@testmail")
                .password("testPassword")
                .build());

        mockMvc.perform(get("/api/users")
                .queryParam("page", "0")
                .queryParam("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }
}
