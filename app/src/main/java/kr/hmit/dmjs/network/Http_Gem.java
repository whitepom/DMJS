package kr.hmit.dmjs.network;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.model.vo.GEM_VO;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public class Http_Gem extends HttpBaseService {
    private static final String Mobile_URL = "Z04012/Z04012CMobile";

    public static Http_Gem.GEM gem(TYPE type, String host) {return (GEM) retrofit(Http_Gem.GEM.class, type, host);}

    public interface GEM {
        @GET("{host}/" + Mobile_URL + "/GEM_READ_API")
        Call<ArrayList<GEM_VO>> GEM_READ_API(
                @Path(value = "host", encoded = true) String host,
                @QueryMap HashMap<String, Object> paramMap
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/GEM_U")
        Call<GEM_VO> GEM_U(
                @Path(value = "host", encoded = true) String host,
                @FieldMap HashMap<String, Object> paramMap
        );
    }
}