package test.toyProject.board.hyeeun.entity;

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
public class HyeeunBaseEntity {
    @CreationTimestamp // 생성됬을 때 시간
    @Column(updatable = false) // 수정시에는 관여 안하도록
    private LocalDateTime createdTime;

    @UpdateTimestamp // 수정됬을 때 시간
    @Column(insertable = false) // insert를 할 때(입력시)는 관여 안하도록
    private LocalDateTime updatedTime;
}
