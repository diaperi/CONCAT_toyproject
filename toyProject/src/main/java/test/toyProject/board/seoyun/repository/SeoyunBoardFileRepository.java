package test.toyProject.board.seoyun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.toyProject.board.seoyun.entity.SeoyunBoardFileEntity;

public interface SeoyunBoardFileRepository extends JpaRepository<SeoyunBoardFileEntity, Long> {
}
