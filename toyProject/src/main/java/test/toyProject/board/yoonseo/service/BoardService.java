package test.toyProject.board.yoonseo.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import test.toyProject.board.yoonseo.dto.BoardDTO;
import test.toyProject.board.yoonseo.entity.BoardEntity;
import test.toyProject.board.yoonseo.entity.BoardFileEntity;
import test.toyProject.board.yoonseo.repository.BoardRepository;
import test.toyProject.board.yoonseo.repository.BoardFileRepository;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    public void save(BoardDTO boardDTO) throws IOException{
        if (boardDTO.getBoardFile().isEmpty()) { //첨부파일이 비어있는 경우
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(boardEntity);
        } else { //첨부한 파일이 있는 경우
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId(); //저장 후 id값을 가져와 saveId에
            BoardEntity board = boardRepository.findById(savedId).get(); //부모 Entity를 DB로부터 가져옴


            for  (MultipartFile boardFile: boardDTO.getBoardFile()){
                // DTO에 있는 파일을 꺼내고
            String originalFilename = boardFile.getOriginalFilename(); // 파일 이름
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 서버에 저장할 이름 _
            String savePath = "C:/yoonseo_img/" + storedFileName; // 실제 C:/yoonseo_img/ 경로의 파일이 존재해야함.
            boardFile.transferTo(new File(savePath)); //저장
            BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
            boardFileRepository.save(boardFileEntity); }


        }

    }
    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity: boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }
    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }
    @Transactional
    public BoardDTO findById(Long id){
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }
    }


    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
    }

    public void delete(Long id) { boardRepository.deleteById(id);}

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1; //페이지 위치값은 0부터 시작한다는 것을 잊지말자!
        int pageLimit = 3;
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));


    System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
    System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
    System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
    System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
    System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
    System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
    System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
    System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부


    Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
   return boardDTOS;
}

}


