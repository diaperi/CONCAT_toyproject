package test.toyProject.board.yuna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.toyProject.board.yuna.entity.YunaBoardFileEntity;

public interface YunaBoardFileRepository extends JpaRepository<YunaBoardFileEntity, Long> {
}
