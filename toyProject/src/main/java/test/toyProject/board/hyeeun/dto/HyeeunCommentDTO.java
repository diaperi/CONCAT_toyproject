package test.toyProject.board.hyeeun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import test.toyProject.board.hyeeun.entity.HyeeunCommentEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class HyeeunCommentDTO {
    private Long id; // 댓글의 아이디값
    private String commentWriter; // 작성자
    private String commentContents; // 내용
    private Long boardId; // 게시글 번호
    private LocalDateTime commentCreatedTime; // 댓글 작성 시간

    /* entity를 dto로 변환 */
    public static HyeeunCommentDTO toCommentDTO(HyeeunCommentEntity commentEntity, Long boardId) {
        HyeeunCommentDTO commentDTO = new HyeeunCommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());
        // commentDTO.setBoardId(commentEntity.getBoardEntity().getId()); // Service 메서드에 @Transactional 반드시
        commentDTO.setBoardId(boardId);
        return commentDTO;
    }
}