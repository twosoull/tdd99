package io.hhplus.tdd.point.entity;

import io.hhplus.tdd.point.TransactionType;
import lombok.Getter;

@Getter
public class PointHistoryEntity {
    private long id;
    private long userId;
    private long amount;
    private TransactionType type;
    private long updateMillis;


}
