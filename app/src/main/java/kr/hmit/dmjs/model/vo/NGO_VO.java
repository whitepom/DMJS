package kr.hmit.dmjs.model.vo;

import java.io.Serializable;

public class NGO_VO implements Serializable{

    //회사코드
    public String NGO_ID;
    public String NGO_01;
    public String NGO_02; //일자
    public String NGO_02_ST;
    public String NGO_02_ED;
    public String NGO_03; //거래처 clt
    public String NGO_03_NM;
    public String NGO_04_NM;
    public String NGO_04; //제품번호
    public String NGO_05; //단가
    public String NGO_06; //수매량
    public String NGO_07; //금액
    public String NGO_08;
    public String NGO_08_NM;
    public String NGO_09;
    public String NGO_10;
    public String NGO_80; // 일련번호
    public String NGO_97; //비고
    public String DAH_01;
    public String DAH_02;
    public String DAH_04;
    public String DAH_14;
    public String DAH_11;
    public String CLT_01;
    public String CLT_02;

    public String NGG_02;

    public String NGG_03;
    public String NGG_03_NM;

    //NGOC

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

    public String CNT;

    public boolean Validation;

    public String  ERROR_MSG;


    public NGO_VO(String NGO_04, String NGO_05, String NGO_06, String NGO_07,String NGO_97){
        this.NGOC_03 = NGO_04; //품번
        this.NGOC_04 = NGO_05; // 단가
        this.NGOC_05 = NGO_06; // 주문수량
        this.NGOC_06 = NGO_07; // 금액
        this.NGOC_97 = NGO_97;
    }


}
