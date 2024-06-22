package io.hhplus.tdd.point;

import io.hhplus.tdd.point.entity.PointHistoryEntity;
import io.hhplus.tdd.point.entity.UserPointEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository {

    UserPointEntity point(Long id);

    List<PointHistoryEntity> history(Long id);

    PointHistoryEntity insertHistory(PointHistoryEntity pointHistoryEntity);

    UserPointEntity insertUserPoint(UserPointEntity userPointEntity);

}
