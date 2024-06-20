package io.hhplus.tdd.point;

import io.hhplus.tdd.point.dto.PointHistoryDto;
import io.hhplus.tdd.point.dto.UserPointDto;
import io.hhplus.tdd.point.entity.PointHistoryEntity;
import io.hhplus.tdd.point.entity.UserPointEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointDao;

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    public UserPointDto point(long id) {
        return UserPointEntity.toDto(pointDao.point(id));
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    public List<PointHistoryDto> history(long id) {
        return pointDao.history(id).stream().map(PointHistoryEntity::toDto).collect(Collectors.toList());
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    public UserPointDto charge(long id, long chargePoint){
        //id를 조회한다.
        UserPointEntity findUserPointEntity = pointDao.point(id);

        long sumPoint = findUserPointEntity.getPoint() + chargePoint;
        findUserPointEntity.setPoint(sumPoint);
        UserPointEntity userPointEntity = pointDao.insertUserPoint(findUserPointEntity);

        //히스토리를 쌓는다.
        insertPointHistory(id, chargePoint, TransactionType.CHARGE);

        return UserPointDto.from(userPointEntity);
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    public UserPointDto use(long id, long usePoint){
        UserPointEntity userPointEntity = pointDao.point(id);

        //valid 처리
        long resultPoint = subtractPoint(userPointEntity.getPoint(), usePoint);

        userPointEntity.setPoint(resultPoint);
        UserPointEntity subtractPointUserPointEntity = pointDao.insertUserPoint(userPointEntity);

        //히스토리를 쌓는다.
        insertPointHistory(id, usePoint, TransactionType.USE);

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
