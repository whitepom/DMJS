package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.base.model.BaseModel;
import kr.hmit.dmjs.model.vo.MAIN_VO;
import kr.hmit.dmjs.model.vo.MEM_VO;

public class MEM_Model extends BaseModel implements Serializable {
    public ArrayList<MEM_VO> Data;

}