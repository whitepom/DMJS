package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.dmjs.model.vo.CALC_VO;
import kr.hmit.base.model.BaseModel;

public class CALC_Model extends BaseModel implements Serializable {
    public ArrayList<CALC_VO> Data;
}
