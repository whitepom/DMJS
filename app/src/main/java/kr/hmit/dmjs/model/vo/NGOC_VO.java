package kr.hmit.dmjs.model.vo;

import java.io.Serializable;

public class NGOC_VO implements Serializable{

    //회사코드
    public String NGOC_ID;
    public String NGOC_01;
    public String NGOC_02; //일자
    public String NGOC_03; // 품번
    public String NGOC_03_NM;

    public String NGOC_04; //단가
    public String NGOC_05; //수매량
    public String NGOC_06; //금액
    public String NGOC_07; // 예비

    public String NGOC_97; //비고
    public String DAH_01;
    public String DAH_02;
    public String DAH_04;
    public String DAH_14;
    public String DAH_11;
    public String CLT_01;
    public String CLT_02;

    public String CNT;

    public boolean Validation;

    public String  ERROR_MSG;


    public NGOC_VO(String NGOC_03, String NGOC_04, String NGOC_05, String NGOC_06,String NGOC_97){
        this.NGOC_03 = NGOC_03; //품번
        this.NGOC_04 = NGOC_04; // 단가
        this.NGOC_05 = NGOC_05; // 주문수량
        this.NGOC_06 = NGOC_06; // 금액
        this.NGOC_97 = NGOC_97;
    }
}
