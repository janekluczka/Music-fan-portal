package org.andante.forum.util;

import dto.post.PostInputDTO;
import dto.post.PostLikeDTO;
import org.andante.forum.logic.model.post.PostInputModel;
import org.andante.forum.logic.model.post.PostLikesRelationModel;
import org.andante.forum.repository.entity.PostEntity;
import org.andante.forum.repository.entity.PostLikesRelationEntity;
import org.andante.forum.repository.entity.TopicEntity;
import org.andante.forum.repository.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;

@Component
public class PostTestUtil {

    public PostEntity createValidPostEntity(TopicEntity topic, UserEntity user) {
        return PostEntity.builder()
                .title("Title")
                .content("Content")
                .topic(topic)
                .user(user)
                .responses(new HashSet<>(Collections.emptySet()))
                .userLikes(new HashSet<>(Collections.emptySet()))
                .build();
    }

    public PostLikesRelationEntity createValidLike(PostEntity post, UserEntity user) {
        return PostLikesRelationEntity.builder()
                .user(user)
                .post(post)
                .build();
    }

    public PostInputModel createValidPostModel(Long topicId, String username) {
        return PostInputModel.builder()
                .title("Title")
                .content("Content")
                .topic(topicId)
                .user(username)
                .build();
    }

    public PostLikesRelationModel createValidLikeModel(Long postId, String username) {
        return PostLikesRelationModel.builder()
                .user(username)
                .post(postId)
                .build();
    }

    public PostInputDTO createValidPost(Long topicId, String username) {
        return PostInputDTO.builder()
                .title("Title")
                .content("Content")
                .topicId(topicId)
                .email(username)
                .build();
    }

    public PostLikeDTO createValidLike(Long postId, String username) {
        return PostLikeDTO.builder()
                .email(username)
                .id(postId)
                .build();
    }
}
