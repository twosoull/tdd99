package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.PointHistoryDomain;
import io.hhplus.tdd.point.domain.UserPointDomain;
import io.hhplus.tdd.point.entity.UserPointEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PointDaoImpl implements PointDao{

    final private UserPointTable userPointTable;
    final private PointHistoryTable pointHistoryTable;

    @Override
    public UserPointEntity point(Long id){
        return new UserPointEntity().toEntity(userPointTable.selectById(id));
    }

    @Override
    public List<PointHistory> history(Long id) {
        return pointHistoryTable.selectAllByUserId(id);
    }

    @Override
    public PointHistory insertHistory(PointHistoryDomain pd) {
        return pointHistoryTable.insert(pd.getUserId(), pd.getAmount(), pd.getType(), pd.getUpdateMillis());
    }

    @Override
    public UserPoint insertUserPoint(UserPointDomain upd) {
        return userPointTable.insertOrUpdate(upd.getId(), upd.getPoint());
    }
}
