package kr.hmit.dmjs.network;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.model.response.ODD_Model;
import kr.hmit.dmjs.model.response.WHC_Model;
import kr.hmit.dmjs.model.vo.ODD_VO;
import kr.hmit.dmjs.model.vo.WHC_VO;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public class Http_Odd extends HttpBaseService {
    private static final String Mobile_URL = "Z04012/Z04012CMobile";

    public static Http_Odd.ODD odd(TYPE type, String host) {return (ODD) retrofit(Http_Odd.ODD.class, type, host);}

    public interface ODD {
        @GET("{host}/" + Mobile_URL + "/ODD_READ_API")
        Call<ArrayList<ODD_VO>> ODD_READ_API(
                @Path(value = "host", encoded = true) String host,
                @QueryMap HashMap<String, Object> ParamMap
        );

        @GET("{host}/" + Mobile_URL + "/WHC_Read")
        Call<ArrayList<WHC_VO>> WHC_Read(
                @Path(value = "host", encoded = true) String host,
                @QueryMap HashMap<String, Object> ParamMap
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/ODD_U")
        Call<BaseModel> ODD_U(
                @Path(value = "host", encoded = true) String host,
                @FieldMap  HashMap<String, Object> ParamMap
        );
        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/ODD_L_Edit")
        Call<BaseModel> ODD_L_Edit(
                @Path(value = "host", encoded = true) String host,
                @FieldMap  HashMap<String, Object> ParamMap
        );

    }
}