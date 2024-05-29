package dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostInputDTO {

    private Long id;

    @NotEmpty(message = "Post title must not be empty")
    private String title;

    @NotEmpty(message = "Post content must not be empty")
    private String content;

    @NotNull(message = "Post must be assigned to topic")
    private Long topicId;

    @NotEmpty(message = "Post must be assigned to user")
    private String email;
}
