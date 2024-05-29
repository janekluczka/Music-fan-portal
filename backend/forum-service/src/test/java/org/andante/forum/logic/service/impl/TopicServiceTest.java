package org.andante.forum.logic.service.impl;

import lombok.SneakyThrows;
import org.andante.forum.logic.model.topic.TopicInputModel;
import org.andante.forum.repository.TopicRepository;
import org.andante.forum.repository.entity.TopicEntity;
import org.andante.forum.util.TopicTestUtil;
import org.andante.forum.util.UserTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import utility.TestContainersExtension;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith({TestContainersExtension.class})
@Import({UserTestUtil.class})
@Transactional
public class TopicServiceTest {

    @Autowired
    private TopicServiceImpl topicService;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private TopicTestUtil topicTestUtil;

    @Nested
    @DisplayName("Create")
    class CreateTests {

        @Test
        @SneakyThrows
        @DisplayName("Valid parent")
        void validParent() {
            TopicInputModel parent = topicTestUtil.buildValidParentTopicModel();

            Long databaseResponse = topicService.create(parent);

            assertThat(databaseResponse).isGreaterThan(0);
        }

        @Test
        @SneakyThrows
        @DisplayName("Valid child")
        void validChild() {
            TopicEntity parent = topicRepository.save(topicTestUtil.buildValidParentTopicEntity());
            TopicInputModel child = topicTestUtil.buildValidChildTopicModel(parent.getId());

            Long databaseResponse = topicService.create(child);

            assertThat(databaseResponse).isGreaterThan(0);
        }

    }

    @Nested
    @DisplayName("Update")
    class UpdateTests {

        @Test
        @SneakyThrows
        @DisplayName("Valid update")
        void validUpdate() {
            TopicEntity topic = topicRepository.save(topicTestUtil.buildValidParentTopicEntity());
            String oldName = topic.getName();

            TopicInputModel updated = topicTestUtil.buildValidParentTopicModel();
            updated.setId(topic.getId());
            updated.setName(oldName + "Updated");

            topicService.update(updated);

            TopicEntity updatedEntity = topicRepository.getReferenceById(topic.getId());

            assertThat(updatedEntity.getName()).isEqualTo(oldName + "Updated");
        }
    }

    @Nested
    @DisplayName("Get By Query")
    class GetByQueryTests {

        @Test
        @SneakyThrows
        @DisplayName("invalid Query")
        void invalidQuery() {

        }
    }

    @Nested
    @DisplayName("Create")
    class DeleteTests {

        @Test
        @SneakyThrows
        @DisplayName("Valid delete")
        void validDelete() {

        }
    }
}
