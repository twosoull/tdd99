package io.hhplus.tdd.point;

import io.hhplus.tdd.point.dto.PointHistoryDto;
import io.hhplus.tdd.point.dto.UserPointDto;
import io.hhplus.tdd.point.entity.PointHistoryEntity;
import io.hhplus.tdd.point.entity.UserPointEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PointService {

    private final PointRepository pointDao;
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    public UserPointDto point(long id) {
        log.info("[point] id = {}", id);
        lock.lock();

        UserPointEntity point;
        try {
            point = pointDao.point(id);
        } finally {
            lock.unlock();
        }
        return UserPointEntity.toDto(point);
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    public List<PointHistoryDto> history(long id) {
        log.info("[history] id = {}", id);
        return pointDao.history(id).stream().map(PointHistoryEntity::toDto).collect(Collectors.toList());
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    public UserPointDto charge(long id, long chargePoint){
        log.info("[charge] id = {}, chargePoint = {}", id, chargePoint);
        lock.lock();

        UserPointEntity userPointEntity;
        try {
            //id를 조회한다.
            UserPointEntity findUserPointEntity = pointDao.point(id);

            long sumPoint = findUserPointEntity.getPoint() + chargePoint;
            findUserPointEntity.setPoint(sumPoint);
            userPointEntity = pointDao.insertUserPoint(findUserPointEntity);

            //히스토리를 쌓는다.
            insertPointHistory(id, chargePoint, TransactionType.CHARGE);
        } finally {
            lock.unlock();
        }

        return UserPointDto.from(userPointEntity);
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    public UserPointDto use(long id, long usePoint){
        log.info("[use] id = {}, usePoint = {}", id, usePoint);
        lock.lock();
        UserPointEntity subtractPointUserPointEntity;

        try {
            UserPointEntity userPointEntity = pointDao.point(id);

            //valid 처리
            long resultPoint = subtractPoint(userPointEntity.getPoint(), usePoint);

            userPointEntity.setPoint(resultPoint);
            subtractPointUserPointEntity = pointDao.insertUserPoint(userPointEntity);

            //히스토리를 쌓는다.
            insertPointHistory(id, usePoint, TransactionType.USE);
        } catch (Exception e) {
            log.error("[use] 예외 발생: ", e);
            throw e;
        }finally {
            lock.unlock();
        }

        return UserPointDto.from(subtractPointUserPointEntity);
    }

    public long subtractPoint(long currentPoint, long usePoint){
        long result = currentPoint - usePoint;

        if(result < 0 ) {
            throw new ArithmeticException();
        }

        return result;
    }

    public PointHistoryDto insertPointHistory(long userId, long amount, TransactionType type) {
        return PointHistoryDto.from(pointDao
                .insertHistory(new PointHistoryEntity(userId,amount,type,System.currentTimeMillis())));
    }

}
