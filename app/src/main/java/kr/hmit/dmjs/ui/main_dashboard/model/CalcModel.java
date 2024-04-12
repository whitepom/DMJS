package kr.hmit.dmjs.ui.main_dashboard.model;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.dmjs.model.vo.CALC_VO;

public class CalcModel implements Serializable {
    private static final long serialVersionUID = 404555499218760852L;

    public ArrayList<CALC_VO> Data;
    public int Total;
    public String Errors;
}
