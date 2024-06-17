package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class PointServiceTest {
    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    // 1. 4가지 중에 테스트를 거치기에 처음으로 작성하기 가장 편한 아이디 조회라고 생각했다.
    // 2. dao를 사용해야하기 때문에 dao에서 불러올 데이터 로직을 먼저 작성하는 것이 바람직하다고 생각했다.


    private PointHistoryTable pointHistoryTable;
    private UserPointTable userPointTable;
    private PointDao pointDao;

    @BeforeEach
    void setUp(){
        pointHistoryTable = new PointHistoryTable();
        userPointTable = new UserPointTable();
    }

    // 1) Dao를 연결해서 테스트
    // 2) Dao의 테스트 데이터를 setUp에서 먼저 넣어야 함

    @Test
    @DisplayName("포인트 충전/이용 내역을 조회")
    void point(){
        /**
         * given
         */

        /**
         * when
         */

        /**
         * then
         */
        //1)PointDao에서 데이터를 셀렉트 이후 비교
    }

    @Test
    @DisplayName("포인트 충전/이용 내역을 조회 ")
    void history(){
        /**
         * given
         */

        /**
         * when
         */

        /**
         * then
         */
    }

    @Test
    @DisplayName("특정 유저의 포인트를 충전")
    void charge(){
        /**
         * given
         */

        /**
         * when
         */

        /**
         * then
         */
        //1) history를 남긴 뒤에
        //2) userPoint 조회 후 포인트를 ++ 해준다. (있을 경우, 예외처리 필요)
        //3) userPoint insert or update가 필요함


    }

    @Test
    @DisplayName("특정 유저의 포인트를 사용하는 기능")
    void use(){
        /**
         * given
         */

        /**
         * when
         */

        /**
         * then
         */

        //1) history를 남긴 뒤에
        //2) userPoint 조회 후 포인트를 -- 해준다. (있을 경우, 예외처리 필요)
        //3) userPoint insert or update가 필요함

    }

}