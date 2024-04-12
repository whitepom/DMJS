package kr.hmit.dmjs.network;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.model.vo.GAG_VO;
import kr.hmit.dmjs.model.vo.LOT_VO;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public class Http_Gag extends HttpBaseService {
    private static final String Mobile_URL = "Z04012/Z04012CMobile";


    public static GAG gag(TYPE type, String host) {
        return (GAG) retrofit(GAG.class, type, host);
    }

    public interface GAG {
        @GET("{host}/" + Mobile_URL + "/GAG_Read")
        Call<ArrayList<GAG_VO> > GAG_Read(
                @Path(value = "host", encoded = true) String host,
                @QueryMap HashMap<String, Object> paramMap
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/GAG_U")
        Call<GAG_VO> GAG_U(
                @Path(value = "host", encoded = true) String host,
                @FieldMap HashMap<String, Object> paramMap
        );
    }
}