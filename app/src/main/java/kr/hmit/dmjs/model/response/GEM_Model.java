package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.dmjs.model.vo.GEM_VO;
import kr.hmit.base.model.BaseModel;

public class GEM_Model extends BaseModel implements Serializable {
    public ArrayList<GEM_VO> Data;
}
