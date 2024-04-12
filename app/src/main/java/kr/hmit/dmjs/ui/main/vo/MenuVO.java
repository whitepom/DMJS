package kr.hmit.dmjs.ui.main.vo;


import java.io.Serializable;
import java.util.ArrayList;

import kr.hmit.dmjs.ui.main.vo.SubMenuVO;

public class MenuVO implements Serializable {
    private static final long serialVersionUID = -7588188816679912148L;

    public String MenuTitle;
    public ArrayList<SubMenuVO> SubMenu;
    public boolean Open;

    public MenuVO(String menuTitle, ArrayList<SubMenuVO> subMenu) {
        MenuTitle = menuTitle;
        SubMenu = subMenu;
    }
}


//-7588188816679912148L