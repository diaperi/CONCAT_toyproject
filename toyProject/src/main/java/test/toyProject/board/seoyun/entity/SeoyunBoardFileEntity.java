package test.toyProject.board.seoyun.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board_file_table")
public class SeoyunBoardFileEntity extends SeoyunBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "stored_file_name")
    private String storedFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private SeoyunBoardEntity boardEntity;

    public static SeoyunBoardFileEntity toBoardFileEntity(SeoyunBoardEntity boardEntity, String originalFilename, String storedFileName) {
        SeoyunBoardFileEntity boardFileEntity = new SeoyunBoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFilename);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity);
        return boardFileEntity;
    }
}
