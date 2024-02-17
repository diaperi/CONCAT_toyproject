package test.toyProject.board.hyeeun.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.toyProject.board.hyeeun.dto.HyeeunCommentDTO;
import test.toyProject.board.hyeeun.entity.HyeeunBoardEntity;
import test.toyProject.board.hyeeun.entity.HyeeunCommentEntity;
import test.toyProject.board.hyeeun.repository.HyeeunBoardRepository;
import test.toyProject.board.hyeeun.repository.HyeeunCommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HyeeunCommentService {
    private final HyeeunCommentRepository commentRepository; // CommentRepository 주입받음
    private final HyeeunBoardRepository boardRepository; // BoardRepository 주입받음 (comment 의 부모 entity)

    public Long save(HyeeunCommentDTO commentDTO) {
        /* 부모엔티티(BoardEntity) 조회*/
        Optional<HyeeunBoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if(optionalBoardEntity.isPresent()) {
            // 부모 엔티티가 조회가 된다면 댓글 저장 처리
            HyeeunBoardEntity boardEntity = optionalBoardEntity.get();
            HyeeunCommentEntity commentEntity = HyeeunCommentEntity.toSaveEntity(commentDTO, boardEntity); // dto로 받아온 것을 entity로 변환
            return commentRepository.save(commentEntity).getId();
        } else {
            // 부모 엔티티가 조회 되지 않는다면 null을 리턴해서 controller에서 그에 대한 응답을 주자
            return null;
        }
    }

    public List<HyeeunCommentDTO> findAll(Long boardId) {
        // select * from comment_table where board_id=? order by id desc;
        HyeeunBoardEntity boardEntity = boardRepository.findById(boardId).get(); // 부모인 BoardEntity 조회하고, get으로 바로 꺼냄
        List<HyeeunCommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity); // 우리가 만든 메서드 호출
        /* EntityList -> DTOList(변환하면서 리스트에다가 하나씩 하나씩 옮겨 담음) */
        List<HyeeunCommentDTO> commentDTOList = new ArrayList<>();
        for (HyeeunCommentEntity commentEntity: commentEntityList) {
            HyeeunCommentDTO commentDTO = HyeeunCommentDTO.toCommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}
