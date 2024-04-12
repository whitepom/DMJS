package kr.hmit.dmjs.ui.m004.filter;

import java.io.Serializable;

public class OddFilterVO implements Serializable {


    public String GEM_02_ST =""; //조회조건 입고일
    public String GEM_02_ED =""; // 조회조건 입고일

    public String GEM_03 =""; // 입고처
    public String GEM_04 =""; // 품번/품명

    public String getGEM_03() {
        return GEM_03;
    }

    public void setGEM_03(String GEM_03) {
        this.GEM_03 = GEM_03;
    }

    public String getGEM_04() {
        return GEM_04;
    }

    public void setGEM_04(String GEM_04) {
        this.GEM_04 = GEM_04;
    }

    public String getGEM_02_ST() {
        return GEM_02_ST;
    }

    public void setGEM_02_ST(String GEM_02_ST) {
        this.GEM_02_ST = GEM_02_ST;
    }

    public String getGEM_02_ED() {
        return GEM_02_ED;
    }

    public void setGEM_02_ED(String GEM_02_ED) {
        this.GEM_02_ED = GEM_02_ED;
    }

    public OddFilterVO() {
    }
}
