package kr.hmit.dmjs.ui.order_management.model;

import java.io.Serializable;

public class FilterVO implements Serializable {


    public String ODM_01;
    public String CLT_02;
    public String ODD_03;
    public String ODM_02_ST;
    public String ODM_02_ED;

    public FilterVO() {
    }

    public FilterVO(String CLT_02,String ODD_03,String ODM_02_ST,String ODM_02_ED) {
        this.CLT_02 = CLT_02;
        this.ODD_03 = ODD_03;
        this.ODM_02_ST = ODM_02_ST;
        this.ODM_02_ED = ODM_02_ED;
    }
    public FilterVO(String ODM_01) {
        this.ODM_01 = ODM_01;

    }

}
