package kr.hmit.dmjs.ui.management_note.model;

import java.io.Serializable;

public class FilterVO implements Serializable {

    public String LED_04;
    public String LED_11;
    public String LED_1301;
    public String LED_1302;
    public FilterVO() {
    }

    public FilterVO(String LED_04, String LED_11, String LED_1301, String LED_1302) {

        this.LED_04 = LED_04;
        this.LED_11 = LED_11;
        this.LED_1301 = LED_1301;
        this.LED_1302 = LED_1302;
    }


}
