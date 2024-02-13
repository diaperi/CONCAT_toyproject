package test.toyProject.board.yuna.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "yuna_board_file_table")
public class YunaBoardFileEntity extends YunaBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private YunaBoardEntity boardEntity;

    public static YunaBoardFileEntity toBoardFileEntity(YunaBoardEntity boardEntity, String originalFileName, String storedFileName) {
        YunaBoardFileEntity boardFileEntity = new YunaBoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity);
        return boardFileEntity;
    }
}

