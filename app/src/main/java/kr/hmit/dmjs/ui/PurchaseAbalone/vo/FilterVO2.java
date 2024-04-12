package kr.hmit.dmjs.ui.PurchaseAbalone.vo;

import java.io.Serializable;

public class FilterVO2 implements Serializable {


    public String NGO_01;
    public String CLT_01;
    public String DAH_01;

    public String NGO_02_ST;
    public String NGO_02_ED;
    public String NGO_08;

    public FilterVO2() {
    }

    public FilterVO2(String CLT_01, String DAH_01, String NGO_02_ST, String NGO_02_ED, String NGO_08) {
        this.CLT_01 = CLT_01;
        this.DAH_01 = DAH_01;
        this.NGO_02_ST = NGO_02_ST;
        this.NGO_02_ED = NGO_02_ED;
        this.NGO_08=NGO_08;
    }
    public FilterVO2(String NGO_01) {
        this.NGO_01 = NGO_01;

    }

}
