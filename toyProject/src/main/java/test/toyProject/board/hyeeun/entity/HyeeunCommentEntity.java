package test.toyProject.board.hyeeun.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import test.toyProject.board.hyeeun.dto.HyeeunCommentDTO;

@Entity
@Getter
@Setter
@Table(name = "comment_table")
// comment의 자식
public class HyeeunCommentEntity  extends HyeeunBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String commentWriter;

    @Column
    private String commentContents;

    /* Board:Comment = 1:N */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private HyeeunBoardEntity boardEntity;

    public static HyeeunCommentEntity toSaveEntity(HyeeunCommentDTO commentDTO, HyeeunBoardEntity boardEntity) {
        HyeeunCommentEntity commentEntity = new HyeeunCommentEntity(); // save entity 만들기
        // 자식 데이터를 저장할 때는 부모 entity가 필요
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        // 부모도 조회
        commentEntity.setBoardEntity(boardEntity);
        return commentEntity;
    }
}

