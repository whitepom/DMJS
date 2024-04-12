package kr.hmit.dmjs.model.vo;

import java.io.Serializable;
import java.util.Comparator;

public class CALC_VO implements Serializable {
    /**
     * 기업 ID
     */
    public String CALC_ID;
    /**
     * SN
     */
    public int CALC_01;
    /**
     * 날짜
     */
    public String CALC_02;
    /**
     * 내용
     */
    public String CALC_03;
    public String CALC_04;
    public boolean Validation;
    public String SUCCESS;
    public String ERROR_MSG;

    public static class AscendingDate implements Comparator<CALC_VO> {

        @Override
        public int compare(CALC_VO o1, CALC_VO o2) {
            return o1.CALC_02.compareTo(o2.CALC_02);
        }
    }
}
