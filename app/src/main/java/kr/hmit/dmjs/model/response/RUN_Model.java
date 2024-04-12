package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.base.model.BaseModel;
import kr.hmit.dmjs.model.vo.RUN_VO;

public class RUN_Model extends BaseModel implements Serializable {
    public ArrayList<RUN_VO> Data;
}
