package kr.hmit.dmjs.ui.PurchaseAbalone.vo;

import kr.hmit.dmjs.ui.UtilBox;

public class NGM_managerVO {

    public int number;
    public String name;
    public String mail;
    public String phone;

    public NGM_managerVO(int number, String name, String mail, String phone) {
        this.number = number;
        this.name = name;
        this.mail = mail;
        this.phone = UtilBox.phone(phone);
    }
    public NGM_managerVO() {
    }
}
