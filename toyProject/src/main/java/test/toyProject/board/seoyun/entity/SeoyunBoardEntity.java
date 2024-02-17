package test.toyProject.board.seoyun.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import test.toyProject.board.seoyun.dto.SeoyunBoardDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "seoyun_board_table")
public class SeoyunBoardEntity extends SeoyunBaseEntity{
    @Id // pk 컬럼 지정. 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(length = 20, nullable = false) // 크기 20, not null
    private String boardWriter;

    @Column // 크기 255, null 가능
    private String boardPass;

    @Column
    private String boardTitle;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;

    @Column
    private int fileAttached; // 1 or 0


    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SeoyunBoardFileEntity> boardFileEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SeoyunCommentEntity> commentEntityList = new ArrayList<>();

    public static SeoyunBoardEntity toSaveEntity(SeoyunBoardDTO boardDTO) {
        SeoyunBoardEntity boardEntity = new SeoyunBoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(0); // 파일 없음.
        return boardEntity;
    }

    public static SeoyunBoardEntity toSaveFileEntity(SeoyunBoardDTO boardDTO) {
        SeoyunBoardEntity boardEntity = new SeoyunBoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(1); // 파일 있음.
        return boardEntity;
    }

    public static SeoyunBoardEntity toUpdateEntity(SeoyunBoardDTO boardDTO) {
        SeoyunBoardEntity boardEntity = new SeoyunBoardEntity();
        // id가 있어야 update 쿼리 전달 가능
        boardEntity.setId(boardDTO.getId());
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        return boardEntity;
    }
}
