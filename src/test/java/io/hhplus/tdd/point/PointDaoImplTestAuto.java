package io.hhplus.tdd.point;

import io.hhplus.tdd.TddApplication;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.entity.PointHistoryEntity;
import io.hhplus.tdd.point.entity.UserPointEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TddApplication.class)
class PointDaoImplTestAuto {
    //의식흐름
    // 1. 4가지 중에 테스트를 거치기에 처음으로 작성하기 가장 편한 아이디 조회라고 생각했다.
    // 2. 하지만 데이터를 먼저 넣어야 하지 않을까하는 생각.
    // 3. **테스트를 위해서라면 먼저 insert를 테이블로 바로 넣어버리는 작성할지
    // // ** , insert메소드를 완성시키는게 좋을지 질문 필요
    // 4. 예외처리 부분은 어디서 throw해 주는게 좋을 지모르겠다
    /************** 질문 부분 *************
    // 1) Dao는 Mock 방식이 아닌 Autowired를 사용한 이유는 실제 dao가 제대로 값을 저장하고 조회하는지를 테스트 해보기 위함입니다.
    // >> 질문 1. Autowired로 진행한 것이 맞는 걸까요?
    // >> 질문 2. 예외처리의 경우 table에서 주는 값이 null이 아니고 userPoint의 경우 empty메소드로 인해 넣은 id값이 조회되어 버려 exception 처리를 어떻게 할지 감이 잡히지 않습니다.
    // >> 질문 3. Controller는 dto Service는 domain Dao는 entity를 사용하라고 강의 때에 들었던 것 같은데.. 각각 request, response마다 변환을 해서 값을 넘겨줘야 할지 고민입니다.
     */


    @Autowired
    PointHistoryTable pointHistoryTable;

    @Autowired
    UserPointTable userPointTable;

    @Autowired
    PointRepositoryImpl pointDao;


    @Test
    @DisplayName("특정 유저의 포인트 조회")
    void pointTest(){
        /**
         * given
         */
        //유저 1,2,3
        pointDao.insertUserPoint(new UserPointEntity(1,1000,System.currentTimeMillis()));
        pointDao.insertUserPoint(new UserPointEntity(2,2000,System.currentTimeMillis()));
        pointDao.insertUserPoint(new UserPointEntity(3,3000,System.currentTimeMillis()));

        /**
         * when
         */
        long id1 = 1L;
        UserPointEntity userPointEntity1 = pointDao.point(id1);
        long id2 = 2L;
        UserPointEntity userPointEntity2 = pointDao.point(id2);
        long id3 = 3L;
        UserPointEntity userPointEntity3 = pointDao.point(id3);

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

    }

    @Test
    @DisplayName("특정 유저의 포인트 조회")
    void pointTest_exception(){
        /**
         * 예외처리
         */
        //insert하지 않은 999번의 회원은 테이블에서 자동으로 기본값(디폴트)로 반환하는 상태임
        //null 예외처리 불가
        //1)
        UserPointEntity point = pointDao.point(999L);
        System.out.println("dd = " + point.getId()); //999
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
        List<PointHistoryEntity> pointHistories = pointDao.history(searchId);

        /**
         * then
         */
        Assertions.assertEquals(4, pointHistories.size());

        Assertions.assertEquals(1000,pointHistories.get(0).getAmount());
        Assertions.assertEquals(TransactionType.CHARGE,pointHistories.get(0).getType());

        Assertions.assertEquals(500,pointHistories.get(1).getAmount());
        Assertions.assertEquals(TransactionType.CHARGE,pointHistories.get(1).getType());

        Assertions.assertEquals(1000,pointHistories.get(2).getAmount());
        Assertions.assertEquals(TransactionType.USE,pointHistories.get(2).getType());

        Assertions.assertEquals(1000,pointHistories.get(3).getAmount());
        Assertions.assertEquals(TransactionType.CHARGE,pointHistories.get(3).getType());

        Assertions.assertEquals(1000,pointHistories.get(3).getAmount());
        Assertions.assertEquals(TransactionType.CHARGE,pointHistories.get(3).getType());

    }

    @Test
    @DisplayName("포인트 충전/이용 내역을 조회 예외처리")
    void history_ExceptionTest(){
        /**
         * 예외처리
         */
        List<PointHistoryEntity> pointHistories= pointDao.history(999L);
        System.out.println(pointHistories.size()); //0
        //질문..NoSuchElementException이 아니므로 dao단에서 할 필요가 없다..?
    }

    @Test
    @DisplayName("특정 유저의 포인트를 충전 - insert_history")
    void insertHistory(){
        /**
         * given
         */
        PointHistoryEntity pd =
                new PointHistoryEntity(1, 2000, TransactionType.CHARGE, System.currentTimeMillis());

        /**
         * when
         */
        PointHistoryEntity result = pointDao.insertHistory(pd);

        /**
         * then
         */
        Assertions.assertEquals(pd.getUserId(),result.getUserId());
        Assertions.assertEquals(pd.getAmount(),result.getAmount());
        Assertions.assertEquals(pd.getType(),result.getType());
        Assertions.assertEquals(pd.getUpdateMillis(),result.getUpdateMillis());
    }

    @Test
    @DisplayName("특정 유저의 포인트를 충전 - insert_userPoint")
    void insertUserPoint(){
        /**
         * given
         */
        UserPointEntity upd = new UserPointEntity(1, 5000, System.currentTimeMillis());
        /**
         * when
         */
        UserPointEntity result = pointDao.insertUserPoint(upd);

        /**
         * then
         */
        Assertions.assertEquals(upd.getId(),result.getId());
        Assertions.assertEquals(upd.getPoint(),result.getPoint());
    }


    // 결국 서비스 단에서 ++냐, --로 사용 되기에 dao에서 use는 딱히 필요가 없다.
    /*
    @Test
    @DisplayName("특정 유저의 포인트를 사용하는 기능")
    void use(){}
    */
}