package io.hhplus.tdd.point.dto;

import io.hhplus.tdd.point.TransactionType;
import lombok.Getter;

@Getter
public class PointHistoryDto {
    private long id;
    private long userId;
    private long amount;
    private TransactionType type;
    private long updateMillis;

}
