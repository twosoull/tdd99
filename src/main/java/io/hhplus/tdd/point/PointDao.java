package io.hhplus.tdd.point;

import io.hhplus.tdd.point.domain.PointHistoryDomain;
import io.hhplus.tdd.point.domain.UserPointDomain;
import io.hhplus.tdd.point.entity.UserPointEntity;

import java.util.List;

public interface PointDao {

    UserPointEntity point(Long id);

    List<PointHistory> history(Long id);

    PointHistory insertHistory(PointHistoryDomain pointHistoryDomain);

    UserPoint insertUserPoint(UserPointDomain userPointDomain);

}
