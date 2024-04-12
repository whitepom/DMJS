package kr.hmit.dmjs.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.dmjs.ui.worker_code.model.WorkerCodeVO;
import kr.hmit.base.model.BaseModel;

public class WorkerCode_Model extends BaseModel implements Serializable {
    private static final long serialVersionUID = 6696987944400579024L;
    public ArrayList<WorkerCodeVO> Data;

}
