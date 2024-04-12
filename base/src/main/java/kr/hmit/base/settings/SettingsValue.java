package kr.hmit.base.settings;

import java.util.ArrayList;

public class SettingsValue {
    /**
     * 로그인 아이디
     */
    public String LoginCID;

    public String LoginID;
    /**
     * 로그인 비번
     */
    public String LoginPW;
    /**
     * 자동 로그인 여부
     */
    public boolean AutoLogin;
    public boolean SaveID;
    /**
     * MEM_CID 설정값
     */
    public String MEM_CID;

    /**
     * 로그인 시 서버에서 받는 값
     */
    public String MEM_01;
    public String MEM_02;
    public String MEM_51;
    /**
     * 토큰
     */
    public String TKN_03;
    public String MEM_99;

    /**
     * URL host
     */
    public String Host;
    /**
     * Web Url Host
     */
    public String WebUrl;

    /**
     * 입고 사용 유무
     */
    public boolean UseWareHousing;
    /**
     * 생산 사용 유무
     */
    public boolean UseProduction;
    /**
     * 출고 사용 유무
     */
    public boolean UseRelease;
    /**
     * 설비 사용 유무
     */
    public boolean UseFacilities;
    /**
     * 품질 사용 유무
     */
    public boolean UseQuality;
    /**
     * 재고 사용 유무
     */
    public boolean UseStock;
    /**
     * QR 사용 유무
     */
    public boolean UseQR;

    //메뉴별 즐겨찾기 버튼
    public boolean WorkManagementBKM;
    public boolean ScheduleBKM;
    public boolean ClientManagementBKM;
    public boolean WorkerCodeBKM;
    public boolean ProductInfoBKM;
    public boolean OrderManagementBKM;
    public boolean ReceiveBKM;
    public boolean EquipmentInfoBKM;
    public boolean ProductionPlanBKM;
    public boolean ProductPerformanceBKM;
    public boolean OrderRequestBKM;
    public boolean ReleaseBKM;
    public boolean StockageListBKM;

    public ArrayList<String> menuList;



    public String FileProviderPath;
}
