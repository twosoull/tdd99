package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.PointHistoryDomain;
import io.hhplus.tdd.point.domain.UserPointDomain;
import io.hhplus.tdd.point.entity.UserPointEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class PointDaoImplTest {
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
    // 2. 하지만 데이터를 먼저 넣어야 하지 않을까하는 생각.
    // 3. **테스트를 위해서라면 먼저 insert를 테이블로 바로 넣어버리는 작성할지
    // // ** , insert메소드를 완성시키는게 좋을지 질문 필요
    // 4. 예외처리 부분은 어디서 throw해 주는게 좋을 지모르겠다

    private PointHistoryTable pointHistoryTable;
    private UserPointTable userPointTable;

    @BeforeEach
    void setUp(){
        pointHistoryTable = new PointHistoryTable();
        userPointTable = new UserPointTable();
    }

    @Test
    @DisplayName("특정 유저의 포인트 조회")
    void point(){
        /**
         * given
         */
        //유저 1,2,3
        userPointTable.insertOrUpdate(1, 1000);
        userPointTable.insertOrUpdate(2, 2000);
        userPointTable.insertOrUpdate(3, 3000);

        /**
         * when
         */
        long id1 = 1L;
        UserPoint userPoint1 = userPointTable.selectById(id1);
        long id2 = 2L;
        UserPoint userPoint2 = userPointTable.selectById(id2);
        long id3 = 3L;
        UserPoint userPoint3 = userPointTable.selectById(id3);

        UserPointEntity userPointEntity1 = new UserPointEntity()
                .toEntity(userPoint1);

        UserPointEntity userPointEntity2 = new UserPointEntity()
                .toEntity(userPoint2);

        UserPointEntity userPointEntity3 = new UserPointEntity()
                .toEntity(userPoint3);

        /**
         * then
         */
        //1. 회원
        Assertions.assertEquals(1,userPointEntity1.getId());
        Assertions.assertEquals(1000,userPointEntity1.getPoint());
        //2. 회원
        Assertions.assertEquals(2,userPointEntity2.getId());
        Assertions.assertEquals(2000,userPointEntity2.getPoint());
        //3. 회원
        Assertions.assertEquals(3,userPointEntity3.getId());
        Assertions.assertEquals(3000,userPointEntity3.getPoint());

        /**
         * 예외처리
         */
        //insert하지 않은 999번의 회원은 테이블에서 자동으로 기본값(디폴트)로 반환하는 상태임
        //null 예외처리 불가
        //1)
        /*
        Assertions.assertThrows(NoSuchElementException.class , ()->
            userPointTable.selectById(999L)
        );
        */
        //2)
        //Assertions.assertNull(userPointTable.selectById(999L));
    }

    @Test
    @DisplayName("포인트 충전/이용 내역을 조회")
    void history(){
        /**
         *given
         */

        pointHistoryTable.insert(1, 1000, TransactionType.CHARGE,System.currentTimeMillis());
        pointHistoryTable.insert(1, 500, TransactionType.CHARGE,System.currentTimeMillis());
        pointHistoryTable.insert(1, 1000, TransactionType.USE,System.currentTimeMillis());
        pointHistoryTable.insert(1, 1000, TransactionType.CHARGE,System.currentTimeMillis());

        /**
         * when
         */
        long searchId = 1;
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(searchId);

        /**
         * then
         */
        Assertions.assertEquals(4, pointHistories.size());

        Assertions.assertEquals(1000,pointHistories.get(0).amount());
        Assertions.assertEquals(TransactionType.CHARGE,pointHistories.get(0).type());

        Assertions.assertEquals(500,pointHistories.get(1).amount());
        Assertions.assertEquals(TransactionType.CHARGE,pointHistories.get(1).type());

        Assertions.assertEquals(1000,pointHistories.get(2).amount());
        Assertions.assertEquals(TransactionType.USE,pointHistories.get(2).type());

        Assertions.assertEquals(1000,pointHistories.get(3).amount());
        Assertions.assertEquals(TransactionType.CHARGE,pointHistories.get(3).type());

    }

    @Test
    @DisplayName("포인트 충전/이용 내역을 조회 예외처리")
    void history_ExceptionTest(){
        /**
         * 예외처리
         */
        List<PointHistory> pointHistories= pointHistoryTable.selectAllByUserId(999);
        System.out.println(pointHistories.size()); //0
        //질문..NoSuchElementException이 아니므로 dao단에서 할 필요가 없다..?
    }

    @Test
    @DisplayName("특정 유저의 포인트를 충전 - insert_history")
    void insertHistory(){
        /**
         * given
         */
        PointHistoryDomain pd = new PointHistoryDomain(1, 2000, TransactionType.CHARGE, System.currentTimeMillis());

        /**
         * when
         */
        PointHistory result = pointHistoryTable.insert(pd.getUserId(), pd.getAmount(), pd.getType(), pd.getUpdateMillis());

        /**
         * then
         */
        Assertions.assertEquals(pd.getUserId(),result.userId());
        Assertions.assertEquals(pd.getAmount(),result.amount());
        Assertions.assertEquals(pd.getType(),result.type());
        Assertions.assertEquals(pd.getUpdateMillis(),result.updateMillis());
    }

    @Test
    @DisplayName("특정 유저의 포인트를 충전 - insert_history")
    void insertHistory_exception(){
        PointHistory result = pointHistoryTable.insert(-1, -1, TransactionType.CHARGE, 123);
        //음수도 그냥 들어가는데.. 어떻게 테스트해야하나 흠
    }

    @Test
    @DisplayName("특정 유저의 포인트를 충전 - insert_userPoint")
    void insertUserPoint(){
        /**
         * given
         */
        UserPointDomain upd = new UserPointDomain(1, 5000);
        /**
         * when
         */
        UserPoint result = userPointTable.insertOrUpdate(upd.getId(), upd.getPoint());

        /**
         * then
         */
        Assertions.assertEquals(upd.getId(),result.id());
        Assertions.assertEquals(upd.getPoint(),result.point());
    }


    // 결국 서비스 단에서 ++냐, --로 사용 되기에 dao에서 use는 딱히 필요가 없다.
    /*
    @Test
    @DisplayName("특정 유저의 포인트를 사용하는 기능")
    void use(){}
    */
}