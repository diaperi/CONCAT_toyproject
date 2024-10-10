package test.toyProject.board.hyeeun.dto;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.web.multipart.MultipartFile;
import test.toyProject.board.hyeeun.entity.HyeeunBoardEntity;
import test.toyProject.board.hyeeun.entity.HyeeunBoardFileEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// DTO(Data Transfer Object), VO, Bean 무언가를 주고받을 때 파라미터들을 하나의 객체에 담아 주고 받도록함
// Entity
@Getter //get 메서드를 자동으로 생성
@Setter //set 메서드를 자동으로 생성
@ToString //필드값 확인시 사용
@NoArgsConstructor //기본생성자
@AllArgsConstructor //모든 필드를 매개변수로 하는 생성자
public class HyeeunBoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits; //조회수
    private LocalDateTime boardCreatedTime; //게시글 작성 시간
    private LocalDateTime boardUpdatedTime; //게시글 수정 시간

    private List<MultipartFile> boardFile; // save.html -> Controller 파일 담는 용도, List<MultipartFile> -> 여러개의 파일
    private List<String> originalFileName; // 원본 파일 이름
    private List<String> storedFileName; // 서버 저장용 파일 이름
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)

    public HyeeunBoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    public static HyeeunBoardDTO toBoardDTO(HyeeunBoardEntity boardEntity) {
        HyeeunBoardDTO boardDTO = new HyeeunBoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());
        if(boardEntity.getFileAttached()==0){
            boardDTO.setFileAttached(boardEntity.getFileAttached()); // 0
        }else{
            // 세션을 열어서 boardFileEntityList를 로드
            Hibernate.initialize(boardEntity.getBoardFileEntityList());
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            boardDTO.setFileAttached(boardEntity.getFileAttached()); // 1
            for(HyeeunBoardFileEntity boardFileEntity: boardEntity.getBoardFileEntityList()){
                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }
            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);
        }
        return boardDTO;
    }
}

