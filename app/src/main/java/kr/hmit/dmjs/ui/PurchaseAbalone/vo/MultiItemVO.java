package kr.hmit.dmjs.ui.PurchaseAbalone.vo;

import java.io.Serializable;

public class MultiItemVO implements Serializable {



    public String NGG_04; //품목
    public String NGG_04_NM;
    public String NGG_05; //단가
    public String NGG_06; //박스수량
    public String NGG_07; //수매금액
    public String NGG_08; //박스당 중량
    public String NGG_0901; //총중량
    public String NGG_0902; //실총중량
    public String NGG_97; //
    public MultiItemVO() {
    }

    public MultiItemVO(String NGG_04, String NGG_04_NM, String NGG_05
            ,  String NGG_06, String NGG_07, String NGG_08, String NGG_0901, String NGG_0902
    ,String NGG_97) {

        this.NGG_04 = NGG_04;
        this.NGG_04_NM = NGG_04_NM;
        this.NGG_05 = NGG_05;
        this.NGG_06 = NGG_06;
        this.NGG_07 = NGG_07;
        this.NGG_08 = NGG_08;
        this.NGG_0901 = NGG_0901;
        this.NGG_0902 = NGG_0902;
        this.NGG_97 = NGG_97;

    }


}
