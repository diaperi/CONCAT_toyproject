package test.toyProject.like.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {

    private Long id;
    private Long seoyunBoardId;
    private Long hyeeunBoardId;
    private Long yoonseoBoardId;
    private Long yunaBoardId;
    private Long userId;
}
