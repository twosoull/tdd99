package io.hhplus.tdd.point.domain;

import io.hhplus.tdd.point.TransactionType;
import lombok.Getter;

@Getter
public class PointHistoryDomain {
    private long id;
    private long userId;
    private long amount;
    private TransactionType type;
    private long updateMillis;

    public PointHistoryDomain() {
    }

    public PointHistoryDomain(long userId, long amount, TransactionType type, long updateMillis) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.updateMillis = updateMillis;
    }

}
