package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.dmjs.model.vo.LOTW_VO;
import kr.hmit.base.model.BaseModel;

public class LOTW_Model extends BaseModel implements Serializable {
    public ArrayList<LOTW_VO> Data;
}
