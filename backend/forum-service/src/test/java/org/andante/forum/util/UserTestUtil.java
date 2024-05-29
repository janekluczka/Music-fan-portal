package org.andante.forum.util;

import dto.UserDTO;
import org.andante.forum.logic.model.UserModel;
import org.andante.forum.repository.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;

@Component
public class UserTestUtil {

    public UserEntity buildValidUserEntity() {
        return UserEntity.builder()
                .username("Username")
                .emailAddress("Username")
                .name("John")
                .surname("Doe")
                .posts(new HashSet<>(Collections.emptySet()))
                .responses(new HashSet<>(Collections.emptySet()))
                .likedPosts(new HashSet<>(Collections.emptySet()))
                .likedResponses(new HashSet<>(Collections.emptySet()))
                .build();
    }

    public UserModel buildValidUserModel() {
        return UserModel.builder()
                .username("Username")
                .emailAddress("Username")
                .name("Jane")
                .surname("Doe")
                .posts(Collections.emptySet())
                .responses(Collections.emptySet())
                .likedPosts(Collections.emptySet())
                .likedResponses(Collections.emptySet())
                .build();
    }

    public UserDTO buildValidUserDTO() {
        return UserDTO.builder()
                .email("Username")
                .username("Username")
                .name("John")
                .surname("Doe")
                .build();
    }
}
