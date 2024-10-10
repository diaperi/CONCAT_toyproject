package test.toyProject.board.seoyun.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import test.toyProject.board.seoyun.dto.SeoyunBoardDTO;
import test.toyProject.board.seoyun.entity.SeoyunBoardEntity;
import test.toyProject.board.seoyun.entity.SeoyunBoardFileEntity;
import test.toyProject.board.seoyun.repository.SeoyunBoardFileRepository;
import test.toyProject.board.seoyun.repository.SeoyunBoardRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeoyunBoardService {

    private final SeoyunBoardRepository boardRepository;
    private final SeoyunBoardFileRepository boardFileRepository;

    public void save(SeoyunBoardDTO boardDTO) throws IOException {

        // 파일 첨부 여부에 따라 로직 분리
        if (boardDTO.getBoardFile().isEmpty()) {
            // 첨부 파일 없음.
            SeoyunBoardEntity boardEntity = SeoyunBoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(boardEntity);
        } else {
            // 첨부 파일 있음.
            /*
                1. DTO에 담긴 파일을 꺼냄
                2. 파일의 이름 가져옴
                3. 서버 저장용 이름을 만듦
                // 내사진.jpg => 839798375892_내사진.jpg
                4. 저장 경로 설정
                5. 해당 경로에 파일 저장
                6. board_table에 해당 데이터 save 처리
                7. board_file_table에 해당 데이터 save 처리
             */
            SeoyunBoardEntity boardEntity = SeoyunBoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId();
            SeoyunBoardEntity board = boardRepository.findById(savedId).get();

            for(MultipartFile boardFile: boardDTO.getBoardFile()) {
//                  MultipartFile boardFile = boardDTO.getBoardFile(); // 1.
                String originalFilename = boardFile.getOriginalFilename(); // 2.
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 3.
                String savePath = "C:/SpringBootStudy/CONCAT_toyproject/boardImg/" + storedFileName; // 4. C:/SpringBootStudy/SpringBootBoard/boardImg/9802398403948_내사진.jpg
//                  Mac : String savePath = "/Users/사용자이름/springboot_img/" + storedFileName; // C:/springboot_img/9802398403948_내사진.jpg
                boardFile.transferTo(new File(savePath)); // 5.

                SeoyunBoardFileEntity boardFileEntity = SeoyunBoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }
        }
    }


    @Transactional
    public List<SeoyunBoardDTO> findAll() {
        List<SeoyunBoardEntity> boardEntityList = boardRepository.findAll();
        // 모든 boardEntity를 가져온 후에 boardFileEntityList를 로드
        for (SeoyunBoardEntity boardEntity : boardEntityList) {
            Hibernate.initialize(boardEntity.getBoardFileEntityList());
        }

        // 모든 boardDTO를 생성하여 반환
        return boardEntityList.stream()
                .map(SeoyunBoardDTO::toBoardDTO)
                .collect(Collectors.toList());
    }

    public int getTotalPostCount() {
        return boardRepository.getTotalPostCount();
    }


    @Transactional
    public SeoyunBoardDTO findById(Long id) {
        Optional<SeoyunBoardEntity> optionalSeoyunBoardEntity = boardRepository.findById(id);
        if(optionalSeoyunBoardEntity.isPresent()){
            SeoyunBoardEntity boardEntity = optionalSeoyunBoardEntity.get();
            SeoyunBoardDTO boardDTO = SeoyunBoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        }else{
            return null;
        }
    }

    public SeoyunBoardDTO update(SeoyunBoardDTO boardDTO) {
        SeoyunBoardEntity boardEntity = SeoyunBoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }


    public Page<SeoyunBoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10; // 한 페이지에 보여줄 글 갯수
        // 한페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작
        Page<SeoyunBoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        // 목록: id, writer, title, hits, createdTime
        Page<SeoyunBoardDTO> boardDTOS = boardEntities.map(board -> new SeoyunBoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
        return boardDTOS;
    }
}
