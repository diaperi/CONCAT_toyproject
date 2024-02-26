package test.toyProject.board.yoonseo.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import test.toyProject.board.yoonseo.entity.YoonseoBoardEntity;
import test.toyProject.board.yoonseo.entity.YoonseoBoardFileEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//DTO 데이터를 전송할 때 사용.
@Getter
@Setter
@ToString
@NoArgsConstructor //기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class YoonseoBoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle; //조회수
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;
    //여러 개의 파일을 받을 수 있도록 하기위해 save.html에 multiple추가 후 List추가
    private List<MultipartFile> boardFile; //  파일첨부부분
    private List<String> originalFileName;
    private List<String> storedFileName;
    private int fileAttached; //파일 첨부여부를 1과 0으로 구분하지만 board/id url이 출력되지 않았을 때 추가하니 정상작동됨.

    public YoonseoBoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;

    }

    public static YoonseoBoardDTO toBoardDTO(YoonseoBoardEntity boardEntity) {
        YoonseoBoardDTO boardDTO = new YoonseoBoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());

        // 파일 첨부 여부에 따른 조건절 추가
        if (boardEntity.getFileAttached() == 0) { //파일을 첨부하지 않은 경우
            boardDTO.setFileAttached(boardEntity.getFileAttached()); // 0
        } else { //파일을 첨부한 경우
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            boardDTO.setFileAttached(boardEntity.getFileAttached()); // 1
            for( YoonseoBoardFileEntity boardFileEntity: boardEntity.getBoardFileEntityList()){
                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }

            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);
        }


        return boardDTO;


    }
}

