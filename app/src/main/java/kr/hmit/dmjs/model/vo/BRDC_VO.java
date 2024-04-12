package kr.hmit.dmjs.model.vo;

import java.io.Serializable;

public class BRDC_VO implements Serializable {

    public String BRDC_CID;
    public int BRDC_01;
    public int BRDC_02;
    public String BRDC_03;
    public String BRDC_04;
    public String BRDC_05;
    public String BRDC_06;
    public String BRDC_07;
    public String BRDC_08;
    public String BRDC_08_TR;

    // 별도 value
    public String MEM_01; // 담당자(작성자) 명
    public String MEM_02; // 담당자(작성자) 명
    public String MEM_31; // 부서명
    public String MEM_32; // 직급명


    //Mobile
    public boolean Validation;
    public String SUCCESS;
    public String ERROR_MSG;


}
