package kr.hmit.dmjs.ui.Cusomer.model;

import java.io.Serializable;

public class MultiItemVO implements Serializable {


    public String RUN_01;
    public String RUN_02;
    public String RUN_03;
    public String RUN_04;
    public String RUN_05;
    public String RUN_06;
    public String RUN_07;
    public String RUN_08;
    public String RUN_09;
    public String RUN_10;
    public String RUN_11;
    public String RUN_12;
    public String RUN_13;
    public String RUN_14;
    public String RUN_1501;
    public String RUN_1502;
    public String RUN_1601;
    public String RUN_1602;
    public String RUN_1603;
    public String RUN_1604;
    public String RUN_1701;
    public String RUN_1702;
    public String RUN_1703;
    public String RUN_18;
    public String RUN_20;
    public String RUN_2101;
    public String RUN_2102;
    public String RUN_2103;
    public String RUN_2201;
    public String RUN_2202;
    public String RUN_2203;
    public String RUN_23;
    public String RUN_24;
    public String RUN_25;
    public String RUN_26;

    public String RUN_27;
    public String RUN_28;

    public String RUN_97;
    public String RUN_98;


    public String CLT_02;
    public String CLT_06;
    public String CLT_07;
    public String CLT_08;
    public String CLT_1001;
    public String CLT_1002;
    public String CLT_13;
    public String CLT_19;
    public String CLT_20;
    public String CLT_21;

    public boolean Validation;
    public String ERROR_MSG;
    public MultiItemVO() {
    }

    public MultiItemVO(
            String RUN_02, // 출고일자
            String RUN_03, // 출고일자
            String RUN_04, // 내용
            String RUN_07, //주문금액
            String RUN_08, //주문경로
            String RUN_09, //품번 없는 품목명만 있는 경우
            String RUN_1701,//입금자
            String RUN_1702, // 입금여부
            String RUN_18, // 우편번호
            String RUN_20, //택배사 
            String RUN_2201, //수취인
            String RUN_2202, // 연락처
            String RUN_2203, // 주소
            String RUN_23, // 요청사항
            String RUN_24, // 송장번호
            String RUN_25, // 택배비
            String RUN_27,  //품번 
            String RUN_97 // 비고
            )
    {

        this.RUN_02= RUN_02;
        this.RUN_03= RUN_03;
      //  this.RUN_03=RUN_2201; //주문자 수취인 동일하게 설정함
        this.RUN_04= RUN_04;
        this.RUN_07= RUN_07;
        this.RUN_08= RUN_08;
        this.RUN_09= RUN_09;
        this.RUN_1701= RUN_1701;
        this.RUN_1702= RUN_1702;
        this.RUN_18= RUN_18;
        this.RUN_20= RUN_20;
        this.RUN_2101=RUN_03;
        this.RUN_2201= RUN_2201;
        this.RUN_2202 = RUN_2202;
        this.RUN_2203 = RUN_2203;
        this.RUN_23= RUN_23;
        this.RUN_24= RUN_24;
        this.RUN_25= RUN_25;
        this.RUN_27= RUN_27;
        this.RUN_97= RUN_97;

    }


}
