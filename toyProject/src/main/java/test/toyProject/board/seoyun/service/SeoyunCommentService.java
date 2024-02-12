package test.toyProject.board.seoyun.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.toyProject.board.seoyun.dto.SeoyunCommentDTO;
import test.toyProject.board.seoyun.entity.SeoyunBoardEntity;
import test.toyProject.board.seoyun.entity.SeoyunCommentEntity;
import test.toyProject.board.seoyun.repository.SeoyunBoardRepository;
import test.toyProject.board.seoyun.repository.SeoyunCommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeoyunCommentService {

    private final SeoyunCommentRepository commentRepository;
    private final SeoyunBoardRepository boardRepository;

    public List<SeoyunCommentDTO> findAll(Long boardId) {
        SeoyunBoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<SeoyunCommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);

        List<SeoyunCommentDTO> commentDTOList = new ArrayList<>();
        for (SeoyunCommentEntity commentEntity : commentEntityList) {
            SeoyunCommentDTO commentDTO = SeoyunCommentDTO.toSeoyunCommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }

    public Long save(SeoyunCommentDTO commentDTO) {
        /* 부모엔티티(BoardEntity) 조회 */
        Optional<SeoyunBoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if (optionalBoardEntity.isPresent()) {
            SeoyunBoardEntity boardEntity = optionalBoardEntity.get();
            SeoyunCommentEntity commentEntity = SeoyunCommentEntity.toSaveEntity(commentDTO, boardEntity);
            return commentRepository.save(commentEntity).getId();
        } else {
            return null;
        }
    }
}
