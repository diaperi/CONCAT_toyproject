package test.toyProject.board.yoonseo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.toyProject.board.seoyun.dto.SeoyunCommentDTO;
import test.toyProject.board.seoyun.entity.SeoyunBoardEntity;
import test.toyProject.board.seoyun.entity.SeoyunCommentEntity;
import test.toyProject.board.yoonseo.dto.YoonseoCommentDTO;
import test.toyProject.board.yoonseo.entity.YoonseoBoardEntity;
import test.toyProject.board.yoonseo.entity.YoonseoCommentEntity;
import test.toyProject.board.yoonseo.repository.YoonseoBoardRepository;
import test.toyProject.board.yoonseo.repository.YoonseoCommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YoonseoCommentService {
    private final YoonseoCommentRepository commentRepository;
    private final YoonseoBoardRepository boardRepository;

    public Long save(YoonseoCommentDTO commentDTO) {
        /* 부모엔티티(BoardEntity) 조회 */
        Optional<YoonseoBoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if (optionalBoardEntity.isPresent()) {
            YoonseoBoardEntity boardEntity = optionalBoardEntity.get();
            YoonseoCommentEntity commentEntity = YoonseoCommentEntity.toSaveEntity(commentDTO, boardEntity);
            return commentRepository.save(commentEntity).getId();
        } else {
            return null;
        }
    }

    public List<YoonseoCommentDTO> findAll(Long boardId) {
        YoonseoBoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<YoonseoCommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);

        List<YoonseoCommentDTO> commentDTOList = new ArrayList<>();
        for (YoonseoCommentEntity commentEntity : commentEntityList) {
            YoonseoCommentDTO commentDTO = YoonseoCommentDTO.toYoonseoCommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }


    @Transactional
    public void deleteByYoonseoBoardId(Long id) {
        commentRepository.deleteByBoardEntity_Id(id);
    }

}


