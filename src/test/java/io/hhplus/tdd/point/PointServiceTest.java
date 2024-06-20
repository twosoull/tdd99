package io.hhplus.tdd.point;
import io.hhplus.tdd.point.dto.PointHistoryDto;
import io.hhplus.tdd.point.dto.UserPointDto;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {
    @Mock
    PointRepository pointRepository;

    @InjectMocks
    PointService pointService;

    @Test
    @DisplayName("포인트 충전/이용 내역을 조회")
    void point(){
        /**
         * given
         */
        long id = 1L;
        long point = 1000;
        long updateMillis = System.currentTimeMillis();
        UserPointEntity returnUserPoint = new UserPointEntity(id,point, updateMillis);
        doReturn(returnUserPoint).when(pointRepository).point(1L);
        /**
         * when
         */
        UserPointDto upe = pointService.point(1L);
        /**
         * then
         */

        Assertions.assertEquals(id, upe.getId());
        Assertions.assertEquals(point, upe.getPoint());
        //1)PointDao에서 데이터를 셀렉트 이후 비교
    }

    @Test
    @DisplayName("포인트 충전/이용 내역을 조회 ")
    void history(){
        /**
         * given
         */
        long userId = 1L;
        List<PointHistoryEntity> pointHistories = new ArrayList<>();
        for (Long i = 1L; i < 4L; i++) {
            PointHistoryEntity pointHistory = new PointHistoryEntity(i, userId, i * 1000, TransactionType.CHARGE, System.currentTimeMillis());
            pointHistories.add(pointHistory);
        }
        doReturn(pointHistories).when(pointRepository).history(userId);

        /**
         * when
         */
        List<PointHistoryDto> pointHistoryDtos = pointService.history(userId);

        /**
         * then
         */

        Assertions.assertEquals(3,pointHistoryDtos.size());
        Assertions.assertEquals(userId,pointHistoryDtos.get(0).getUserId());
        Assertions.assertEquals(1000,pointHistoryDtos.get(0).getAmount());
        Assertions.assertEquals(TransactionType.CHARGE,pointHistoryDtos.get(0).getType());

        Assertions.assertEquals(userId,pointHistoryDtos.get(1).getUserId());
        Assertions.assertEquals(2000,pointHistoryDtos.get(1).getAmount());
        Assertions.assertEquals(TransactionType.CHARGE,pointHistoryDtos.get(1).getType());

        Assertions.assertEquals(userId,pointHistoryDtos.get(2).getUserId());
        Assertions.assertEquals(3000,pointHistoryDtos.get(2).getAmount());
        Assertions.assertEquals(TransactionType.CHARGE,pointHistoryDtos.get(2).getType());

    }

    @Test
    @DisplayName("신규 특정 유저의 포인트를 충전")
    void charge_insert(){
        /**
         * given
         */
        long id = 1L;
        long findPoint = 1000; //찾은 유저의 포인트
        long point = 2000; // 충전 포인트
        long updateMillis = System.currentTimeMillis();
        long sumPoint = findPoint + point; //합산 포인트
        //조회
        UserPointEntity returnUserPoint = new UserPointEntity(id,findPoint, updateMillis);
        doReturn(returnUserPoint).when(pointRepository).point(id);

        //insert 후
        UserPointEntity userPointEntity = new UserPointEntity(id,sumPoint,System.currentTimeMillis());
        doReturn(userPointEntity).when(pointRepository).insertUserPoint(any(UserPointEntity.class));

        //포인트 내역 저장
        PointHistoryEntity pointHistoryEntity = new PointHistoryEntity(id,point,TransactionType.CHARGE,System.currentTimeMillis());
        doReturn(pointHistoryEntity).when(pointRepository).insertHistory(any(PointHistoryEntity.class));

        /**
         * when
         */
        UserPointDto chargedUserPointDto = pointService.charge(id, point);

        /**
         * then
         */
        Assertions.assertEquals(id,chargedUserPointDto.getId());
        Assertions.assertEquals(sumPoint, chargedUserPointDto.getPoint());

    }

    @Test
    @DisplayName("충전/사용 히스토리 남기기")
    void insertPointHistory_test(){
        //give
        //insert 후
        long userId = 1L;
        long point = 2000; //포인트
        PointHistoryEntity pointHistoryEntity = new PointHistoryEntity(userId,point,TransactionType.CHARGE,System.currentTimeMillis());
        doReturn(pointHistoryEntity).when(pointRepository).insertHistory(any(PointHistoryEntity.class));

        //when
        PointHistoryDto pointHistoryDto = pointService.insertPointHistory(userId, point, TransactionType.CHARGE);

        //then
        Assertions.assertEquals(userId, pointHistoryDto.getUserId());
        Assertions.assertEquals(point, pointHistoryDto.getAmount());
        Assertions.assertEquals(TransactionType.CHARGE, pointHistoryDto.getType());
    }

    @Test
    @DisplayName("특정 유저의 포인트를 사용하는 기능")
    void use(){
        /**
         * given
         */
        long id = 1L;
        long point = 1000L;
        long useAmount = 500L;
        long subtractPoint = point - useAmount;

        UserPointEntity giveUserPoint = new UserPointEntity(id, point, System.currentTimeMillis());
        doReturn(giveUserPoint).when(pointRepository).point(id);

        //insert 후
        UserPointEntity userPointEntity = new UserPointEntity(id,subtractPoint,System.currentTimeMillis());
        doReturn(userPointEntity).when(pointRepository).insertUserPoint(any(UserPointEntity.class));

        //포인트 내역 저장
        PointHistoryEntity pointHistoryEntity = new PointHistoryEntity(id,point,TransactionType.CHARGE,System.currentTimeMillis());
        doReturn(pointHistoryEntity).when(pointRepository).insertHistory(any(PointHistoryEntity.class));

        /**
         * when
         */
        //잔고가 부족할 경우, 포인트 사용은 실패하여야 합니다.
        UserPointDto userPointDto = pointService.use(id,useAmount);
        /**
         * then
         */
        Assertions.assertEquals(subtractPoint, userPointDto.getPoint());
    }

    @Test
    void subtractPoint_test_throw_exception(){
        assertThrows(ArithmeticException.class, () -> {
            pointService.subtractPoint(1000, 2000);
        });
    }

    @Test
    void subtractPoint_test_success(){
        long result = pointService.subtractPoint(1000, 200);
        Assertions.assertEquals(result, 800);
    }
}