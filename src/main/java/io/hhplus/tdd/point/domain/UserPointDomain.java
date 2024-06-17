package io.hhplus.tdd.point.domain;

import io.hhplus.tdd.point.UserPoint;
import lombok.Getter;

@Getter
public class UserPointDomain {
    private long id;
    private long point;
    private long updateMillis;

    public UserPointDomain(long id, long point) {
        this.id = id;
        this.point = point;
    }
}
