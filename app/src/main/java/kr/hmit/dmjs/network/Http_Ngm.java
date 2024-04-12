package kr.hmit.dmjs.network;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.model.vo.NGM_VO;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public class Http_Ngm extends HttpBaseService {
    private static final String Mobile_URL = "Z04012/Z04012CMobile";


    public static NGM ngm(TYPE type, String host) {
        return (NGM) retrofit(NGM.class, type, host);
    }

    public interface NGM {
        @GET("{host}/" + Mobile_URL + "/NGM_Read")
        Call<ArrayList<NGM_VO> > NGM_Read(
                @Path(value = "host", encoded = true) String host,
                @QueryMap HashMap<String, Object> paramMap
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/NGM_U")
        Call<NGM_VO> NGM_U(
                @Path(value = "host", encoded = true) String host,
                @FieldMap HashMap<String, Object> paramMap
        );
    }
}