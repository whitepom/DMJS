package kr.hmit.dmjs.ui.release.model;
import java.io.Serializable;
public class FilterVO implements Serializable  {

    public String CLT_01;
    public String CLT_02;
    public String RUN_02_ST;
    public String RUN_02_ED;
    public String DAH_01;
    public FilterVO() {
    }

    public FilterVO(String CLT_02,String RUN_02_ST,String RUN_02_ED) {
        this.CLT_02 = CLT_02;
        this.RUN_02_ST=RUN_02_ST;
        this.RUN_02_ED=RUN_02_ED;
    }
    public FilterVO(String CLT_02, String CLT_01){
        this.CLT_02=CLT_02;
        this.CLT_01=CLT_01;
    }
    public FilterVO(String CLT_01, String CLT_02,String RUN_02_ST,String RUN_02_ED,String DAH_01) {
        this.CLT_01 = CLT_01;
        this.CLT_02 = CLT_02;
        this.RUN_02_ST=RUN_02_ST;
        this.RUN_02_ED=RUN_02_ED;
        this.DAH_01=DAH_01;
    }
}
