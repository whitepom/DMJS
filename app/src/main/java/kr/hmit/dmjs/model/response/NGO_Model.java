package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.base.model.BaseModel;
import kr.hmit.dmjs.model.vo.NGO_VO;


public class NGO_Model extends BaseModel implements Serializable {
    public ArrayList<NGO_VO> Data;
}
