package kr.hmit.dmjs.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.model.response.BAS_Model;
import kr.hmit.dmjs.model.response.BMD_Model;
import kr.hmit.dmjs.model.response.BMM_Model;
import kr.hmit.dmjs.model.response.BRDC_Model;
import kr.hmit.dmjs.model.response.BRD_Model;
import kr.hmit.dmjs.model.response.CALC_Model;
import kr.hmit.dmjs.model.response.CAL_Model;
import kr.hmit.dmjs.model.response.CLT_Model;
import kr.hmit.dmjs.model.response.CODE_Model;
import kr.hmit.dmjs.model.response.FIL_Model;
import kr.hmit.dmjs.model.response.GCM_Model;
import kr.hmit.dmjs.model.response.GEM_Model;
import kr.hmit.dmjs.model.response.LED_Model;
import kr.hmit.dmjs.model.response.MAIN_Model;
import kr.hmit.dmjs.model.response.MEM_Model;
import kr.hmit.dmjs.model.response.MEM_ReadModel;
import kr.hmit.dmjs.model.response.MSM_Model;
import kr.hmit.dmjs.model.response.NGGK_Model;
import kr.hmit.dmjs.model.response.NGG_Model;
import kr.hmit.dmjs.model.response.NGO_Model;
import kr.hmit.dmjs.model.response.ODD_Model;
import kr.hmit.dmjs.model.response.ODM_Model;
import kr.hmit.dmjs.model.response.OOK_Model;
import kr.hmit.dmjs.model.response.ProductionInfo_Model;
import kr.hmit.dmjs.model.response.RCD_Model;
import kr.hmit.dmjs.model.response.RCM_Model;
import kr.hmit.dmjs.model.response.REM_Model;
import kr.hmit.dmjs.model.response.REQ_Model;
import kr.hmit.dmjs.model.response.RUM_Model;
import kr.hmit.dmjs.model.response.RUN2_Model;
import kr.hmit.dmjs.model.response.RUN_Model;
import kr.hmit.dmjs.model.response.TYC_Model;
import kr.hmit.dmjs.model.response.UNO_Model;
import kr.hmit.dmjs.model.response.WHC_Model;
import kr.hmit.dmjs.model.response.WKSM_Model;
import kr.hmit.dmjs.model.response.WKS_Model;
import kr.hmit.dmjs.model.response.WorkerCode_Model;
import kr.hmit.dmjs.model.vo.GAG_VO;
import kr.hmit.dmjs.model.vo.LOT_VO;
import kr.hmit.dmjs.model.vo.RUN2_VO;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public class Http_Lot extends HttpBaseService {
    private static final String Mobile_URL = "Z04012/Z04012CMobile";

    public static LOT lot(TYPE type, String host) { return (LOT) retrofit(LOT.class, type, host);}

    public interface LOT {
        @GET("{host}/" + Mobile_URL + "/LOT_Read")
        Call<ArrayList<LOT_VO> > LOT_Read(
                @Path(value = "host", encoded = true) String host,
                @QueryMap HashMap<String, Object> paramMap
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/LOT_U")
        Call<LOT_VO> LOT_U(
                @Path(value = "host", encoded = true) String host,
                @FieldMap HashMap<String, Object> paramMap
        );
    }
}