package kr.hmit.dmjs.model.request;

import com.google.gson.annotations.SerializedName;

public class RequestLogin {
    @SerializedName("GUBUN")
    public String GUBUN;

    @SerializedName("MEM_CID")
    public String MEM_CID;

    @SerializedName("MEM_01")
    public String MEM_01;

    @SerializedName("MEM_03")
    public String MEM_03;
}
