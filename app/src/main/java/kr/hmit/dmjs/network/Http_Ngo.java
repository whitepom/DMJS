package kr.hmit.dmjs.network;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.model.vo.NGM_VO;
import kr.hmit.dmjs.model.vo.NGO_VO;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public class Http_Ngo extends HttpBaseService {
    private static final String Mobile_URL = "Z04012/Z04012CMobile";


    public static NGO ngo(TYPE type, String host) {
        return (NGO) retrofit(NGO.class, type, host);
    }

    public interface NGO {
        @GET("{host}/" + Mobile_URL + "/NGO_Read")
        Call<ArrayList<NGO_VO> > NGO_Read(
                @Path(value = "host", encoded = true) String host,
                @QueryMap HashMap<String, Object> paramMap
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/NGO_U")
        Call<NGO_VO> NGO_U(
                @Path(value = "host", encoded = true) String host,
                @FieldMap HashMap<String, Object> paramMap
        );
    }
}