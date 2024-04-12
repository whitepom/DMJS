package kr.hmit.base.user_interface;

import kr.hmit.base.model.LoginModel;

public class InterfaceUser {
    public static InterfaceUser instance;

    public UserInfo Value;

    public InterfaceUser() {
        Value = new UserInfo();
    }

    public static synchronized InterfaceUser getInstance() {
        if (null == instance) {
            instance = new InterfaceUser();
        }

        return instance;
    }
}
