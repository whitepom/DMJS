package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.dmjs.model.vo.LOT_VO;
import kr.hmit.base.model.BaseModel;

public class LOT_Model extends BaseModel implements Serializable {
    public ArrayList<LOT_VO> Data;
}
