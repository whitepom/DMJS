package kr.hmit.dmjs.ui.product_info.model;

import java.io.Serializable;

public class FilterVO implements Serializable {

    public String DAH_01;
    public String DAH_02;
    public String DAH_14;

    public FilterVO() {
    }

    public FilterVO(String DAH_01, String DAH_02, String DAH_14) {

        this.DAH_01 = DAH_01;
        this.DAH_02 = DAH_02;
        this.DAH_14 = DAH_14;
    }


}
