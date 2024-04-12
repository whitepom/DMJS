package kr.hmit.dmjs.ui.stockage_list.model;

import java.io.Serializable;

public class FilterVO implements Serializable {

    public String OOK_05;
    public String CDO_03;
    public String OOK_02_ST;
    public String OOK_02_ED;
    public String OOK_04;

    public FilterVO() {
    }

    public FilterVO(String OOK_05, String CDO_03, String OOK_02_ST, String OOK_02_ED) {
        this.OOK_05 = OOK_05;
        this.CDO_03 = CDO_03;
        this.OOK_02_ST = OOK_02_ST;
        this.OOK_02_ED = OOK_02_ED;
    }
    public FilterVO(String OOK_04, String OOK_02_ST, String OOK_02_ED) {
        this.OOK_04 = OOK_04;
        this.OOK_02_ST = OOK_02_ST;
        this.OOK_02_ED = OOK_02_ED;
    }
}
