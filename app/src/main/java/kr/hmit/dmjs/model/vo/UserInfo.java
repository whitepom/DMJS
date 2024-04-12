package kr.hmit.dmjs.model.vo;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 7580519133254622397L;
    public String MEM_CID;
    public String MEM_01;
    public String MEM_02;
    public String MEM_51;
    public String TKN_03;
    public String MEM_99;
    public boolean Validation;
    public String ErrorCode;

    public UserInfo() {

    }
}