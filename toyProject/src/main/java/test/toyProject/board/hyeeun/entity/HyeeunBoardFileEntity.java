package test.toyProject.board.hyeeun.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "board_file_table")
public class HyeeunBoardFileEntity extends HyeeunBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    @ManyToOne(fetch = FetchType.LAZY) // eager : DB에서 쓰던 안쓰던 다가져옴 / lazy : 필요한 옵션만 호출해서, 보통 많이 씀
    @JoinColumn(name = "board_id") // DB에 만들어지는 column 이름
    private HyeeunBoardEntity boardEntity;

    public static HyeeunBoardFileEntity toHyeeunBoardFileEntity(HyeeunBoardEntity boardEntity, String originalFileName, String storedFileName) {
        HyeeunBoardFileEntity boardFileEntity = new HyeeunBoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity);
        return boardFileEntity;
    }

}
