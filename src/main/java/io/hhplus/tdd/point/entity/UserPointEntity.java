package io.hhplus.tdd.point.entity;

import io.hhplus.tdd.point.UserPoint;
import lombok.Getter;

@Getter
public class UserPointEntity {
    private long id;
    private long point;
    private long updateMillis;

    public UserPointEntity toEntity(UserPoint userPoint){
        this.id = userPoint.id();
        this.point = userPoint.point();
        this.updateMillis = userPoint.updateMillis();
        return this;
    }
}
