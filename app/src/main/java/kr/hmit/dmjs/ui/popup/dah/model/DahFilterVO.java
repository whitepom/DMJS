package kr.hmit.dmjs.ui.popup.dah.model;

import java.io.Serializable;

public class DahFilterVO implements Serializable {

    public String REQ_01;
    public String REQ_02_ST;
    public String REQ_02_ED;
    public String REQ_03; //거래처
    public String REQ_04; //품번/품명/규격
    public String REQ_09; // 판매유형


    public DahFilterVO() {
    }

    public DahFilterVO(String REQ_02_ST, String REQ_02_ED, String REQ_03, String REQ_04, String REQ_09) {
        this.REQ_02_ST = REQ_02_ST;
        this.REQ_02_ED = REQ_02_ED;
        this.REQ_03 = REQ_03;
        this.REQ_04 = REQ_04;
        this.REQ_09 = REQ_09;

    }
    public DahFilterVO(String REQ_01) {
        this.REQ_01 = REQ_01;


    }

}
