package kr.hmit.dmjs.ui.stockStatus.model;

import java.io.Serializable;

public class FilterVO implements Serializable {

    public String OOK_02_ST;
    public String OOK_02_ED;
    public String OOK_04;


    public FilterVO(String OOK_02_ST, String OOK_02_ED,String OOK_04) {
        this.OOK_02_ST = OOK_02_ST;
        this.OOK_02_ED = OOK_02_ED;
        this.OOK_04 = OOK_04;
    }

}
