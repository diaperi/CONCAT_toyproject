package test.toyProject.board.hyeeun.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import test.toyProject.board.hyeeun.dto.HyeeunBoardDTO;

import java.util.ArrayList;
import java.util.List;

// DB의 테이블 역할을 하는 클래스
@Entity
@Getter
@Setter
@Table(name = "hyeeun_board_table") // 특정 테이블 이름을 따로 주고 싶다면 사용
public class HyeeunBoardEntity extends HyeeunBaseEntity {
    @Id // pk 컬럼 지정. 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(length = 20, nullable = false) // 크기는 20이고, null 일 수 없다.
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
    private List<HyeeunBoardFileEntity> boardFileEntityList = new ArrayList<>(); // table 만듬

    // 부모가 삭제되면 자식도 함께 삭제됨
    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HyeeunCommentEntity> commentEntityList = new ArrayList<>();

    public static HyeeunBoardEntity toSaveEntity(HyeeunBoardDTO boardDTO) {
        HyeeunBoardEntity boardEntity = new HyeeunBoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(0); // 파일 없음.
        return boardEntity;
        // DTO에 담긴 객체를 entity 객체로 옮겨 담는 작업
    } // static 형태의 메서드로 정의


    public static HyeeunBoardEntity toSaveFileEntity(HyeeunBoardDTO boardDTO) {
        HyeeunBoardEntity boardEntity = new HyeeunBoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(0); // 파일 있음.
        return boardEntity;
    }

    public static HyeeunBoardEntity toUpdateEntity(HyeeunBoardDTO boardDTO) {
        HyeeunBoardEntity boardEntity = new HyeeunBoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        return boardEntity;
    }
} // BoardEntity가 BaseEntity를 상속받음
