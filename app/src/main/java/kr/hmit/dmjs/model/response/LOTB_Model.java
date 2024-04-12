package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.dmjs.model.vo.LOTB_VO;
import kr.hmit.base.model.BaseModel;

public class LOTB_Model extends BaseModel implements Serializable {
    public ArrayList<LOTB_VO> Data;
}
