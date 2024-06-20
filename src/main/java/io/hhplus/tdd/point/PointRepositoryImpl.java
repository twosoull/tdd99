package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.entity.PointHistoryEntity;
import io.hhplus.tdd.point.entity.UserPointEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    @Override
    public UserPointEntity point(Long id){
        return UserPointEntity.from(userPointTable.selectById(id));
    }

    @Override
    public List<PointHistoryEntity> history(Long id) {
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(id);

        return pointHistories.stream().map(PointHistoryEntity::from).collect(Collectors.toList());
    }

    @Override
    public PointHistoryEntity insertHistory(PointHistoryEntity pd) {
        return PointHistoryEntity.from(pointHistoryTable.insert(pd.getUserId(), pd.getAmount(), pd.getType(), pd.getUpdateMillis()));

    }

    @Override
    public UserPointEntity insertUserPoint(UserPointEntity upd) {
        return UserPointEntity.from(userPointTable.insertOrUpdate(upd.getId(), upd.getPoint()));
    }
}
