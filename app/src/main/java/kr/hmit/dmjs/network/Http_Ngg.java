package kr.hmit.dmjs.network;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.model.vo.NGG_VO;
import kr.hmit.dmjs.model.vo.NGO_VO;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public class Http_Ngg extends HttpBaseService {
    private static final String Mobile_URL = "Z04012/Z04012CMobile";


    public static NGG ngg(TYPE type, String host) {
        return (NGG) retrofit(NGG.class, type, host);
    }

    public interface NGG {
        @GET("{host}/" + Mobile_URL + "/NGG_Read")
        Call<ArrayList<NGG_VO> > NGG_Read(
                @Path(value = "host", encoded = true) String host,
                @QueryMap HashMap<String, Object> paramMap
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/NGG_U")
        Call<NGG_VO> NGG_U(
                @Path(value = "host", encoded = true) String host,
                @FieldMap HashMap<String, Object> paramMap
        );
    }
}