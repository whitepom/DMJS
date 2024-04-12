package kr.hmit.dmjs.ui.work_management.model;

import java.io.Serializable;

public class EmployeeVO implements Serializable {
    private static final long serialVersionUID = -2864116525696164046L;

    public String Name;
    public String MEM_01;
    public StateWork State;

    public EmployeeVO(String name, String MEM_01, StateWork state) {
        Name = name;
        this.MEM_01 = MEM_01;
        State = state;
    }

    public enum StateWork {
        UNREAD("1"),
        READ("2"),
        DONE("3");

        public String mState;

        StateWork(String state) {
            this.mState = state;
        }

        public static StateWork from(String state) {
            if (state.equals("1"))
                return UNREAD;
            else if (state.equals("2"))
                return READ;
            else
                return DONE;
        }
    }
}
