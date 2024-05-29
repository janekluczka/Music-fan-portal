package org.andante.forum.logic.service.impl;

import exception.UserConflictException;
import exception.UserNotFoundException;
import org.andante.enums.OperationStatus;
import org.andante.forum.logic.mapper.UserModelEntityMapper;
import org.andante.forum.logic.model.UserModel;
import org.andante.forum.repository.UserRepository;
import org.andante.forum.repository.entity.UserEntity;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith({TestContainersExtension.class})
@Import({UserTestUtil.class})
@Transactional
public class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserModelEntityMapper userModelEntityMapper;
    @Autowired
    private UserTestUtil userTestUtil;

    @Nested
    @DisplayName("Create")
    class CreateTests {

        @Test
        void shouldCreateUser() {
            UserModel userToSave = userTestUtil.buildValidUserModel();

            String email = userToSave.getEmailAddress();
            String databaseResponse = userService.create(userToSave);

            assertThat(databaseResponse).isNotEmpty();
            assertThat(databaseResponse).isEqualTo(email);
        }

        @Test
        void shouldRaiseUserConflictWhenUserWithGivenUsernameExists() {
            UserEntity existingUser = userRepository.save(userTestUtil.buildValidUserEntity());
            String email = existingUser.getEmailAddress();

            UserModel userToAdd = UserModel.builder()
                    .username("Username")
                    .emailAddress("Username")
                    .name("Jane")
                    .surname("Doe")
                    .posts(Collections.emptySet())
                    .responses(Collections.emptySet())
                    .likedPosts(Collections.emptySet())
                    .likedResponses(Collections.emptySet())
                    .build();

            String serviceResponse = userService.create(userToAdd);
            Optional<UserEntity> databaseResponse = userRepository.findByEmail(email);

            assertThat(databaseResponse.isPresent()).isTrue();
            assertThat(databaseResponse.get().getName()).isNotEqualTo("Jane");
        }
    }

    @Nested
    @DisplayName("Get")
    class GetTests {

        @Test
        void shouldRaiseUserNotFound() {
            String email = "Username";

            assertThatThrownBy(() -> userService.get(email))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Delete")
    class DeleteTests {

        @Test
        void shouldDeleteGivenAllOtherCriteriaAreMet() {
            UserEntity existingUser = userRepository.save(UserEntity.builder()
                    .username("Username")
                    .emailAddress("Username")
                    .name("John")
                    .surname("Doe")
                    .build());
            String emailAddress = existingUser.getEmailAddress();

            OperationStatus serviceResponse = userService.delete(existingUser.getEmailAddress());

            assertThat(serviceResponse).isNotNull();
            assertThat(serviceResponse).isEqualTo(OperationStatus.OK);
            assertThat(userRepository.findByEmail(emailAddress)).isEmpty();
        }

        @Test
        void shouldNotDeleteGivenAllOtherCriteriaAreMet() {
            String emailAddress = "Username";
            OperationStatus serviceResponse = userService.delete(emailAddress);

            assertThat(serviceResponse).isNotNull();
            assertThat(serviceResponse).isEqualTo(OperationStatus.NOT_FOUND);
        }
    }
}
