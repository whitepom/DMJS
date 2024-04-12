package kr.hmit.dmjs.model.vo;

import java.io.Serializable;

public class WKSM_VO implements Serializable {

    public String WKSM_ID;
    public String WKSM_01;
    public String WKSM_02;
    public String WKSM_03;
    public String WKSM_04;
    public String WKSM_05;

    public String MEM_02;
    public String PCB_02;
    public boolean isSelected;


    public boolean Validation;
    public String ErrorCode;

    public WKSM_VO(String WKSM_02, String MEM_02, String PCB_02) {
        this.WKSM_02 = WKSM_02;
        this.MEM_02 = MEM_02;
        this.PCB_02 = PCB_02;
    }

    public WKSM_VO(){

    }
}
