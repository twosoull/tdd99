package io.hhplus.tdd.point.entity;

import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.dto.UserPointDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPointEntity {
    private long id;
    private long point;
    private long updateMillis;

    public UserPointEntity() {
    }

    public UserPointEntity(long id, long point, long updateMillis) {
        this.id = id;
        this.point = point;
        this.updateMillis = updateMillis;
    }

    public static UserPointEntity from(UserPoint userPoint){
        UserPointEntity userPointEntity = new UserPointEntity();
        userPointEntity.setId(userPoint.id());
        userPointEntity.setPoint(userPoint.point());
        userPointEntity.setUpdateMillis(userPoint.updateMillis());
        return userPointEntity;
    }

    public static UserPointDto toDto(UserPointEntity upe){
        return new UserPointDto(upe.getId(), upe.getPoint(), upe.getUpdateMillis());
    }
}
