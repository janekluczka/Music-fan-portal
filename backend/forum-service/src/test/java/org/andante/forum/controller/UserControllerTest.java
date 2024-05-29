package org.andante.forum.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.UserDTO;
import lombok.SneakyThrows;
import org.andante.forum.configuration.ForumTestConfiguration;
import org.andante.forum.repository.UserRepository;
import org.andante.forum.repository.entity.UserEntity;
import org.andante.forum.util.UserTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import utility.TestContainersExtension;

import javax.transaction.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(TestContainersExtension.class)
@Import({UserTestUtil.class, ForumTestConfiguration.class})
@Transactional
public class UserControllerTest {

    private static final String CREATE_PATH = "/forum/user";
    private static final String GET_PATH = "/forum/user?email=%s";
    private static final String DELETE_PATH = "/forum/user?email=%s";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTestUtil userTestUtil;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("Create")
    class CreateTests {

        @Test
        @SneakyThrows
        @DisplayName("should return bad request for empty input")
        void shouldReturnBadRequest() {
            String serializedInput = "{}";

            MvcResult result = mockMvc.perform(post(CREATE_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(serializedInput))
                    .andExpect(status().isPreconditionFailed())
                    .andReturn();

            List<String> violationMessages = readResponse(result);
            assertThat(violationMessages).isNotNull().hasSizeGreaterThanOrEqualTo(1);
        }

        @Test
        @SneakyThrows
        @DisplayName("should return bad request for empty input")
        void shouldReturnGoodRequest() {
            UserDTO userDTO = userTestUtil.buildValidUserDTO();
            String serializedInput = objectMapper.writeValueAsString(userDTO);

            mockMvc.perform(post(CREATE_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(serializedInput))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Create")
    class GetTests {

    }

    @Nested
    @DisplayName("Create")
    class DeleteTests {

        @Test
        @SneakyThrows
        @DisplayName("should return bad request for empty input")
        void shouldReturnUserNotFound() {
            String username = "Username";
            mockMvc.perform(delete(String.format(DELETE_PATH, username)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @SneakyThrows
        @DisplayName("should return bad request for empty input")
        void shouldDeleteExistingUser() {
            UserEntity existingUser = userRepository.save(UserEntity.builder()
                    .username("Username")
                    .emailAddress("Username")
                    .name("John")
                    .surname("Doe")
                    .build());
            String username = existingUser.getUsername();
            mockMvc.perform(delete(String.format(DELETE_PATH, username)))
                    .andExpect(status().isOk());
        }
    }

    @SneakyThrows
    private <T> T readResponse(MvcResult result) {
        return objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
    }
}
