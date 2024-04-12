package kr.hmit.dmjs.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.hmit.dmjs.model.response.BAS_Model;
import kr.hmit.dmjs.model.response.BMD_Model;
import kr.hmit.dmjs.model.response.BMM_Model;
import kr.hmit.dmjs.model.response.BRDC_Model;
import kr.hmit.dmjs.model.response.BRD_Model;
import kr.hmit.dmjs.model.response.CAL_Model;
import kr.hmit.dmjs.model.response.CALC_Model;
import kr.hmit.dmjs.model.response.CLT_Model;
import kr.hmit.dmjs.model.response.CODE_Model;
import kr.hmit.dmjs.model.response.FIL_Model;
import kr.hmit.dmjs.model.response.GCM_Model;
import kr.hmit.dmjs.model.response.GEM_Model;
import kr.hmit.dmjs.model.response.LED_Model;
import kr.hmit.dmjs.model.response.LOTB_Model;
import kr.hmit.dmjs.model.response.LOTG_Model;
import kr.hmit.dmjs.model.response.LOTW_Model;
import kr.hmit.dmjs.model.response.LOT_Model;
import kr.hmit.dmjs.model.response.MAIN_Model;
import kr.hmit.dmjs.model.response.MEM_Model;
import kr.hmit.dmjs.model.response.MEM_ReadModel;
import kr.hmit.dmjs.model.response.MSM_Model;
import kr.hmit.dmjs.model.response.NGGK_Model;
import kr.hmit.dmjs.model.response.NGG_Model;
import kr.hmit.dmjs.model.response.NGO_Model;
import kr.hmit.dmjs.model.response.ODD_Model;
import kr.hmit.dmjs.model.response.ODM_Model;
import kr.hmit.dmjs.model.response.OOK_Model;
import kr.hmit.dmjs.model.response.ProductionInfo_Model;
import kr.hmit.dmjs.model.response.RCD_Model;
import kr.hmit.dmjs.model.response.RCM_Model;
import kr.hmit.dmjs.model.response.REM_Model;
import kr.hmit.dmjs.model.response.REQ_Model;
import kr.hmit.dmjs.model.response.RUN2_Model;
import kr.hmit.dmjs.model.response.RUN_Model;
import kr.hmit.dmjs.model.response.TYC_Model;
import kr.hmit.dmjs.model.response.RUM_Model;
import kr.hmit.dmjs.model.response.UNO_Model;
import kr.hmit.dmjs.model.response.WHC_Model;
import kr.hmit.dmjs.model.response.WKSM_Model;
import kr.hmit.dmjs.model.response.WKS_Model;
import kr.hmit.dmjs.model.response.WorkerCode_Model;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.model.vo.GAG_VO;
import kr.hmit.dmjs.model.vo.LOT_VO;
import kr.hmit.dmjs.model.vo.RUN2_VO;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public class Http extends HttpBaseService {
    private static final String Mobile_URL = "Z04012/Z04012CMobile";

    public static MAIN main(TYPE type, String host) {return (MAIN) retrofit(MAIN.class, type, host);}

    public static WKS wks(TYPE type, String host) {
        return (WKS) retrofit(WKS.class, type, host);
    }
    public static UNO uno(TYPE type, String host) {
        return (UNO) retrofit(UNO.class, type, host);
    }

    public static BRD brd(TYPE type, String host) {
        return (BRD) retrofit(BRD.class, type, host);
    }

    public static RCD rcd(TYPE type, String host) {
        return (RCD) retrofit(RCD.class, type, host);
    }

    public static BRDC brdc(TYPE type, String host) { return (BRDC) retrofit(BRDC.class, type, host); }

    public static FIL fil(TYPE type, String host) { return (FIL) retrofit(FIL.class, type, host); }

    public static WOC woc(TYPE type, String host) {
        return (WOC) retrofit(WOC.class, type, host);
    }
    public static MEM mem(TYPE type, String host) {
        return (MEM) retrofit(MEM.class, type, host);
    }

    public static CLT clt(TYPE type, String host) { return (CLT) retrofit(CLT.class, type, host);}

    public static DAH dah(TYPE type, String host) {
        return (DAH) retrofit(DAH.class, type, host);
    }
    public static GEM gem(TYPE type, String host) {
        return (GEM) retrofit(GEM.class, type, host);
    }
    public static ODM odm(TYPE type, String host) {
        return (ODM) retrofit(ODM.class, type, host);
    }

    public static ODD odd(TYPE type, String host) {
        return (ODD) retrofit(ODD.class, type, host);
    }

    public static BMM bmm(TYPE type, String host) {
        return (BMM) retrofit(BMM.class, type, host);
    }

    public static MSM msm(TYPE type, String host) {
        return (MSM) retrofit(MSM.class, type, host);
    }



    public static REM rem(TYPE type,String host){return (REM) retrofit(REM.class,type,host);}

    public static RUN run (TYPE type,String host){return (RUN) retrofit(RUN.class,type,host);}
    public static RUN2 run2 (TYPE type,String host){return (RUN2) retrofit(RUN2.class,type,host);}
    public static REQ req(TYPE type, String host) {
        return (REQ) retrofit(REQ.class, type, host);
    }

    public static NGG ngg(TYPE type, String host) {
        return (NGG) retrofit(NGG.class, type, host);
    }
    public static NGGK nggk(TYPE type, String host) {
        return (NGGK) retrofit(NGGK.class, type, host);
    }

    public static NGO ngo(TYPE type, String host) { return (NGO) retrofit(NGO.class, type, host);
    }
    public static OOK ook(TYPE type, String host) {
        return (OOK) retrofit(OOK.class, type, host);
    }

    public static CAL cal(TYPE type, String host) {
        return (CAL) retrofit(CAL.class, type, host);
    }

    public static LED led(TYPE type, String host) {
        return (LED) retrofit(LED.class, type, host);

    }

    public static GCM gcm(TYPE type, String host) { return (GCM) retrofit(GCM.class, type, host);
    }

    public interface FIL {
        @GET("{host}/" + Mobile_URL + "/FIL_Read")
        Call<FIL_Model> FIL_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("FIL_01") int FIL_01,
                @Query("FIL_7004") String FIL_7004

        );
        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/FIL_U")
        Call<BaseModel> FIL_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("FIL_ID") String FIL_ID,
                @Field("FIL_01") String FIL_01,
                @Field("FIL_02") int FIL_02,
                @Field("FIL_7001") String FIL_7001,
                @Field("FIL_7002") int FIL_7002,
                @Field("FIL_7003") String FIL_7003,
                @Field("FIL_7004") String FIL_7004,
                @Field("FIL_98") String FIL_98
        );

    }

    public interface UNO {
        @GET("{host}/" + Mobile_URL + "/UNO_Read")
        Call<UNO_Model> UNO_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("UNO_CID") String UNO_CID,

                @Query("UNO_04") String UNO_04

        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/UNO_U")
        Call<BaseModel> UNO_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("UNO_CID") String UNO_CID,
                @Field("UNO_01") int UNO_01,
                @Field("UNO_02") String UNO_02,
                @Field("UNO_03") String UNO_03,
                @Field("UNO_04") String UNO_04,
                @Field("UNO_05") String UNO_05,
                @Field("UNO_98") String UNO_98,
                @Field("TMP_01") String TMP_01,
                @Field("TMP_02") String TMP_02,
                @Field("TMP_03") String TMP_03,
                @Field("TMP_04") String TMP_04
        );
    }

    public interface RUN{

        @GET("{host}/" + Mobile_URL + "/RUN_Read")
        Call<RUN_Model> RUN_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("RUN_ID") String RUN_ID,
                @Query("RUN_03") String CLT_02,
                @Query("RUN_02_ST") String RUN_02_ST,
                @Query("RUN_02_ED") String RUN_02_ED
                );


        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/RUN_U")
        Call<BaseModel> RUN_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("RUN_ID") String RUN_ID,
                @Field("RUN_01") String RUN_01,
                @Field("RUN_02") String RUN_02, // 출고일자
                @Field("RUN_03") String RUN_03, // 주문처
                @Field("RUN_04") String RUN_04, // 제품
                @Field("RUN_05") double RUN_05, // 단가
                @Field("RUN_06") double RUN_06, // 수량
                @Field("RUN_07") double RUN_07, // 금액
                @Field("RUN_13") String RUN_13, // 거래구분 D:직거래, W:도매, H:홈쇼핑
                @Field("RUN_1501") String RUN_1501, // REQ_01
                @Field("RUN_1701") String RUN_1701, // 입금자
                @Field("RUN_1702") String RUN_1702, // 입금구분
                @Field("RUN_97") String RUN_97,
                @Field("RUN_98") String RUN_98
        );
        @GET("{host}/" + Mobile_URL + "/RUM_Read")
        Call<RUM_Model> RUM_CD(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN);


        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/RUM_U")
        Call<BaseModel> RUM_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("RUM_ID") String RUN_ID,
                @Field("RUM_01") String RUM_01,
                @Field("RUM_02") String RUM_02,    //주문처
                @Field("RUM_03") String RUM_03, //일자
                @Field("RUM_04") String RUM_04, // 주문자
                @Field("RUM_05") String RUM_05, // 주소
                @Field("RUM_06") String RUM_06, //관리구분
                @Field("RUM_07") double RUM_07, // 금액
                @Field("RUM_08") double RUM_08, // 부가세
                @Field("RUM_09") double RUM_09, // 총금액
                @Field("RUM_10") String RUM_10, //주문자/거래처 구분 -> 거래처이면 "Y"
                @Field("RUM_11") String RUM_11, // 제조일자
                @Field("RUM_98") String RUM_98
        );
    }
    public interface RUN2{

        @GET("{host}/" + Mobile_URL + "/RUN_READ_API")
        Call<ArrayList<RUN2_VO>> RUN2_READ_API(
                @Path(value = "host", encoded = true) String host,
                @QueryMap HashMap<String, Object> paramMap
        );

        @GET("{host}/" + Mobile_URL + "/RUN2_Read")
        Call<RUN2_Model> RUN2_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("RUN_ID") String RUN_ID,
                @Query("RUN_03") String CLT_02,
                @Query("RUN_02_ST") String RUN_02_ST,
                @Query("RUN_02_ED") String RUN_02_ED
        );


        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/RUN2_U")
        Call<RUN2_Model> RUN2_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("RUN_ID") String RUN_ID,
                @Field("RUN_01") String RUN_01,
                @Field("RUN_02") String RUN_02, // 출고일자
                @Field("RUN_03") String RUN_03, // 주문자
                @Field("RUN_04") String RUN_04, // 내용    
                @Field("RUN_07") double RUN_07, // 주문금액
                @Field("RUN_08") String RUN_08, // 주문경로
                @Field("RUN_09") String RUN_09, // 품번 없이 품명만 있는 경우 제품명
                @Field("RUN_1701") String RUN_1701, // 입금자
                @Field("RUN_1702") String RUN_1702, // 입금구분
                @Field("RUN_18") String RUN_18, // 우편번호
                @Field("RUN_20") String RUN_20, //택배사
                @Field("RUN_2101") String RUN_2101, //run_03
                @Field("RUN_2201") String RUN_2201, //수취인
                @Field("RUN_2202") String RUN_2202, //연락쳐
                @Field("RUN_2203") String RUN_2203, //주소
                @Field("RUN_23") String RUN_23, //요청사항
                @Field("RUN_24") String RUN_24, //송장번호
                @Field("RUN_25") String RUN_25, //택배비
                @Field("RUN_27") String RUN_27, //품번
                @Field("RUN_98") String RUN_98
        );



    }
    public interface RCD {
        // RCD = 디테일
        @GET("{host}/" + Mobile_URL + "/RCD_Read")
        Call<RCD_Model> RCD_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("RCD_ID") String RCD_ID,
                @Query("RCD_01") String RCD_01,
                @Query("RCD_02") int RCD_02

        );
        //RCM = 메인
        @GET("{host}/" + Mobile_URL + "/RCD_Read")
        Call<RCM_Model> RCM_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("RCD_ID") String RCD_ID,
                @Query("RCD_01") String RCD_01,
                @Query("RCD_02") int RCD_02
        );




    }




    public interface BRD {

        @GET("{host}/" + Mobile_URL + "/BRD_Read")
        Call<BRD_Model> BRD_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("BRD_CID") String BRD_CID,
                @Query("BRD_05") String BRD_05,
                @Query("BRD_07") String BRD_07,
                @Query("BRD_10") String BRD_10

        );

        @Multipart
        @POST("{host}/" + Mobile_URL + "/BRD_U")
        Call<BaseModel> BRD_U(
                @Path(value = "host", encoded = true) String host,
                @PartMap Map<String, RequestBody> info,
                @Part List<MultipartBody.Part> multiFiles
        );

    }
    public interface LED {
        @GET("{host}/" + Mobile_URL + "/MMO_Read")
        Call<LED_Model> MMO_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("LED_ID") String LED_ID,
                @Query("LED_01") String LED_01,
                @Query("LED_04") String LED_04,
                @Query("LED_11") String LED_11,
                @Query("LED_1301") String LED_1301,
                @Query("LED_1302") String LED_1302

        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/MMO_U")
        Call<BaseModel> MMO_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("LED_ID") String LED_ID,
                @Field("LED_01") String LED_01,
                @Field("LED_02") String LED_02,
                @Field("LED_03") String LED_03,
                @Field("LED_04") String LED_04,
                @Field("LED_11") String LED_11,
                @Field("LED_12") String LED_12,
                @Field("LED_21") String LED_21,
                @Field("LED_98") String LED_98

        );

        @GET("{host}/" + Mobile_URL + "/APP_Read")
        Call<LED_Model> APP_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("LED_ID") String LED_ID,
                @Query("LED_01") String LED_01,
                @Query("LED_04") String LED_04,
                @Query("LED_05") String LED_05

        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/APP_U")
        Call<BaseModel> APP_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("LED_ID") String LED_ID,
                @Field("LED_01") String LED_01,
                @Field("LED_02") String LED_02,
                @Field("LED_03") String LED_03,
                @Field("LED_04") String LED_04,
                @Field("LED_10") String LED_10,
                @Field("LED_15") String LED_15



        );
    }
    public interface BRDC {
        @GET("{host}/" + Mobile_URL + "/BRDC_Read")
        Call<BRDC_Model> BRDC_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("BRDC_CID") String BRD_CID,
                @Query("BRDC_01") int BRDC_01,
                @Query("BRDC_02") int BRDC_02
        );
        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/BRDC_U")
        Call<BaseModel> BRDC_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("BRDC_CID") String BRDC_CID,
                @Field("BRDC_01") int BRDC_01,
                @Field("BRDC_02") int BRDC_02,
                @Field("BRDC_03") String BRDC_03,
                @Field("BRDC_04") String BRDC_04,
                @Field("BRDC_05") String BRDC_05,
                @Field("BRDC_06") String BRDC_06
        );

    }

    public interface WKS {
        /**
         * 사용자 리스트
         */
        @GET("{host}/" + Mobile_URL + "/MEM_Read")
        Call<MEM_ReadModel> MEM_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03
        );

        /**
         * 업무분류 리스트
         */
        @GET("{host}/" + Mobile_URL + "/WKS_05")
        Call<WKS_Model> WKS_05(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("WKS_ID") String WKS_ID
        );

        /**
         * 업무관리 리스트
         */
        @GET("{host}/" + Mobile_URL + "/WKS_Read")
        Call<WKS_Model> WKS_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("WKS_ID") String WKS_ID,
                @Query("WKS_03") String WKS_03,
                @Query("WKS_05") String WKS_05,
                @Query("WKS_1001") String WKS_1001,
                @Query("WKS_98_02") String WKS_98
        );

        /**
         * 업무관리 상세
         */
        @GET("{host}/" + Mobile_URL + "/WKS_DETAIL")
        Call<WKS_Model> WKS_DETAIL(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("WKS_ID") String WKS_ID,
                @Query("WKS_01") String WKS_01,
                @Query("WKS_98") String WKS_98
        );

        /**
         * 업무관리 신규,수정,삭제
         */
        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/WKS_U")
        Call<BaseModel> WKS_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("WKS_ID") String WKS_ID,
                @Field("WKS_01") String WKS_01,  
                @Field("WKS_02") String WKS_02,
                @Field("WKS_03") String WKS_03,
                @Field("WKS_04") String WKS_04,
                @Field("WKS_05") String WKS_05,
                @Field("WKS_06") String WKS_06,
                @Field("WKS_07") String WKS_07,
                @Field("WKS_08") String WKS_08,
                @Field("WKS_1001") String WKS_1001,
                @Field("WKS_1002") String WKS_1002,
                @Field("WKS_1101") String WKS_1101,
                @Field("WKS_1102") String WKS_1102,
                @Field("WKS_1201") String WKS_1201,
                @Field("WKS_1202") String WKS_1202,
                @Field("WKS_1301") String WKS_1301,
                @Field("WKS_1302") String WKS_1302,
                @Field("WKS_1401") String WKS_1401,
                @Field("WKS_1402") String WKS_1402,
                @Field("WKS_7001") String WKS_7001,
                @Field("WKS_7002") int WKS_7002,
                @Field("WKS_7003") String WKS_7003,
                @Field("WKS_97") String WKS_97,
                @Field("WKS_98") String WKS_98,
                @Field("WKS_99") String WKS_99
                  // 20211014 추가
        );

        @GET("{host}/" + Mobile_URL + "/WKSM_Read")
        Call<WKSM_Model> WKSM_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("WKSM_ID") String WKSM_ID,
                @Query("WKSM_01") int WKSM_01      // 20211018
        );

        @POST("{host}/" + Mobile_URL + "/WKSM_U")
        Call<WKSM_Model> WKSM_U(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("WKSM_ID") String WKSM_ID,
                @Query("WKSM_01") String WKSM_01,     //20211008
                @Query("WKSM_02") String WKSM_02,
                @Query("WKSM_03") String WKSM_03,
                @Query("WKSM_04") String WKSM_04,
                @Query("WKSM_06") String WKSM_06

        );
    }
    public interface MAIN {
        @GET("{host}/" + Mobile_URL + "/UNO_Read")
        Call<UNO_Model> UNO_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("UNO_CID") String UNO_CID

        );

        @GET("{host}/" + Mobile_URL + "/MAIN_Read")
        Call<MAIN_Model> MAIN_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("COL_ID") String COL_ID
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/BMK_U")
        Call<BaseModel> BMK_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("BMK_ID") String BMK_ID,
                @Field("BMK_01") String BMK_01,
                @Field("BMK_02") String BMK_02,
                @Field("BMK_03") String BMK_03,
                @Field("BMK_04") String BMK_04,
                @Field("BMK_05") String BMK_05,
                @Field("BMK_06") String BMK_06,
                @Field("BMK_98") String BMK_98

        );
    }

    public interface WOC {
        @GET("{host}/" + Mobile_URL + "/WOC_Read")
        Call<WorkerCode_Model> WOC_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("WOC_ID") String WOC_ID,
                @Query("WOC_02") String WOC_02,
                @Query("WOC_09") String WOC_09,
                @Query("WOC_10") String WOC_10,
                @Query("WOC_0702") String WOC_0702

        );

        /**
         * 작업자코드 필터
         */
        @GET("{host}/" + Mobile_URL + "/WOC_F")
        Call<WKS_Model> WOC_F(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("WOC_ID") String WOC_ID,
                @Query("WOC_02") String WOC_02,
                @Query("WOC_09") String WOC_09,
                @Query("WOC_10") String WOC_10
        );

        /**
         * 작업자코드 신규,수정,삭제
         */
        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/WOC_U")
        Call<BaseModel> WOC_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("WOC_ID") String WOC_ID,
                @Field("WOC_01") String WOC_01,
                @Field("WOC_02") String WOC_02,
                @Field("WOC_03") String WOC_03,
                @Field("WOC_04") String WOC_04,
                @Field("WOC_05") String WOC_05,
                @Field("WOC_06") String WOC_06,
                @Field("WOC_0701") String WOC_0701,
                @Field("WOC_0702") String WOC_0702,
                @Field("WOC_08") String WOC_08,
                @Field("WOC_09") String WOC_09,
                @Field("WOC_10") String WOC_10,
                @Field("WOC_11") String WOC_11,
                @Field("WOC_12") String WOC_12,
                @Field("WOC_13") String WOC_13,
                @Field("WOC_14") String WOC_14,
                @Field("WOC_80") int WOC_80,
                @Field("WOC_98") String WOC_98,
                @Field("WOC_99") String WOC_99
        );
    }

     public interface CLT {
        /**
         * 거래처 리스트
         */
        @GET("{host}/" + Mobile_URL + "/CLT_Read")
        Call<CLT_Model> CLT_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("CLT_ID") String WOC_ID,
                @Query("CLT_02") String CLT_02
        );

         @GET("{host}/" + Mobile_URL + "/CLT_Read")
         Call<CLT_Model> CLT_Read_Test(
                 @Path(value = "host", encoded = true) String host,
                 @QueryMap HashMap<String, Object> paramMap
         );

         @GET("{host}/" + Mobile_URL + "/CLTA_Read")
         Call<CLT_Model> CLTA_Read(
                 @Path(value = "host", encoded = true) String host,
                 @Query("MEM_CID") String MEM_CID,
                 @Query("MEM_01") String MEM_01,
                 @Query("TKN_03") String TKN_03,
                 @Query("GUBUN") String GUBUN,
                 @Query("CLTA_ID") String WOC_ID,
                 @Query("CLTA_01") String CLTA_01
         );

         /* 거래처 신규,수정,삭제*/

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/CLT_U")
        Call<BaseModel> CLT_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("CLT_ID") String CLT_ID,
                @Field("CLT_01") String CLT_01,
                @Field("CLT_29") String CLT_29,
                @Field("CLT_02") String CLT_02,
                @Field("CLT_04") String CLT_04,
                @Field("CLT_03") String CLT_03,
                @Field("CLT_06") String CLT_06,
                @Field("CLT_07") String CLT_07,
                @Field("CLT_1001") String CLT_1001,
                @Field("CLT_1002") String CLT_1002,
                @Field("CLT_1003") String CLT_1003,
                @Field("CLT_08") String CLT_08,
                @Field("CLT_09") String CLT_09,
                @Field("CLT_24") String CLT_24,
                @Field("CLT_25") String CLT_25,
                @Field("CLT_13") String CLT_13,
                @Field("CLT_50") String CLT_50,
                @Field("CLT_51") String CLT_51,
                @Field("CLT_52") String CLT_52,
                @Field("CLT_53") String CLT_53,
                @Field("CLT_54") String CLT_54,
                @Field("CLT_55") String CLT_55,
                @Field("CLT_05") String CLT_05,
                @Field("CLT_11") String CLT_11,
                @Field("CLT_12") String CLT_12,
                @Field("CLT_14") String CLT_14,
                @Field("CLT_97") String CLT_97,
                @Field("CLT_972") String CLT_972,
                @Field("CLT_15") String CLT_15,
                @Field("CLT_16") String CLT_16,
                @Field("CLT_17") String CLT_17,
                @Field("CLT_18") String CLT_18,
                @Field("CLT_99") String CLT_99,
                @Field("CLT_19") String CLT_19,
                @Field("CLT_20") String CLT_20,
                @Field("CLT_21") String CLT_21,
                @Field("CLT_22") String CLT_22,
                @Field("CLT_23") String CLT_23,
                @Field("CLT_26") String CLT_26,
                @Field("CLT_27") String CLT_27,
                @Field("CLT_28") String CLT_28,
                @Field("CLT_30") String CLT_30,
                @Field("CLT_31") String CLT_31,
                @Field("CLT_32") String CLT_32,
                @Field("CLT_33") String CLT_33,
                @Field("CLT_40") String CLT_40


        );
         @FormUrlEncoded
         @POST("{host}/" + Mobile_URL + "/CLT_U")
         Call<CLT_Model> CLT_U2( //고객관리용
                 @Path(value = "host", encoded = true) String host,
                 @Field("MEM_CID") String MEM_CID,
                 @Field("MEM_01") String MEM_01,
                 @Field("TKN_03") String TKN_03,
                 @Field("GUBUN") String GUBUN,
                 @Field("CLT_ID") String CLT_ID,
                 @Field("CLT_01") String CLT_01,
                 @Field("CLT_29") String CLT_29,
                 @Field("CLT_02") String CLT_02,
               
                 @Field("CLT_06") String CLT_06, //주문경로
                                 @Field("CLT_07") String CLT_07, //고객구분

                 @Field("CLT_08") String CLT_08, // 전화번호
                                 @Field("CLT_13") String CLT_13, // 전화번호
                 @Field("CLT_1001") String CLT_1001, //우체국번호
                 @Field("CLT_1002") String CLT_1002, //주소
                 @Field("CLT_1003") String CLT_1003,

                 @Field("CLT_19") String CLT_19, //거래은행
                 @Field("CLT_20") String CLT_20, //계좌번호

                 @Field("CLT_21") String CLT_21



         );

    }

    public interface DAH {
        @GET("{host}/" + Mobile_URL + "/DAH_Read")
        Call<ProductionInfo_Model> DAH_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN")  String GUBUN,
                @Query("DAH_ID") String DAH_ID,
                @Query("OOK_02_ST") String OOK_02_ST,
                @Query("OOK_02_ED") String OOK_02_ED,
                @Query("DAH_03") String DAH_03,
                @Query("DAH_05") String DAH_05

        );

        @GET("{host}/" + Mobile_URL + "/DAH_Read2")
        Call<ArrayList<ProductionInfoVO>> DAH_Read2(
                @Path(value = "host", encoded = true) String host,
                @QueryMap HashMap<String, Object> paramMap
        );

        @GET("{host}/" + Mobile_URL + "/DAH_Read")
        Call<ProductionInfo_Model> DAH_Read_P(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN")  String GUBUN,
                @Query("DAH_ID") String DAH_ID,
                @Query("DAH_02") String DAH_02  // 품명 검색
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/DAH_U")
        Call<BaseModel> DAH_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("DAH_ID") String DAH_ID,
                @Field("DAH_01") String DAH_01,
                @Field("DAH_02") String DAH_02,
                @Field("DAH_03") String DAH_03,
                @Field("DAH_04") String DAH_04,
                @Field("DAH_05") float DAH_05,
                @Field("DAH_06") String DAH_06,
                @Field("DAH_07") String DAH_07,
                @Field("DAH_08") String DAH_08,
                @Field("DAH_09") String DAH_09,
                @Field("DAH_10") String DAH_10,
                @Field("DAH_11") String DAH_11,
                @Field("DAH_12") String DAH_12,
                @Field("DAH_13") String DAH_13,
                @Field("DAH_14") String DAH_14,
                @Field("DAH_15") float DAH_15,
                @Field("DAH_1601") String DAH_1601,
                @Field("DAH_1602") String DAH_1602,
                @Field("DAH_1603") int DAH_1603,
                @Field("DAH_17") int DAH_17,
                @Field("DAH_18") float DAH_18,
                @Field("DAH_19") float DAH_19,
                @Field("DAH_20") String DAH_20,
                @Field("DAH_80") String DAH_80,
                @Field("DAH_98") String DAH_98,
                @Field("DAH_99") String DAH_99,
                @Field("DAH_21") String DAH_21
        );

        @GET("{host}/" + Mobile_URL + "/TYC_Read")
        Call<TYC_Model> TYC_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("TYC_ID") String TYC_ID,
                @Query("TYC_90") String TYC_90
        );

    }

    public interface ODM {
        @GET("{host}/" + Mobile_URL + "/ODM_Read")
        Call<ODM_Model> ODM_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("ODM_ID") String ODM_ID,
                @Query("ODM_02_ST") String ODM_02_ST,
                @Query("ODM_02_ED") String ODM_02_ED,
                @Query("ODM_01") String ODM_01,     //
                @Query("CLT_02") String CLT_02,      // 발주처명
                @Query("ODD_03") String ODD_03
                );

        @GET("{host}/" + Mobile_URL + "/ODD_Read")
        Call<ODD_Model> ODD_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("ODD_ID") String ODD_ID,
                @Query("ODD_01") String ODD_01,
                @Query("ODD_10") String ODD_10,
                @Query("EDAY") String EDAY,
                @Query("ODM_03_NM") String ODM_03_NM,
                @Query("ODD_03") String ODD_03
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

    public interface GEM {
        @GET("{host}/" + Mobile_URL + "/GEM_Read")
        Call<GEM_Model> GEM_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("GEM_ID") String GEM_ID,
                @Query("GEM_02_ST") String GEM_02_ST,
                @Query("GEM_02_ST") String GEM_02_ED,
                @Query("GEM_04") String GEM_04, // 품명
                @Query("GEM_03") String CLT_02 // 입고처
       );


        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/GEM_U")
        Call<BaseModel> GEM_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("GEM_ID") String GEM_ID,
                @Field("GEM_01") String GEM_01,
                @Field("GEM_02") String GEM_02, //입고일자
                @Field("GEM_03") String GEM_03, //입고처
                @Field("GEM_04") String GEM_04, //품번
                @Field("GEM_05") int GEM_05,
                @Field("GEM_06") int GEM_06,
                @Field("GEM_07") int GEM_07,
                @Field("GEM_08") String GEM_08, // 생산자
                @Field("GEM_10") String GEM_10, // 제조일자
                @Field("GEM_20") int GEM_20, // 박스수량
                @Field("GEM_2101") String GEM_2101,
                @Field("GEM_2102") String GEM_2102,
                @Field("GEM_1501") String GEM_1501,
                @Field("GEM_1502") int GEM_1502,
                @Field("GEM_97") String GEM_97,
                @Field("GEM_98") String GEM_98

        );


    }



    public interface ODD {
        @GET("{host}/" + Mobile_URL + "/ODD_Read")
        Call<ODD_Model> ODD_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("ODD_ID") String ODD_ID,
                @Query("ODD_01") String ODD_01,
                @Query("CLT_01_NM") String CLT_01_NM,
                @Query("ODD_03") String ODD_03
        );

        @GET("{host}/" + Mobile_URL + "/WHC_Read")
        Call<WHC_Model> WHC_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("WHC_ID") String WHC_ID
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/ODD_U")
        Call<BaseModel> ODD_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("ODD_ID") String ODD_ID,
                @Field("ODD_01") String ODD_01,
                @Field("ODD_02") int ODD_02,
                @Field("ODD_03") String ODD_03,
                @Field("ODD_04") String ODD_04,
                @Field("ODD_05") String ODD_05,
                @Field("ODD_06") String ODD_06,
                @Field("ODD_07") String ODD_07,
                @Field("ODD_08") String ODD_08,
                @Field("ODD_09") String ODD_09,
                @Field("ODD_10") String ODD_10,
                @Field("ODD_97") String ODD_97,
                @Field("ODD_98") String ODD_98,
                @Field("ODD_99") String ODD_99,
                @Field("MEM_02") String MEM_02,
                @Field("MEM_32_NM") String MEM_32_NM,
                @Field("PCB_02") String PCB_02,
                @Field("CLT_02") String CLT_02,
                @Field("DAH_04") String DAH_04
        );
        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/ODD_L_Edit")
        Call<BaseModel> ODD_L_Edit(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("ODD_ID") String ODD_ID,
                @Field("ODD_01") String ODD_01,     // ODM_01
                @Field("ODD_02") int ODD_02,        // 상세번호
                @Field("ODD_03") String ODD_03,    //품번
                @Field("ODD_04") int ODD_04,      //단가
                @Field("ODD_10") String ODD_10,   // 입고일
                @Field("CLT_01") String CLT_02,  // 입고처
                @Field("WHC_01") String WHC_01 ,    // 창고위치
                @Field("WHC_02") String WHC_02,
                @Field("GEM_06") int GEM_06      // 입고수량
                );

    }

    public interface BMM {
        @GET("{host}/" + Mobile_URL + "/BMM_Read")
        Call<BMM_Model> BMM_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("BMM_ID") String BMM_ID,
                @Query("BMM_02") String BMM_02,
                @Query("BMM_14") String BMM_14
        );
        @GET("{host}/" + Mobile_URL + "/BMD_Read")
        Call<BMD_Model> BMD_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("BMD_ID") String BMD_ID,
                @Query("BMD_01") String BMD_01
        );
        @GET("{host}/" + Mobile_URL + "/BAS_Read")
        Call<BAS_Model> BAS_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("BAS_ID") String BAS_ID,
                @Query("BAS_03") String BAS_03
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/BMD_U")
        Call<BaseModel> BMD_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("BMD_ID") String BMD_ID,
                @Field("BMD_01") String BMD_01,
                @Field("BMD_02") int BMD_02,
                @Field("BMD_03") String BMD_03,
                @Field("BMD_04") String BMD_04,
                @Field("BMD_05") String BMD_05,
                @Field("BMD_06") String BMD_06,
                @Field("BMD_07") int BMD_07,
                @Field("BMD_08") String BMD_08,
                @Field("BMD_09") String BMD_09,
                @Field("BMD_10") String BMD_10,
                @Field("BMD_11") String BMD_11,
                @Field("BMD_98") String BMD_98

        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/BMM_U")
        Call<BaseModel> BMM_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("BMM_ID") String BMM_ID,
                @Field("BMM_01") String BMM_01,
                @Field("BMM_02") String BMM_02,
                @Field("BMM_03") String BMM_03,
                @Field("BMM_15") String BMM_15,
                @Field("BMM_04") String BMM_04,
                @Field("BMM_05") String BMM_05,
                @Field("BMM_06") String BMM_06,
                @Field("BMM_07") String BMM_07,
                @Field("BMM_08") String BMM_08,
                @Field("BMM_09") String BMM_09,
                @Field("BMM_10") String BMM_10,
                @Field("BMM_11") String BMM_11,
                @Field("BMM_12") String BMM_12,
                @Field("BMM_13") String BMM_13,
                @Field("BMM_14") String BMM_14,
                @Field("BMM_7001") String BMM_7001,
                @Field("BMM_7002") int BMM_7002,
                @Field("BMM_98") String BMM_98,
                @Field("BMM_99") String BMM_99,
                @Field("MEM_02") String MEM_02,
                @Field("CNT") int CNT,
                @Field("BMM_02_NM") String BMM_02_NM,
                @Field("BAS_99") String BAS_99,
                @Field("BMMF_7001") String BMMF_7001
        );
    }

    public interface MSM {
        @GET("{host}/" + Mobile_URL + "/MSM_Read")
        Call<MSM_Model> MSM_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("MSM_ID") String MSM_ID,
                @Query("MSM_02") String MSM_02,
                @Query("MSM_03") String MSM_03
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/MSM_U")
        Call<BaseModel> MSM_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("MSMS_ID") String MSMS_ID,
                @Field("MSMS_02") String MSMS_02,
                @Field("MSMS_03") String MSMS_03,
                @Field("MSMS_10") String MSMS_10,
                @Field("MSMS_1001") String MSMS_1001,
                @Field("MSMS_1002") String MSMS_1002,
                @Field("MSMS_1003") String MSMS_1003,
                @Field("MSMS_1004") String MSMS_1004,
                @Field("MSMS_1005") String MSMS_1005,
                @Field("MSMS_1006") String MSMS_1006,
                @Field("MSMS_1007") String MSMS_1007,
                @Field("MSMS_1008") String MSMS_1008,
                @Field("MSMS_1009") String MSMS_1009,
                @Field("MSMS_1010") String MSMS_1010,
                @Field("MSMS_1011") String MSMS_1011,
                @Field("MSMS_1012") String MSMS_1012,
                @Field("MSMS_1013") String MSMS_1013,
                @Field("MSMS_1014") String MSMS_1014,
                @Field("MSMS_1015") String MSMS_1015,
                @Field("MSMS_1016") String MSMS_1016,
                @Field("MSMS_1017") String MSMS_1017,
                @Field("MSMS_1018") String MSMS_1018,
                @Field("MSMS_1019") String MSMS_1019,
                @Field("MSMS_1020") String MSMS_1020,
                @Field("MSMS_1021") String MSMS_1021,
                @Field("MSMS_1022") String MSMS_1022,
                @Field("MSMS_1023") String MSMS_1023,
                @Field("MSMS_1024") String MSMS_1024,
                @Field("MSMS_1025") String MSMS_1025,
                @Field("MSMS_1026") String MSMS_1026,
                @Field("MSMS_1027") String MSMS_1027,
                @Field("MSMS_1028") String MSMS_1028,
                @Field("MSMS_1029") String MSMS_1029,
                @Field("MSMS_1030") String MSMS_1030,
                @Field("MSMS_1031") String MSMS_1031,
                @Field("MSMS_98") String MSMS_98
        );

    }

    public interface REQ {
        @GET("{host}/" + Mobile_URL + "/REQ_Read")
        Call<REQ_Model> REQ_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("REQ_ID") String REQ_ID,
                @Query("REQ_02_ST") String REQ_02_ST,
                @Query("REQ_02_ED") String REQ_02_ED,
                @Query("REQ_03") String REQ_03,
                @Query("REQ_04") String REQ_04,
                @Query("REQ_09") String REQ_09

        );
        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/REQ_U")
        Call<BaseModel> REQ_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("REQ_ID") String REQ_ID,
                @Field("REQ_01") String REQ_01,
                @Field("REQ_02") String REQ_02,
                @Field("REQ_03") String REQ_03,
                @Field("REQ_04") String REQ_04,
                @Field("REQ_05") double REQ_05,
                @Field("REQ_06") double REQ_06,
                @Field("REQ_07") double REQ_07,
                @Field("REQ_08") String REQ_08,
                @Field("REQ_09") String REQ_09,
                @Field("REQ_10") String REQ_10,
                @Field("REQ_11") String REQ_11,

                @Field("REQ_97") String REQ_97,
                @Field("REQ_98") String REQ_98
        );
    }


    public interface OOK {
        @GET("{host}/" + Mobile_URL + "/OOK_Read")
        Call<OOK_Model> OOK_Read2(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("OOK_ID") String OOK_ID,
                @Query("OOK_02_ST") String OOK_02_ST,
                @Query("OOK_02_ED") String OOK_02_ED,
                @Query("DAH_01") String OOK_04

        );
        @GET("{host}/" + Mobile_URL + "/CODE_Read")
        Call<CODE_Model> CODE_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("files") String files,
                @Query("GUBUN") String GUBUN,
                @Query("CODE_ID") String CODE_ID,
                @Query("CODE_01") String CODE_01,
                @Query("CODE_DIV") String CODE_DIV,
                @Query("CODE_03") String CODE_03
        );


        @GET("{host}/" + Mobile_URL + "/OOK_Read")
        Call<ProductionInfo_Model> OOK_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN")  String GUBUN,
                @Query("OOK_ID") String DAH_ID,
                @Query("OOK_02_ST") String OOK_02_ST,
                @Query("OOK_02_ED") String OOK_02_ED,
                @Query("DAH_01") String OOK_04

        );


        @GET("{host}/" + Mobile_URL + "/OOK_Detail")
        Call<OOK_Model> OOK_Detail(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("OOK_ID") String OOK_ID,
                @Query("OOK_01") String OOK_01,
                @Query("OOK_06") String OOK_06,
                @Query("OOK_0201") String OOK_0201,
                @Query("OOK_0202") String OOK_0202

        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/OOK_U")
        Call<BaseModel> OOK_U(

                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("OOK_ID") String OOK_ID,
                @Field("OOK_01") String OOK_01,
                @Field("OOK_02") String OOK_02,
                @Field("OOK_03") String OOK_03,
                @Field("OOK_04") String OOK_04,
                @Field("OOK_05") String OOK_05,
                @Field("OOK_06") String OOK_06,
                @Field("OOK_07") double OOK_07,
                @Field("OOK_0801") String OOK_0801,
                @Field("OOK_0802") int OOK_0802,
                @Field("OOK_09") String OOK_09,
                @Field("OOK_10") String OOK_10,
                @Field("OOK_97") String OOK_97, // 조정사유
                @Field("OOK_98") String OOK_98



        );
    }

    public interface CAL {
        @GET("{host}/" + Mobile_URL + "/CALC_Read")
        Call<CALC_Model> CALC_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("CALC_ID") String CALC_ID,
                @Query("CALC_02") String CALC_02
        );
        @GET("{host}/" + Mobile_URL + "/CAL_Read")
        Call<CAL_Model> CAL_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("CAL_ID") String CAL_ID
        );

        @GET("{host}/" + Mobile_URL + "/CAL_Read")
        Call<CAL_Model> CAL_Detail(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("CAL_ID") String CAL_ID,
                @Query("CAL_01") int CAL_01

        );
        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/CAL_U")
        Call<BaseModel> CAL_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("CAL_ID") String CAL_ID,
                @Field("CAL_01") int CAL_01,
                @Field("CAL_02") String CAL_02,
                @Field("CAL_03") String CAL_03,
                @Field("CAL_11") String CAL_11,
                @Field("CAL_12") String CAL_12,
                @Field("CAL_98") String CAL_98,
                @Field("CAL_99") String CAL_99,
                @Field("CAL_04") String CAL_04,
                @Field("CAL_05_NM") String CAL_05_NM,
                @Field("CAL_05") String CAL_05,
                @Field("MEM_02") String MEM_02
        );
    }
    public interface REM {
        /**
         * 거래처 리스트
         */
        @GET("{host}/" + Mobile_URL + "/REM_Read")
        Call<REM_Model> REM_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("REM_ID") String REM_ID,
                @Query("REM_02") String REM_02
        );
    }
    public interface NGG {
        @GET("{host}/" + Mobile_URL + "/NGG_Read")
        Call<NGG_Model> NGG_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("NGG_ID") String NGG_ID,
                @Query("NGG_01") String NGG_01,
                @Query("NGG_02_ST") String NGG_02_ST,
                @Query("NGG_02_ED") String NGG_02_ED,
                @Query("CLT_01") String CLT_01, // NGG_03
                @Query("DAH_01") String DAH_01, // NGG_04
                @Query("NGG_YN") String NGG_YN  // 정산구분

        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/NGG_U")
        Call<NGG_Model> NGG_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("NGG_ID") String NGG_ID,
                @Field("NGG_01") String NGG_01,
                @Field("NGG_02") String NGG_02,
                @Field("NGG_03") String NGG_03,
                @Field("NGG_04") String NGG_04,
                @Field("NGG_05") double NGG_05,
                @Field("NGG_06") double NGG_06,
                @Field("NGG_07") double NGG_07,
                @Field("NGG_08") double NGG_08,
                @Field("NGG_0901") double NGG_0901,
                @Field("NGG_0902") double NGG_0902,
                @Field("NGG_10") String NGG_10,
                @Field("NGG_11") double NGG_11,
                @Field("NGG_12") String NGG_12,
                @Field("NGG_13") String NGG_13,
                @Field("NGG_14") String NGG_14,
                @Field("NGG_15") String NGG_15,
                @Field("NGG_97") String NGG_97,
                @Field("NGG_98") String NGG_98

        );
    }

    public interface NGGK {
        @GET("{host}/" + Mobile_URL + "/NGGK_Read")
        Call<NGGK_Model> NGGK_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("NGGK_ID") String NGGK_ID,
                @Query("NGGK_06") String NGGK_06
        );
    }

    public interface NGO {
        @GET("{host}/" + Mobile_URL + "/NGO_Read")
        Call<NGO_Model> NGO_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("NGO_ID") String NGO_ID,
                @Query("NGO_01") String NGO_01,
                @Query("NGO_02_ST") String NGO_02_ST,
                @Query("NGO_02_ED") String NGO_02_ED,
                @Query("NGO_03") String NGO_03, // clt
                @Query("NGO_04") String NGO_04, // dah
                @Query("NGO_08") String NGO_08  // 출고구분

        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/NGO_U")
        Call<NGO_Model> NGO_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("NGO_ID") String NGO_ID,
                @Field("NGO_01") String NGO_01,
                @Field("NGO_02") String NGO_02,
                @Field("NGO_03") String NGO_03,
                @Field("NGO_04") String NGO_04,
                @Field("NGO_05") double NGO_05, // 단가
                @Field("NGO_06") double NGO_06, // 수매량
                @Field("NGO_07") double NGO_07, // 금액
                @Field("NGO_08") String NGO_08,
                @Field("NGO_09") String NGO_09,
                @Field("NGO_10") int NGO_10,
                @Field("NGO_97") String NGO_97,
                @Field("NGO_98") String NGO_98

        );
        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/NGOC_U")
        Call<NGO_Model> NGOC_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("NGOC_ID") String NGOC_ID,
                @Field("NGOC_01") String NGOC_01,
                @Field("NGOC_02") int NGOC_02,
                @Field("NGOC_03") String NGOC_03, // 품번

                @Field("NGOC_04") double NGOC_04, // 단가
                @Field("NGOC_05") double NGOC_05, // 수매량
                @Field("NGOC_06") double NGOC_06, // 금액
                @Field("NGOC_07") double NGOC_07, // 예비

                @Field("NGOC_97") String NGOC_97,
                @Field("NGOC_98") String NGOC_98

        );
    }

    public interface GCM {
        @GET("{host}/" + Mobile_URL + "/GCM_Read")
        Call<GCM_Model> GCM_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("GCM_ID") String GCM_ID,
                @Query("GCM_01") String GCM_01,
                @Query("GCM_02_ST") String GCM_02_ST,
                @Query("GCM_02_ED") String GCM_02_ED,
                @Query("GCM_04") String GCM_04, //처리상태
                @Query("GCM_05") String GCM_05, // 발생유형
                @Query("GCM_06") String GCM_06  // 처리유형

        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/GCM_U")
        Call<GCM_Model> GCM_U(
                @Path(value = "host", encoded = true) String host,
                @Field("MEM_CID") String MEM_CID,
                @Field("MEM_01") String MEM_01,
                @Field("TKN_03") String TKN_03,
                @Field("GUBUN") String GUBUN,
                @Field("GCM_ID") String GCM_ID,
                @Field("GCM_01") String GCM_01,
                @Field("GCM_02") String GCM_02,
                @Field("GCM_03") String GCM_03, //CLT고객
                @Field("GCM_04") String GCM_04, //처리상태
                @Field("GCM_05") String GCM_05, // 발생유형
                @Field("GCM_06") String GCM_06, // 처리유형
                @Field("GCM_07") String GCM_07, // 출고번호
                @Field("GCM_08") String GCM_08, //DAH
                @Field("GCM_09") String GCM_09, //접수자
                @Field("GCM_10") String GCM_10, //불만내용

                @Field("GCM_11") String GCM_11, //처리자
                @Field("GCM_12") String GCM_12, //처리일자
                @Field("GCM_13") String GCM_13, //불만처리 및 개선조치사항
                @Field("GCM_14") double GCM_14, //고객만족도
                @Field("GCM_15") String GCM_15, //확인자
                @Field("GCM_16") String GCM_16, //원인
                @Field("GCM_17") String GCM_17, //대책
                
                @Field("GCM_97") String GCM_97,
                @Field("GCM_98") String GCM_98

        );

    }
    public interface MEM {
        @GET("{host}/" + Mobile_URL + "/MEM_Read")
        Call<MEM_Model> MEM_Read(
                @Path(value = "host", encoded = true) String host,
                @Query("MEM_CID") String MEM_CID,
                @Query("MEM_01") String MEM_01,
                @Query("TKN_03") String TKN_03,
                @Query("GUBUN") String GUBUN,
                @Query("MEM_02") String MEM_02

        );

    }

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

    public static LOT lot(TYPE type, String host) { return (LOT) retrofit(LOT.class, type, host);}

    public interface LOT {
        @GET("{host}/" + Mobile_URL + "/LOT_Read")
        Call<ArrayList<LOT_VO> > LOT_Read(
                @Path(value = "host", encoded = true) String host,
                @QueryMap HashMap<String, Object> paramMap
        );

        @FormUrlEncoded
        @POST("{host}/" + Mobile_URL + "/LOT_U")
        Call<LOT_VO> LOT_U(
                @Path(value = "host", encoded = true) String host,
                @FieldMap HashMap<String, Object> paramMap
        );
    }
}