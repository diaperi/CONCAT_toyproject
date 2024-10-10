package test.toyProject.board.yuna.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import test.toyProject.board.yuna.entity.YunaCommentEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class YunaCommentDTO {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static YunaCommentDTO toCommentDTO(YunaCommentEntity commentEntity, Long boardId) {
        YunaCommentDTO commentDTO = new YunaCommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());
        commentDTO.setBoardId(boardId);
        return commentDTO;
    }
}

