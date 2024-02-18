package test.toyProject.like.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import test.toyProject.board.hyeeun.entity.HyeeunBoardEntity;
import test.toyProject.board.seoyun.entity.SeoyunBoardEntity;
import test.toyProject.board.yoonseo.entity.YoonseoBoardEntity;
import test.toyProject.board.yuna.entity.YunaBoardEntity;
import test.toyProject.user.entity.UserEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "like_table")
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seoyun_board_id")
    private SeoyunBoardEntity seoyunBoardEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hyeeun_board_id")
    private HyeeunBoardEntity hyeeunBoardEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yoonseo_board_id")
    private YoonseoBoardEntity yoonseoBoardEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yuna_board_id")
    private YunaBoardEntity yunaBoardEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;


    public LikeEntity(SeoyunBoardEntity seoyunBoardEntity, HyeeunBoardEntity hyeeunBoardEntity,
                      YoonseoBoardEntity yoonseoBoardEntity, YunaBoardEntity yunaBoardEntity,
                      UserEntity userEntity) {
        this.seoyunBoardEntity = seoyunBoardEntity;
        this.hyeeunBoardEntity = hyeeunBoardEntity;
        this.yoonseoBoardEntity = yoonseoBoardEntity;
        this.yunaBoardEntity = yunaBoardEntity;
        this.userEntity = userEntity;
    }
}
