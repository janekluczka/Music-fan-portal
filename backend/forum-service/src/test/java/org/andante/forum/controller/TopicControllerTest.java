package org.andante.forum.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.topic.TopicInputDTO;
import lombok.SneakyThrows;
import org.andante.forum.configuration.ForumTestConfiguration;
import org.andante.forum.repository.TopicRepository;
import org.andante.forum.repository.entity.TopicEntity;
import org.andante.forum.util.TopicTestUtil;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(TestContainersExtension.class)
@Import({TopicTestUtil.class, ForumTestConfiguration.class})
@Transactional
public class TopicControllerTest {

    private static final String CREATE_PATH = "/forum/topic";
    private static final String UPDATE_PATH = "/forum/topic";
    private static final String GET_PATH = "/forum/topic?id=%d";
    private static final String GET_PAGE_PATH = "/forum/topic/page?pageNumber=%d&pageAmount=%d&query=%s";
    private static final String GET_SUBTOPICS_PATH = "/forum/topic/subtopics?id=%d";
    private static final String DELETE_PATH = "/forum/topic/%d";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private TopicTestUtil topicTestUtil;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("Create")
    class CreateTests {

        @Test
        @SneakyThrows
        @DisplayName("Precondition filed")
        void preconditionFailed() {
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
        @DisplayName("Valid parent")
        void validParent() {
            TopicInputDTO topicInputDTO = topicTestUtil.buildValidTopic();
            String serializedInput = objectMapper.writeValueAsString(topicInputDTO);

            mockMvc.perform(post(CREATE_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(serializedInput))
                    .andExpect(status().isOk());
        }

        @Test
        @SneakyThrows
        @DisplayName("Valid child")
        void validChild() {
            TopicEntity parent = topicRepository.save(topicTestUtil.buildValidParentTopicEntity());

            TopicInputDTO topicInputDTO = topicTestUtil.buildValidChildTopic(parent.getId());
            String serializedInput = objectMapper.writeValueAsString(topicInputDTO);

            mockMvc.perform(post(CREATE_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(serializedInput))
                    .andExpect(status().isOk());
        }

        @Test
        @SneakyThrows
        @DisplayName("Invalid child")
        void invalidChild() {
            TopicInputDTO topicInputDTO = topicTestUtil.buildValidChildTopic(1L);
            String serializedInput = objectMapper.writeValueAsString(topicInputDTO);

            mockMvc.perform(post(CREATE_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(serializedInput))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Update")
    class UpdateTests {

        @Test
        @SneakyThrows
        @DisplayName("Valid update")
        void validUpdate() {
            TopicEntity parent = topicRepository.save(topicTestUtil.buildValidParentTopicEntity());
            TopicInputDTO topicInputDTO = topicTestUtil.buildValidTopic();
            topicInputDTO.setId(parent.getId());
            topicInputDTO.setName(parent.getId() + " updated");
            String serializedInput = objectMapper.writeValueAsString(topicInputDTO);

            mockMvc.perform(put(UPDATE_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(serializedInput))
                    .andExpect(status().isOk());

            Optional<TopicEntity> databaseResponse = topicRepository.findById(parent.getId());
            assertThat(databaseResponse.get().getName()).isEqualTo(topicInputDTO.getName());
        }
    }

    @Nested
    @DisplayName("Get By Query")
    class GetByQueryTests {

        @Test
        @SneakyThrows
        @DisplayName("invalid Query")
        void invalidQuery() {
            // given
            String query = "";
            int pageNumber = -1;
            int pageSize = 0;

            // when
            mockMvc.perform(get(String.format(GET_PAGE_PATH, pageNumber, pageSize, query))
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Create")
    class DeleteTests {

        @Test
        @SneakyThrows
        @DisplayName("Valid delete")
        void validDelete() {
            TopicEntity parent = topicRepository.save(topicTestUtil.buildValidParentTopicEntity());
            TopicEntity child = topicRepository.save(topicTestUtil.buildValidChildTopicEntity(parent));

            mockMvc.perform(put(String.format(DELETE_PATH, parent.getId())))
                    .andExpect(status().isNotFound());

        }
    }

    @SneakyThrows
    private <T> T readResponse(MvcResult result) {
        return objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {

        });
    }
}
