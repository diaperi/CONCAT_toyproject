package test.toyProject.board.yoonseo.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import test.toyProject.board.yoonseo.dto.YoonseoBoardDTO;
import test.toyProject.board.yoonseo.entity.YoonseoBoardEntity;
import test.toyProject.board.yoonseo.entity.YoonseoBoardFileEntity;
import test.toyProject.board.yoonseo.repository.YoonseoBoardRepository;
import test.toyProject.board.yoonseo.repository.YoonseoBoardFileRepository;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YoonseoBoardService {
    private final YoonseoBoardRepository boardRepository;
    private final YoonseoBoardFileRepository boardFileRepository;

    public void save(YoonseoBoardDTO boardDTO) throws IOException{
        if (boardDTO.getBoardFile().isEmpty()) { //첨부파일이 비어있는 경우
            YoonseoBoardEntity boardEntity = YoonseoBoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(boardEntity);
        } else { //첨부한 파일이 있는 경우
            YoonseoBoardEntity boardEntity = YoonseoBoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId(); //저장 후 id값을 가져와 saveId에
            YoonseoBoardEntity board = boardRepository.findById(savedId).get(); //부모 Entity를 DB로부터 가져옴


            for  (MultipartFile boardFile: boardDTO.getBoardFile()){
                // DTO에 있는 파일을 꺼내고
            String originalFilename = boardFile.getOriginalFilename(); // 파일 이름
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 서버에 저장할 이름 _
            String savePath = "C:/SpringBootStudy/CONCAT_toyproject/boardImg/" + storedFileName; // 실제 C:/yoonseo_img/ 경로의 파일이 존재해야함.
            boardFile.transferTo(new File(savePath)); //저장
            YoonseoBoardFileEntity boardFileEntity = YoonseoBoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
            boardFileRepository.save(boardFileEntity); }


        }

    }
    @Transactional
    public List<YoonseoBoardDTO> findAll() {
        List<YoonseoBoardEntity> boardEntityList = boardRepository.findAll();
        List<YoonseoBoardDTO> boardDTOList = new ArrayList<>();
        for (YoonseoBoardEntity boardEntity: boardEntityList) {
            boardDTOList.add(YoonseoBoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }
    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }
    @Transactional
    public YoonseoBoardDTO findById(Long id){
        Optional<YoonseoBoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            YoonseoBoardEntity boardEntity = optionalBoardEntity.get();
            YoonseoBoardDTO boardDTO = YoonseoBoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }
    }


    public YoonseoBoardDTO update(YoonseoBoardDTO boardDTO) {
        YoonseoBoardEntity boardEntity = YoonseoBoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
    }

    public void delete(Long id) { boardRepository.deleteById(id);}

    public Page<YoonseoBoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1; //페이지 위치값은 0부터 시작한다는 것을 잊지말자!
        int pageLimit = 3;
        Page<YoonseoBoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));


    System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
    System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
    System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
    System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
    System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
    System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
    System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
    System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부


    Page<YoonseoBoardDTO> boardDTOS = boardEntities.map(board -> new YoonseoBoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
   return boardDTOS;
}

}


