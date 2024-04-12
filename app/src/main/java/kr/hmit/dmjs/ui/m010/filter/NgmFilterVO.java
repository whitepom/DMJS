package kr.hmit.dmjs.ui.m010.filter;

import java.io.Serializable;

public class NgmFilterVO implements Serializable {


    public String NGM_02_ST;
    public String NGM_02_ED;
    public String NGM_07;

    public String getNGM_02_ST() {
        return NGM_02_ST;
    }

    public void setNGM_02_ST(String NGM_02_ST) {
        this.NGM_02_ST = NGM_02_ST;
    }

    public String getNGM_02_ED() {
        return NGM_02_ED;
    }

    public void setNGM_02_ED(String NGM_02_ED) {
        this.NGM_02_ED = NGM_02_ED;
    }

    public String getNGM_07() {
        return NGM_07;
    }

    public void setNGM_07(String NGM_07) {
        this.NGM_07 = NGM_07;
    }

    public NgmFilterVO() {
    }

}
