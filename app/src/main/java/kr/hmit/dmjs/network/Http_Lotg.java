package kr.hmit.dmjs.network;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.model.vo.LOTG_VO;
import kr.hmit.dmjs.model.vo.LOT_VO;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public class Http_Lotg extends HttpBaseService {
    private static final String Mobile_URL = "Z04012/Z04012CMobile";

    public static Http_Lotg.LOTG lotg(TYPE type, String host) { return (Http_Lotg.LOTG) retrofit(Http_Lotg.LOTG.class, type, host);}

    public interface LOTG {
        @GET("{host}/" + Mobile_URL + "/LOTG_Read")
        Call<ArrayList<LOTG_VO> > LOTG_Read(
                @Path(value = "host", encoded = true) String host,
                @QueryMap HashMap<String, Object> paramMap
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/LOTG_U")
        Call<LOTG_VO> LOTG_U(
                @Path(value = "host", encoded = true) String host,
                @FieldMap HashMap<String, Object> paramMap
        );
    }
}