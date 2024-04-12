package kr.hmit.dmjs.model.vo;

import java.io.Serializable;

public class MEM_ReadVO implements Serializable {
    private static final long serialVersionUID = 8591859710640260416L;

    public String MEM_CID;
    public String MEM_01;
    public String MEM_02;
    public String MEM_11;
    public String MEM_32;
    public String MEM_32_NM;
    public String MEM_51;

    public boolean isSelected;
    public String state;
    public String content;
    public boolean Validation;
    public String ErrorCode;

    public MEM_ReadVO(String MEM_01,String MEM_02,String state) {
        this.MEM_01 = MEM_01;
        this.MEM_02 = MEM_02;
        if(state.equals("1")){
            this.state="미열람";
        } else if(state.equals("2")){
            this.state="열람";
        } else{
            this.state="열람";
        }

    }
    public MEM_ReadVO(String content) {
        if (content ==""){

            this.MEM_01="";
            this.MEM_02="담당자 없음";
        }
    }
    public MEM_ReadVO() {

    }
}
