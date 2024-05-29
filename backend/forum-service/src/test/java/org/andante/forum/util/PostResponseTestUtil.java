package org.andante.forum.util;

import dto.response.PostResponseInputDTO;
import dto.response.PostResponseLikeDTO;
import org.andante.forum.logic.model.response.PostResponseInputModel;
import org.andante.forum.logic.model.response.PostResponsesLikesRelationModel;
import org.andante.forum.repository.entity.PostEntity;
import org.andante.forum.repository.entity.PostResponseEntity;
import org.andante.forum.repository.entity.PostResponsesLikesRelationEntity;
import org.andante.forum.repository.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;

@Component
public class PostResponseTestUtil {

    public PostResponseEntity createValidPostResponseEntity(PostEntity post, UserEntity user) {
        return PostResponseEntity.builder()
                .content("Content")
                .post(post)
                .user(user)
                .likedByUsers(new HashSet<>(Collections.emptySet()))
                .build();
    }

    public PostResponsesLikesRelationEntity createValidPostResponseLikeEntity(PostResponseEntity response, UserEntity user) {
        return PostResponsesLikesRelationEntity.builder()
                .response(response)
                .user(user)
                .build();
    }

    public PostResponseInputModel createValidPostResponseModel(Long postId, String username) {
        return PostResponseInputModel.builder()
                .content("Content")
                .post(postId)
                .user(username)
                .build();
    }

    public PostResponsesLikesRelationModel createValidPostResponseLikeModel(Long responseId, String username) {
        return PostResponsesLikesRelationModel.builder()
                .response(responseId)
                .email(username)
                .build();
    }

    public PostResponseInputDTO createValidPostResponse(Long postId, String username) {
        return PostResponseInputDTO.builder()
                .content("Content")
                .postId(postId)
                .email(username)
                .build();
    }

    public PostResponseLikeDTO createValidPostResponseLike(Long responseId, String username) {
        return PostResponseLikeDTO.builder()
                .id(responseId)
                .email(username)
                .build();
    }
}
