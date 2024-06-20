package io.hhplus.tdd.point.entity;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.dto.PointHistoryDto;
import io.hhplus.tdd.point.dto.UserPointDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PointHistoryEntity {
    private long id;
    private long userId;
    private long amount;
    private TransactionType type;
    private long updateMillis;

    public PointHistoryEntity() {
    }

    public PointHistoryEntity(long userId, long amount, TransactionType type, long updateMillis) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.updateMillis = updateMillis;
    }

    public PointHistoryEntity(long id, long userId, long amount, TransactionType type, long updateMillis) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.updateMillis = updateMillis;
    }

    public static PointHistoryEntity from(PointHistory pointHistory) {
        PointHistoryEntity entity = new PointHistoryEntity();
        entity.setId(pointHistory.id());
        entity.setUserId(pointHistory.userId());
        entity.setAmount(pointHistory.amount());
        entity.setType(pointHistory.type());
        entity.setUpdateMillis(pointHistory.updateMillis());
        return entity;
    }

    public static PointHistoryDto toDto(PointHistoryEntity phe){
        return new PointHistoryDto(phe.getId(), phe.getUserId(), phe.getAmount(),phe.getType(),phe.getUpdateMillis());
    }

}
