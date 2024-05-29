package org.andante.forum.logic.service.impl;

import exception.TopicNotFoundException;
import exception.UserNotFoundException;
import lombok.SneakyThrows;
import org.andante.forum.logic.model.post.PostInputModel;
import org.andante.forum.logic.model.post.PostLikesRelationModel;
import org.andante.forum.repository.PostRepository;
import org.andante.forum.repository.TopicRepository;
import org.andante.forum.repository.UserRepository;
import org.andante.forum.repository.entity.PostEntity;
import org.andante.forum.repository.entity.TopicEntity;
import org.andante.forum.repository.entity.UserEntity;
import org.andante.forum.util.PostTestUtil;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith({TestContainersExtension.class})
@Import({PostTestUtil.class, TopicTestUtil.class, UserTestUtil.class})
@Transactional
public class PostServiceTest {

    @Autowired
    private PostServiceImpl postService;
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

    @Nested
    @DisplayName("Create")
    class CreateTests {

        @Test
        @SneakyThrows
        @DisplayName("should return bad request for empty input")
        void validCreate() {
            TopicEntity topic = topicRepository.save(topicTestUtil.buildValidParentTopicEntity());
            UserEntity user = userRepository.save(userTestUtil.buildValidUserEntity());

            PostInputModel post = postTestUtil.createValidPostModel(topic.getId(), user.getUsername());

            postService.create(post);
        }

        @Test
        @SneakyThrows
        @DisplayName("should return bad request for empty input")
        void invalidTopic() {
            TopicEntity topic = topicRepository.save(topicTestUtil.buildValidParentTopicEntity());
            UserEntity user = userRepository.save(userTestUtil.buildValidUserEntity());

            PostInputModel post = postTestUtil.createValidPostModel(topic.getId() + 1L, user.getUsername());

            assertThatThrownBy(() -> postService.create(post))
                    .isInstanceOf(TopicNotFoundException.class);
        }

        @Test
        @SneakyThrows
        @DisplayName("should return bad request for empty input")
        void invalidUser() {
            TopicEntity topic = topicRepository.save(topicTestUtil.buildValidParentTopicEntity());
            UserEntity user = userRepository.save(userTestUtil.buildValidUserEntity());

            PostInputModel post = postTestUtil.createValidPostModel(topic.getId(), user.getUsername() + "123");

            assertThatThrownBy(() -> postService.create(post))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Like")
    class LikeTests {

        @Test
        @SneakyThrows
        @DisplayName("valid Like")
        void validLike() {
            TopicEntity topic = topicRepository.save(topicTestUtil.buildValidParentTopicEntity());
            UserEntity user = userRepository.save(userTestUtil.buildValidUserEntity());
            PostEntity post = postRepository.save(postTestUtil.createValidPostEntity(topic, user));

            PostLikesRelationModel like = postTestUtil.createValidLikeModel(post.getId(), user.getUsername());

            postService.likePost(like);

        }
    }

}
