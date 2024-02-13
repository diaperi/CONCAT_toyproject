package test.toyProject.board.yoonseo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


//board_table은 부모테이블, board_file_table은 자식 테이블, 게시글 하나에는 여러개의 파일이 들어갈 수 있음!
@Entity

@Getter

@Setter

@Table(name="yoonseo_board_file_table")
public class YoonseoBoardFileEntity extends YoonseoBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private String originalFileName;


    @Column
    private String storedFileName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id") // 실제 db에 저장되는 것
    private YoonseoBoardEntity boardEntity; // *baoardEntity로 들어가야함!


    public static YoonseoBoardFileEntity toBoardFileEntity(YoonseoBoardEntity boardEntity, String originalFileName, String storedFileName) {
        YoonseoBoardFileEntity boardFileEntity = new YoonseoBoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity);
        return boardFileEntity;
    }



}
