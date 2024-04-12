package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.dmjs.model.vo.CAL_VO;
import kr.hmit.base.model.BaseModel;

public class CAL_Model extends BaseModel implements Serializable {
    public ArrayList<CAL_VO> Data;
}
