package kr.hmit.dmjs.ui.receive.model;

import java.io.Serializable;

public class FilterVO implements Serializable {

    public String ODD_03; // 품번
    public String CLT_02; // 입고처
    public String CLT_01; //입고처
    public String GEM_04_NM; //품명
    public String GEM_02_ST; //조회조건 입고일
    public String GEM_02_ED; // 조회조건 입고일

    public FilterVO() {
    }

    public FilterVO( String ODD_03,String CLT_02,String GEM_04_NM
                    ,String CLT_01, String GEM_02_ST, String GEM_02_ED) {

        this.ODD_03 = ODD_03;
        this.CLT_02=CLT_02;
        this.GEM_04_NM=GEM_04_NM;
        this.GEM_02_ED=GEM_02_ED;
        this.GEM_02_ST=GEM_02_ST;
        this.CLT_01=CLT_01;


    }


}
