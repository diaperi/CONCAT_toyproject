package test.toyProject.board.yuna.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import test.toyProject.board.yuna.dto.YunaCommentDTO;

@Entity
@Getter
@Setter
@Table(name = "yuna_comment_table")
public class YunaCommentEntity extends YunaBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String commentWriter;

    @Column
    private String commentContents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private YunaBoardEntity boardEntity;


    public static YunaCommentEntity toSaveEntity(YunaCommentDTO commentDTO, YunaBoardEntity boardEntity) {
        YunaCommentEntity commentEntity = new YunaCommentEntity();
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        commentEntity.setBoardEntity(boardEntity);
        return commentEntity;
    }
}

