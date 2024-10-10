package test.toyProject.board.yoonseo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import test.toyProject.board.yoonseo.dto.YoonseoCommentDTO;

@Entity
@Getter
@Setter
@Table(name="yoonseo_comment_table")
public class YoonseoCommentEntity extends YoonseoBaseEntity{
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
    private YoonseoBoardEntity boardEntity;

    public static YoonseoCommentEntity toSaveEntity(YoonseoCommentDTO commentDTO, YoonseoBoardEntity boardEntity) {
        YoonseoCommentEntity commentEntity = new YoonseoCommentEntity();
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        commentEntity.setBoardEntity(boardEntity);
        return commentEntity;
    }

}
