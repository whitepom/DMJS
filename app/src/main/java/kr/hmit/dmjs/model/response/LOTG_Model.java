package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.dmjs.model.vo.LOTG_VO;
import kr.hmit.base.model.BaseModel;

public class LOTG_Model extends BaseModel implements Serializable {
    public ArrayList<LOTG_VO> Data;
}
