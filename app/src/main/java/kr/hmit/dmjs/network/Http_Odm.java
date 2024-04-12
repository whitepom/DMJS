package kr.hmit.dmjs.network;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.model.vo.ODM_VO;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public class Http_Odm extends HttpBaseService {
    private static final String Mobile_URL = "Z04012/Z04012CMobile";

    public static ODM odm(TYPE type, String host) { return (ODM) retrofit(ODM.class, type, host);}

    public interface ODM {
        @GET("{host}/" + Mobile_URL + "/ODM_READ_API")
        Call<ArrayList<ODM_VO>> ODM_READ_API(
                @Path(value = "host", encoded = true) String host,
                @QueryMap HashMap<String, Object> ParamMap
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/ODM_U")
        Call<BaseModel> ODM_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("ODM_ID") String ODM_ID,
                @Field("ODM_01") String ODM_01,
                @Field("ODM_02") String ODM_02,
                @Field("ODM_03") String ODM_03,
                @Field("ODM_04") String ODM_04,
                @Field("ODM_05") String ODM_05,
                @Field("ODM_06") String ODM_06,
                @Field("ODM_07") String ODM_07,
                @Field("ODM_97") String ODM_97,
                @Field("ODM_98") String ODM_98,
                @Field("ODM_99") String ODM_99
        );
    }
}