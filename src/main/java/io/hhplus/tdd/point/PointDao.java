package io.hhplus.tdd.point;

import io.hhplus.tdd.point.domain.PointHistoryDomain;
import io.hhplus.tdd.point.domain.UserPointDomain;
import io.hhplus.tdd.point.entity.UserPointEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointDao {

    UserPointEntity point(Long id);

    List<PointHistory> history(Long id);

    PointHistory insertHistory(PointHistoryDomain pointHistoryDomain);

    UserPoint insertUserPoint(UserPointDomain userPointDomain);

}
