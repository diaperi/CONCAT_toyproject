package test.toyProject.board.yuna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.toyProject.board.yuna.dto.YunaCommentDTO;
import test.toyProject.board.yuna.entity.YunaBoardEntity;
import test.toyProject.board.yuna.entity.YunaCommentEntity;
import test.toyProject.board.yuna.repository.YunaBoardRepository;
import test.toyProject.board.yuna.repository.YunaCommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YunaCommentService {
    private final YunaCommentRepository commentRepository;
    private final YunaBoardRepository boardRepository;

    public Long save(YunaCommentDTO commentDTO) {
        /* 부모엔티티(BoardEntity) 조회 */
        Optional<YunaBoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if (optionalBoardEntity.isPresent()) {
            YunaBoardEntity boardEntity = optionalBoardEntity.get();
            YunaCommentEntity commentEntity = YunaCommentEntity.toSaveEntity(commentDTO, boardEntity);
            return commentRepository.save(commentEntity).getId();
        } else {
            return null;
        }
    }

    public List<YunaCommentDTO> findAll(Long boardId) {
        YunaBoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<YunaCommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        /* EntityList -> DTOList */
        List<YunaCommentDTO> commentDTOList = new ArrayList<>();
        for (YunaCommentEntity commentEntity: commentEntityList) {
            YunaCommentDTO commentDTO = YunaCommentDTO.toCommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}

