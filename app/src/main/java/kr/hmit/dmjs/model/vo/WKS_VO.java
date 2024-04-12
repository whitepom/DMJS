package kr.hmit.dmjs.model.vo;

import java.io.Serializable;

public class WKS_VO implements Serializable {
    private static final long serialVersionUID = -5401691036848387225L;

     public String WKS_ID;
    /**
     * 글번호
     */
    public String WKS_01;
    /**
     * 요청일자
     */
    public String WKS_02;
    /**처리구분*/
    public String WKS_03;
    public String WKS_0302;
    /**
     * 업무내용
     */
    public String WKS_04;
    /**
     * 업무구분
     */
    public String WKS_05;
    /**
     * maybe 완료일자?
     */
    public String WKS_06;
    public String WKS_07;
    public String WKS_08; // 관리자 코멘트

    public String WKS_1001;
    public String WKS_1002;
    public String WKS_1101;
    public String WKS_1102;
    public String WKS_1201;
    public String WKS_1202;
    public String WKS_1301;
    public String WKS_1302;
    public String WKS_1401;
    public String WKS_1402;
//    public String WKS_7001;
//    public String WKS_7002;
//    public String WKS_7003;
//    public String WKS_97;
    /**
     * 요청자
     */
    public String WKS_98;
    public String WKS_98_02;
    public String WKS_99;
    public String CNT;
    /**
     * 담당자 이름
     */

    public String MEM_02;
    public String MEM_31;
    public String MEM_11; // 관리자


    public String WKS_1001_NM;
    public String WKS_1101_NM;
    public String WKS_1201_NM;
    public String WKS_1301_NM;
    public String WKS_1401_NM;

    public String WKSM_LIST;


    public boolean Validation;
    public String ErrorCode;
}
