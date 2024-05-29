package org.andante.forum.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.post.PostInputDTO;
import dto.response.PostResponseInputDTO;
import lombok.SneakyThrows;
import org.andante.forum.configuration.ForumTestConfiguration;
import org.andante.forum.repository.PostRepository;
import org.andante.forum.repository.PostResponseRepository;
import org.andante.forum.repository.TopicRepository;
import org.andante.forum.repository.UserRepository;
import org.andante.forum.repository.entity.PostEntity;
import org.andante.forum.repository.entity.TopicEntity;
import org.andante.forum.repository.entity.UserEntity;
import org.andante.forum.util.PostResponseTestUtil;
import org.andante.forum.util.PostTestUtil;
import org.andante.forum.util.TopicTestUtil;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(TestContainersExtension.class)
@Import({PostResponseTestUtil.class, PostTestUtil.class, TopicTestUtil.class, UserTestUtil.class, ForumTestConfiguration.class})
@Transactional
public class PostResponseControllerTest {

    private static final String CREATE_PATH = "/forum/response";
    private static final String UPDATE_PATH = "/forum/response";
    private static final String GET_PATH = "/forum/response?id=%d";
    private static final String GET_PAGE_PATH = "/forum/response/page?pageSize=%d&pageAmount=%d&query=%s";
    private static final String LIKE_PATH = "/forum/response/like";
    private static final String DELETE_PATH = "/forum/response/%d";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostResponseRepository postResponseRepository;
    @Autowired
    private PostResponseTestUtil postResponseTestUtil;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostTestUtil postTestUtil;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private TopicTestUtil topicTestUtil;
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
        void validCreate() {
            TopicEntity topic = topicRepository.save(topicTestUtil.buildValidParentTopicEntity());
            UserEntity user = userRepository.save(userTestUtil.buildValidUserEntity());
            PostEntity post = postRepository.save(postTestUtil.createValidPostEntity(topic, user));

            PostResponseInputDTO response = postResponseTestUtil.createValidPostResponse(post.getId(), user.getUsername());
            String serializedInput = objectMapper.writeValueAsString(response);

            MvcResult result = mockMvc.perform(post(CREATE_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(serializedInput))
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("Like")
    class LikeTests {

        @Test
        @SneakyThrows
        @DisplayName("should return bad request for empty input")
        void shouldReturnBadRequest() {
            String serializedInput = "{}";

            MvcResult result = mockMvc.perform(post(LIKE_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(serializedInput))
                    .andExpect(status().isPreconditionFailed())
                    .andReturn();

            List<String> violationMessages = readResponse(result);
            assertThat(violationMessages).isNotNull().hasSizeGreaterThanOrEqualTo(1);
        }
    }

    @SneakyThrows
    private <T> T readResponse(MvcResult result) {
        return objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
    }
}
