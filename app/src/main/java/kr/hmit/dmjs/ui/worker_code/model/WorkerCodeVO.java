package kr.hmit.dmjs.ui.worker_code.model;

import java.io.Serializable;

public class WorkerCodeVO implements Serializable {
    private static final long serialVersionUID = -4546486917987365816L;

    public String WOC_ID;
    public String WOC_01;
    /**
     * 작업자명
     */
    public String WOC_02;
    public String WOC_03;
    /**
     * 성별(M 남, F 여)
     */
    public String WOC_04;
    /**
     * 근무구분(1 정규, 2 일용)
     */
    public String WOC_05;
    public boolean WOC_06;
    public String WOC_0701;
    public String WOC_0702;
    /**
     * 거주지
     */
    public String WOC_08;
    /**
     * 생산공정
     */
    public String WOC_09;
    /**
     * 직급
     */
    public String WOC_10;
    /**
     * 연락처
     */
    public String WOC_11;
    /**
     * 보건증 만료일자
     */
    public String WOC_12;
    /**
     * 비상연락
     */
    public String WOC_13;
    public String WOC_14;
    public int WOC_80;
    public String WOC_98;
    public String WOC_99;
    public String RetVal;
    public String MEM_02;
    public String MEM_32;
    public boolean isChecked;
    public String ParentLOT_01;
    public String DELTECHECK_MESSAGE;
    public boolean Validation;
    public String SUCCESS;
    public String ErrorCode;

    public WorkerCodeVO() {
    }

    public WorkerCodeVO(String WOC_ID, String WOC_01) {
        this.WOC_ID = WOC_ID;
        this.WOC_01 = WOC_01;
    }
}
