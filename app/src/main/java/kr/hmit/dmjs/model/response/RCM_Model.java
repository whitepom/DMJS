package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.base.model.BaseModel;
import kr.hmit.dmjs.model.vo.RCM_VO;

public class RCM_Model extends BaseModel implements Serializable {
    public ArrayList<RCM_VO> Data;
}
