package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.dmjs.model.vo.WKS_VO;
import kr.hmit.base.model.BaseModel;

public class WKS_Model extends BaseModel implements Serializable {
    private static final long serialVersionUID = 6696987944400579024L;

    public ArrayList<WKS_VO> Data;
}
