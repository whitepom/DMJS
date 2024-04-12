package kr.hmit.base.model;

import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.base.user_interface.UserInfo;

public class LoginModel extends BaseModel implements Serializable {
    private static final long serialVersionUID = -8740195596145373346L;

    public ArrayList<UserInfo> Data;
}
