package org.andante.forum.util;

import dto.topic.TopicInputDTO;
import org.andante.forum.logic.model.topic.TopicInputModel;
import org.andante.forum.repository.entity.TopicEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;

@Component
public class TopicTestUtil {

    public TopicEntity buildValidChildTopicEntity(TopicEntity parent) {
        return TopicEntity.builder()
                .name("Rap")
                .imageUrl("https://image.com/image.jpg")
                .parentTopic(parent)
                .childTopics(new HashSet<>(Collections.emptySet()))
                .posts(new HashSet<>(Collections.emptySet()))
                .build();
    }

    public TopicEntity buildValidParentTopicEntity() {
        return TopicEntity.builder()
                .name("Rap")
                .imageUrl("https://image.com/image.jpg")
                .parentTopic(null)
                .childTopics(new HashSet<>(Collections.emptySet()))
                .posts(new HashSet<>(Collections.emptySet()))
                .build();
    }

    public TopicInputModel buildValidChildTopicModel(Long parent) {
        return TopicInputModel.builder()
                .name("Rap")
                .imageUrl("https://image.com/image.jpg")
                .parentTopic(parent)
                .build();
    }

    public TopicInputModel buildValidParentTopicModel() {
        return TopicInputModel.builder()
                .name("Rap")
                .imageUrl("https://image.com/image.jpg")
                .build();
    }

    public TopicInputDTO buildValidTopic() {
        return TopicInputDTO.builder()
                .name("Rap")
                .imageUrl("https://image.com/image.jpg")
                .build();
    }

    public TopicInputDTO buildValidChildTopic(Long parentId) {
        return TopicInputDTO.builder()
                .name("Eminem")
                .imageUrl("https://image.com/image.jpg")
                .parentId(parentId)
                .build();
    }
}
