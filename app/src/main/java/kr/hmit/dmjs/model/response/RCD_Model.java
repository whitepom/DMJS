package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.base.model.BaseModel;
import kr.hmit.dmjs.model.vo.RCD_VO;

public class RCD_Model extends BaseModel implements Serializable {
    public ArrayList<RCD_VO> Data;
}
