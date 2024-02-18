package test.toyProject.board.yoonseo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import test.toyProject.board.yoonseo.entity.YoonseoCommentEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class YoonseoCommentDTO {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static YoonseoCommentDTO toYoonseoCommentDTO(YoonseoCommentEntity commentEntity, Long boardId){
        YoonseoCommentDTO commentDTO = new YoonseoCommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCommentCreatedTime(commentEntity.getBoardEntity().getCreatedTime());
        commentDTO.setBoardId(boardId);
        return commentDTO;

    }

}
