package kr.hmit.dmjs.ui.worker_code.model;

import java.io.Serializable;

public class FilterVO implements Serializable {

    public String WOC_02;
    public String WOC_09;
    public String WOC_10;

    public FilterVO() {
    }

    public FilterVO(String WOC_02, String WOC_09, String WOC_10) {

        this.WOC_02 = WOC_02;
        this.WOC_09 = WOC_09;
        this.WOC_10 = WOC_10;
    }


}
