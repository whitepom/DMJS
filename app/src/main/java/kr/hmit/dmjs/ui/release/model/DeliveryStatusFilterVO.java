package kr.hmit.dmjs.ui.release.model;
import java.io.Serializable;

public class DeliveryStatusFilterVO implements Serializable  {

    public String RUN_03;
    public String RUN_02_ST;
    public String RUN_02_ED;
    public DeliveryStatusFilterVO() {
    }

    public DeliveryStatusFilterVO(String RUN_03, String RUN_02_ST, String RUN_02_ED) {
        this.RUN_03 = RUN_03;
        this.RUN_02_ST = RUN_02_ST;
        this.RUN_02_ED = RUN_02_ED;
    }

}
