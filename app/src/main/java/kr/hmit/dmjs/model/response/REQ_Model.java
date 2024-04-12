package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.dmjs.model.vo.REQ_VO;
import kr.hmit.base.model.BaseModel;

public class REQ_Model extends BaseModel implements Serializable {
    public ArrayList<REQ_VO> Data;

}
