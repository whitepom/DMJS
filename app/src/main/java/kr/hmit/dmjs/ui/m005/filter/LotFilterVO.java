package kr.hmit.dmjs.ui.m005.filter;

import java.io.Serializable;

public class LotFilterVO implements Serializable {


    public String LOT_02_ST;
    public String LOT_02_ED;

    public String LOT_18;

    public String getLOT_02_ST() {
        return LOT_02_ST;
    }

    public void setLOT_02_ST(String LOT_02_ST) {
        this.LOT_02_ST = LOT_02_ST;
    }

    public String getLOT_02_ED() {
        return LOT_02_ED;
    }

    public void setLOT_02_ED(String LOT_02_ED) {
        this.LOT_02_ED = LOT_02_ED;
    }

    public String getLOT_18() {
        return LOT_18;
    }

    public void setLOT_18(String LOT_18) {
        this.LOT_18 = LOT_18;
    }

    public LotFilterVO() {
    }

}
