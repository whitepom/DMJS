package kr.hmit.dmjs.network;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.model.vo.NGGK_VO;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public class Http_Nggk extends HttpBaseService {
    private static final String Mobile_URL = "Z04012/Z04012CMobile";


    public static NGGK nggk(TYPE type, String host) {
        return (NGGK) retrofit(NGGK.class, type, host);
    }

    public interface NGGK {
        @GET("{host}/" + Mobile_URL + "/NGGK_Read")
        Call<ArrayList<NGGK_VO> > NGGK_Read(
                @Path(value = "host", encoded = true) String host,
                @QueryMap HashMap<String, Object> paramMap
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/NGGK_U")
        Call<NGGK_VO> NGGK_U(
                @Path(value = "host", encoded = true) String host,
                @FieldMap HashMap<String, Object> paramMap
        );
    }
}