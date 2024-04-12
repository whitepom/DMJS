package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.dmjs.model.vo.LGC_VO;
import kr.hmit.base.model.BaseModel;

public class LGC_Model extends BaseModel implements Serializable {
    public ArrayList<LGC_VO> Data;
}
