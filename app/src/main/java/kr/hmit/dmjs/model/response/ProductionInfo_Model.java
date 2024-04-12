package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import kr.hmit.base.model.BaseModel;

public class ProductionInfo_Model extends BaseModel implements Serializable {
    public ArrayList<ProductionInfoVO> Data;

}
