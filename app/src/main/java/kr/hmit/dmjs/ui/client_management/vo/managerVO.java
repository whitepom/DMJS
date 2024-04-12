package kr.hmit.dmjs.ui.client_management.vo;

import kr.hmit.dmjs.ui.UtilBox;

public class managerVO {

    public int number;
    public String name;
    public String mail;
    public String phone;

    public managerVO(int number, String name, String mail, String phone) {
        this.number = number;
        this.name = name;
        this.mail = mail;
        this.phone = UtilBox.phone(phone);
    }
    public managerVO() {
    }
}
