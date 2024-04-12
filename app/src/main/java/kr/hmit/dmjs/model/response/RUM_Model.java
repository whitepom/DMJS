package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.base.model.BaseModel;
import kr.hmit.dmjs.model.vo.RCD_VO;
import kr.hmit.dmjs.model.vo.RUM_VO;

public class RUM_Model extends BaseModel implements Serializable {
    public ArrayList<RUM_VO> Data;
}
