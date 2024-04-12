package kr.hmit.dmjs.ui.order_request.model;

import java.io.Serializable;

public class MultiItemVO implements Serializable {

    public String DAH_01;
    public String DAH_02;
    public String DAH_04;
    public int DAH_11;
    public String quantity;
    public String amount;


    public MultiItemVO() {
    }

    public MultiItemVO(String DAH_01, String DAH_02, String DAH_04,int DAH_11,String quantity,String amount) {
        this.DAH_01 = DAH_01;
        this.DAH_02 = DAH_02;
        this.DAH_04 = DAH_04;
        this.DAH_11 = DAH_11;
        this.quantity = quantity;
        this.amount = amount;

    }


}
