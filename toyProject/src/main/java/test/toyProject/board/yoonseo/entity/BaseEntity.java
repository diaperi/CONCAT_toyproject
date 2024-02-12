package test.toyProject.board.yoonseo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter

public class BaseEntity {
    @CreationTimestamp //생성시간
    @Column(updatable = false) // 수정시 관여X
    private LocalDateTime createdTime;


    @UpdateTimestamp// 업데이트 되었을 때
    @Column(insertable = false) // 입력시 관여X
    private LocalDateTime updatedTime;
}
