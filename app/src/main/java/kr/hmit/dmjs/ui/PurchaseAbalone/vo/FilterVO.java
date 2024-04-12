package kr.hmit.dmjs.ui.PurchaseAbalone.vo;

import java.io.Serializable;

public class FilterVO implements Serializable {


    public String NGG_01;
    public String CLT_01;
    public String DAH_01;
    public String NGG_YN;
    public String NGG_02_ST;
    public String NGG_02_ED;

    public FilterVO() {
    }

    public FilterVO(String CLT_01, String DAH_01, String NGG_02_ST, String NGG_02_ED, String NGG_YN) {
        this.CLT_01 = CLT_01;
        this.DAH_01 = DAH_01;
        this.NGG_02_ST = NGG_02_ST;
        this.NGG_02_ED = NGG_02_ED;
        this.NGG_YN=NGG_YN;
    }
    public FilterVO(String NGG_01) {
        this.NGG_01 = NGG_01;

    }

}
