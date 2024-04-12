package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.dmjs.model.vo.LED_VO;
import kr.hmit.base.model.BaseModel;

public class LED_Model extends BaseModel implements Serializable {
    public ArrayList<LED_VO> Data;
}
