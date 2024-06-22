package io.hhplus.tdd.point.dto;

import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.entity.PointHistoryEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PointHistoryDto {
    private long id;
    private long userId;
    private long amount;
    private TransactionType type;
    private long updateMillis;

    public PointHistoryDto() {
    }

    public PointHistoryDto(long id, long userId, long amount, TransactionType type, long updateMillis) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.updateMillis = updateMillis;
    }

    public static PointHistoryDto from(PointHistoryEntity phe){
        PointHistoryDto pointHistoryDto = new PointHistoryDto();
        pointHistoryDto.setId(phe.getId());
        pointHistoryDto.setUserId(phe.getUserId());
        pointHistoryDto.setAmount(phe.getAmount());
        pointHistoryDto.setType(phe.getType());
        pointHistoryDto.setUpdateMillis(phe.getUpdateMillis());
        return pointHistoryDto;
    }
}
