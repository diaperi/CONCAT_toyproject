package test.toyProject.board.seoyun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import test.toyProject.board.seoyun.entity.SeoyunCommentEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class SeoyunCommentDTO {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static SeoyunCommentDTO toSeoyunCommentDTO(SeoyunCommentEntity commentEntity, Long boardId) {
        SeoyunCommentDTO commentDTO = new SeoyunCommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());
        commentDTO.setBoardId(boardId);
        return commentDTO;
    }
}
