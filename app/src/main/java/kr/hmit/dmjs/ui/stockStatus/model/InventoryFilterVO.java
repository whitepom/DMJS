package kr.hmit.dmjs.ui.stockStatus.model;

import java.io.Serializable;

public class InventoryFilterVO implements Serializable {

    public String DAH_03;
    public String DAH_10;
    public String OOK_06;
    public String OOK_02_ST;
    public String OOK_02_ED;



    public InventoryFilterVO(String OOK_0201, String OOK_0202, String OOK_04) {
        this.DAH_03 = DAH_03;
        this.DAH_10 = DAH_10;
        this.OOK_06 = OOK_06;
        this.OOK_02_ST = OOK_02_ST;
        this.OOK_02_ED = OOK_02_ED;
    }

}
