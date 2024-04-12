package kr.hmit.dmjs.network;

import kr.hmit.base.model.BaseModel;

public class RestResult {
    public final BaseModel mData;

    public RestResult(BaseModel baseModel) {
        mData = baseModel;
    }

    public static RestResult from(BaseModel baseModel) {
        return new RestResult(baseModel);
    }
}
