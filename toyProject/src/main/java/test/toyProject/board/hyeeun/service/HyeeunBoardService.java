package test.toyProject.board.hyeeun.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import test.toyProject.board.hyeeun.dto.HyeeunBoardDTO;
import test.toyProject.board.hyeeun.entity.HyeeunBoardEntity;
import test.toyProject.board.hyeeun.entity.HyeeunBoardFileEntity;
import test.toyProject.board.hyeeun.repository.HyeeunBoardFileRepository;
import test.toyProject.board.hyeeun.repository.HyeeunBoardRepository;
import test.toyProject.board.seoyun.dto.SeoyunBoardDTO;
import test.toyProject.board.seoyun.entity.SeoyunBoardEntity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HyeeunBoardService {
    private final HyeeunBoardRepository boardRepository; // repository 를 주입받도록 생성자 주입 방식 사용
    private final HyeeunBoardFileRepository boardFileRepository; // repository 를 주입받도록 생성자 주입 방식 사용

    public void save(HyeeunBoardDTO boardDTO) throws IOException {
        // 파일 첨부 여부에 따라 로직 분리
        if(boardDTO.getBoardFile().isEmpty()) {
            // dto 에 첨부 파일이 없음
            HyeeunBoardEntity boardEntity = HyeeunBoardEntity.toSaveEntity(boardDTO); // 메서드를 호출한 결과를 entity로 받아올 수 있고 결국 entity를 save 메서드로 넘겨주게됨
            boardRepository.save(boardEntity); // repository는 기본적으로 entity 타입으로만 받아준다.
        } else {
            // dto 에 첨부 파일이 있음
            /*
                1. DTO에 담긴 파일을 꺼냄
                2. 파일의 이름 가져옴
                3. 서버 저장용 이름으로 수정
                // 내사진.jpg => 840345924_내사진.jpg
                4. 저장 경로 설정
                5. 해당 경로에 파일 저장
                6. board_table에 해당 데이터 save 처리
                7. board_file_table에 해당 데이터 save 처리
             */
            HyeeunBoardEntity boardEntity = HyeeunBoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId();
            HyeeunBoardEntity board = boardRepository.findById(savedId).get();

            for(MultipartFile boardFile : boardDTO.getBoardFile()) {
                // MultipartFile boardFile = boardDTO.getBoardFile(); // 1.
                String originalFileName = boardFile.getOriginalFilename(); // 2.
                // System.currentTimeMillis() : 1970년 1월 1일을 기준으로 해서 현재가 몇 ms나 지났느냐 /
                String storedFileName = System.currentTimeMillis() + "_" + originalFileName; // 3.
                String savePath = "C:/SpringBootStudy/CONCAT_toyproject/boardImg/" + storedFileName; // 4. C:/springboot_img/840345924_내사진.jpg
                boardFile.transferTo(new File(savePath)); // 5. file을 transferTo로 넘긴다

                HyeeunBoardFileEntity boardFileEntity = HyeeunBoardFileEntity.toHyeeunBoardFileEntity(board, originalFileName, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }
        }

    }

    @Transactional
    public List<HyeeunBoardDTO> findAll() {
        List<HyeeunBoardEntity> boardEntityList = boardRepository.findAll();
        // 모든 boardEntity를 가져온 후에 boardFileEntityList를 로드
        for (HyeeunBoardEntity boardEntity : boardEntityList) {
            Hibernate.initialize(boardEntity.getBoardFileEntityList());
        }// 반복문을 돌린다음 dto 객체에 하나씩 담음. entity 객체를 dto로 변환하고 변환된 객체를 boardDTOList 에 담는다.
        // 모든 boardDTO를 생성하여 반환
        return boardEntityList.stream()
                .map(HyeeunBoardDTO::toBoardDTO)
                .collect(Collectors.toList());
    }
    @Transactional // 별도로 추가된 메서드를 쓰는 경우 필수
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public HyeeunBoardDTO findById(Long id) {
        Optional<HyeeunBoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if(optionalBoardEntity.isPresent()) {
            HyeeunBoardEntity boardEntity = optionalBoardEntity.get();
            HyeeunBoardDTO boardDTO = HyeeunBoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }
    }

    public HyeeunBoardDTO update(HyeeunBoardDTO boardDTO) {
        HyeeunBoardEntity boardEntity = HyeeunBoardEntity.toUpdateEntity(boardDTO); // entity 객체로 변환
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<HyeeunBoardDTO> paging(Pageable pageable) {
        // repository에서 가져옴
        int page = pageable.getPageNumber()-1;
        int pageLimit = 10; // 한 페이지에 보여줄 글 개수
        // page : 몇 페이지를 보고 싶은지, page 위치에 있는 값은 0부터 시작
        // pageLimit : 한 페이지에 몇개
        // Sort.by(Sort.Direction.DESC, "id") : sorting 기준, 어떻게 정렬을 해서 가져 올거냐, "id" -> entity에 작성한 이름 기준
        // => 한페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        Page<HyeeunBoardEntity>boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        // 목록 : id, writer, title, hits, createdTime
        Page<HyeeunBoardDTO> boardDTOS = boardEntities.map(board -> new HyeeunBoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime())); // map 안에서 하나씩 뽑아서 dto 객체로 옮겨담음
        return boardDTOS;
    }

    public int getTotalPostCount() {
        return boardRepository.getTotalPostCount();
    }
}

