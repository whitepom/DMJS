package kr.hmit.dmjs.network;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public class Http_Zag extends HttpBaseService {
    private static final String Mobile_URL = "Z04012/Z04012CMobile";


    public static ZAG zag(TYPE type, String host) {
        return (ZAG) retrofit(ZAG.class, type, host);
    }

    public interface ZAG {
        @GET("{host}/" + Mobile_URL + "/ZAG_Read")
        Call<ArrayList<ZAG_VO> > ZAG_Read(
                @Path(value = "host", encoded = true) String host,
                @QueryMap HashMap<String, Object> paramMap
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/ZAG_U")
        Call<ZAG_VO> ZAG_U(
                @Path(value = "host", encoded = true) String host,
                @FieldMap HashMap<String, Object> paramMap
        );
    }
}