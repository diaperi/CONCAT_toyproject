package test.toyProject.board.yuna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.toyProject.board.yuna.entity.BoardFileEntity;

public interface BoardFileRepository extends JpaRepository<BoardFileEntity, Long> {
}
