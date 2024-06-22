package io.hhplus.tdd.point;


import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.entity.PointHistoryEntity;
import io.hhplus.tdd.point.entity.UserPointEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class PointRepositoryImplTestMock {

    @Mock
    UserPointTable userPointTable;

    @Mock
    PointHistoryTable pointHistoryTable;

    @InjectMocks
    PointRepositoryImpl pointRepository;

    @Test
    @DisplayName("유저의 포인트를 조회")
    void point_test() {
        //give
        Long userId = 1L;
        UserPoint userPoint = UserPoint.empty(userId);
        doReturn(userPoint).when(userPointTable).selectById(userId);

        //when
        UserPointEntity point = pointRepository.point(userId);

        //then
        Assertions.assertEquals(1, point.getId());
        Assertions.assertEquals(0, point.getPoint());

    }

    @Test
    @DisplayName("포인트 충전/이용 내역을 조회")
    void history_test() {

        //give
        Long userId = 1L;
        List<PointHistory> pointHistories = new ArrayList<>();
        for (Long i = 1L; i < 5L; i++) {
        PointHistory pointHistory = new PointHistory(i, userId, 1000, TransactionType.CHARGE, System.currentTimeMillis());
            pointHistories.add(pointHistory);
        }
        doReturn(pointHistories).when(pointHistoryTable).selectAllByUserId(userId);

        //when
        List<PointHistoryEntity> history = pointRepository.history(userId);

        //then
        for (PointHistoryEntity ph : history) {
            System.out.println(ph.toString());
            Assertions.assertEquals(userId,ph.getUserId());
        }
    }


    //
    @Test
    @DisplayName("포인트 충전/이용 내역을 저장")
    void insertHistory_test() {

        //give
        Long id = 1L ;
        Long userId = 1L;
        int amount = 1000;
        TransactionType type = TransactionType.CHARGE;
        long updateMillis = System.currentTimeMillis();

        PointHistory whenPointHistory = new PointHistory(id , userId, amount, type, updateMillis);
        doReturn(whenPointHistory).when(pointHistoryTable)
                .insert(1L,1000,TransactionType.CHARGE,updateMillis);

        //when
        PointHistoryEntity pointHistoryDomain = new PointHistoryEntity(userId,amount,type,updateMillis);
        PointHistoryEntity resultEntity = pointRepository.insertHistory(pointHistoryDomain);

        //then
        Assertions.assertEquals(userId, resultEntity.getUserId());
        Assertions.assertEquals(amount, resultEntity.getAmount());
        Assertions.assertEquals(type, resultEntity.getType());
        Assertions.assertEquals(updateMillis, resultEntity.getUpdateMillis());
    }

    @Test
    @DisplayName("특정 유저의 포인트를 충전/사용")
    void insertUserPoint_test(){
        //give
        Long id = 1L;
        int point = 1000;
        long updateMillis = System.currentTimeMillis();
        UserPoint whenUserPoint = new UserPoint(id,point,updateMillis);
        doReturn(whenUserPoint).when(userPointTable).insertOrUpdate(id,point);

        //when
        UserPointEntity userPointEntity = new UserPointEntity(id,point, System.currentTimeMillis());
        UserPointEntity resultEntity = pointRepository.insertUserPoint(userPointEntity);

        //then
        Assertions.assertEquals(id,resultEntity.getId());
        Assertions.assertEquals(1000,resultEntity.getPoint());
    }
}
