package kr.hmit.dmjs.ui.work_management.model;

import java.io.Serializable;

public class FilterVO implements Serializable {

    public String WKS_1001;
    public String WKS_98;
    public String WKS_05;


    public FilterVO(String wKS_1001, String wKS_98,String wKS_05) {
        WKS_1001 = wKS_1001;
        WKS_98 = wKS_98;
        WKS_05=wKS_05;
    }

}
