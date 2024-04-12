package kr.hmit.base.network;

import kr.hmit.base.model.LoginModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;


public class Http extends HttpBaseService {
    private static final String MEMBER_URL = "Mobile";

    //--------------------------------------------------
    // 회원
    //--------------------------------------------------
    public static MEM_SELECT member(TYPE type, String host) {
        return (MEM_SELECT) retrofit(MEM_SELECT.class, type, host);
    }

    public interface MEM_SELECT {
        /**
         * 로그인1
         */
        @FormUrlEncoded
        @POST("{host}/" + MEMBER_URL + "/MEM_SELECT")
        Call<LoginModel> login(
                @Path(value = "host", encoded = true) String host,
                @Field(value = "GUBUN") String GUBUN,
                @Field(value = "MEM_CID") String MEM_CID,
                @Field(value = "MEM_01") String MEM_01,
                @Field(value = "MEM_03") String MEM_03
        );
    }

}