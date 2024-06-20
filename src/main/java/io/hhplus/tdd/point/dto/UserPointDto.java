package io.hhplus.tdd.point.dto;

import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.entity.UserPointEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPointDto {
    private long id;
    private long point;
    private long updateMillis;

    public UserPointDto() {
    }

    public UserPointDto(long id, long point, long updateMillis) {
        this.id = id;
        this.point = point;
        this.updateMillis = updateMillis;
    }

    public static UserPointDto from(UserPointEntity upe) {
        UserPointDto userPointDto = new UserPointDto();
        userPointDto.setId(upe.getId());
        userPointDto.setPoint(upe.getPoint());
        userPointDto.setUpdateMillis(upe.getUpdateMillis());
        return userPointDto;
    }
}
