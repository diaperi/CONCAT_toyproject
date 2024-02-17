package test.toyProject.board.seoyun.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import test.toyProject.board.seoyun.dto.SeoyunCommentDTO;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "seoyun_comment_table")
public class SeoyunCommentEntity extends SeoyunBaseEntity{
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
    private SeoyunBoardEntity boardEntity;

    public static SeoyunCommentEntity toSaveEntity(SeoyunCommentDTO commentDTO, SeoyunBoardEntity boardEntity) {
        SeoyunCommentEntity commentEntity = new SeoyunCommentEntity();
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        commentEntity.setBoardEntity(boardEntity);
        return commentEntity;
    }

}
