package test.toyProject.board.yoonseo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.toyProject.board.yoonseo.entity.BoardFileEntity;

public interface BoardFileRepository extends JpaRepository<BoardFileEntity,Long> {

}
