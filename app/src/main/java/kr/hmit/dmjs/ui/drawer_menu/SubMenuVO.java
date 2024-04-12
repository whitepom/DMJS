package kr.hmit.dmjs.ui.drawer_menu;

import java.io.Serializable;

public class SubMenuVO implements Serializable {
    private static final long serialVersionUID = -7588188816679912148L;

    public String SubTitle;
    public String Code;
    public Class<?> Cls;

    public SubMenuVO() {
    }

    public SubMenuVO(String subTitle, String code) {
        SubTitle = subTitle;
        Code = code;
    }
    public SubMenuVO(String subTitle, String code,Class<?> cls) {
        SubTitle = subTitle;
        Code = code;
        Cls = cls;
    }
}