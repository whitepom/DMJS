package kr.hmit.dmjs.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Arrays;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.databinding.ActivityMainDashboardBinding;
import kr.hmit.dmjs.ui.Cusomer.ComplaintMainActivity;
import kr.hmit.dmjs.ui.Cusomer.CustomerSaleMainActivity;
import kr.hmit.dmjs.ui.PurchaseAbalone.NGM_ClientManagementMainActivity;
import kr.hmit.dmjs.ui.PurchaseAbalone.PurchaseMainActivity;
import kr.hmit.dmjs.ui.PurchaseAbalone.PurchaseScanInfoActivity;
import kr.hmit.dmjs.ui.PurchaseAbalone.ReleaseAbaloneMainActivity;
import kr.hmit.dmjs.ui.bulletin_board.BulletinBoardMainActivity;
import kr.hmit.dmjs.ui.m004.Odm02MainActivity;
import kr.hmit.dmjs.ui.m004.Odm03MainActivity;
import kr.hmit.dmjs.ui.m004.OdmScanMainActivity;
import kr.hmit.dmjs.ui.m005.Lot01MainActivity;
import kr.hmit.dmjs.ui.m005.Lot01MainFilterActivity;
import kr.hmit.dmjs.ui.m005.Lot02MainActivity;
import kr.hmit.dmjs.ui.m005.Lot03MainActivity;
import kr.hmit.dmjs.ui.m005.Lot04MainActivity;
import kr.hmit.dmjs.ui.m010.Ngm04MainActivity;
import kr.hmit.dmjs.ui.m010.Ngm05MainActivity;
import kr.hmit.dmjs.ui.m017.Zag01MainActivity;
import kr.hmit.dmjs.ui.m017.Zag02MainActivity;
import kr.hmit.dmjs.ui.m017.Zag03MainActivity;
import kr.hmit.dmjs.ui.m017.Zag04MainActivity;
import kr.hmit.dmjs.ui.m017.Zag05ScanActivity;
import kr.hmit.dmjs.ui.m020.Temp01MainActivity;
import kr.hmit.dmjs.ui.main.adapter.MainMenuListAdapter;
import kr.hmit.dmjs.ui.main.vo.SubMenuVO;
import kr.hmit.dmjs.ui.main.adapter.SubMenuListAdapter;
import kr.hmit.dmjs.ui.main.vo.MenuVO;
import kr.hmit.dmjs.ui.main_dashboard.MainDashboardFragment;
import kr.hmit.dmjs.ui.notice.NoticeMainActivity;
import kr.hmit.dmjs.ui.order_management.OrderManagementMainActivity;
import kr.hmit.dmjs.ui.order_request.OderRequestMainActivity;
import kr.hmit.dmjs.ui.receive.ReceiveCurrentActivity;
import kr.hmit.dmjs.ui.receive.ReceiveMainActivity;
import kr.hmit.dmjs.ui.release.DeliveryStatusMainActivity;
import kr.hmit.dmjs.ui.release.ReleaseCurrentActivity;
import kr.hmit.dmjs.ui.release.ReleaseMainActivity;
import kr.hmit.dmjs.ui.schedule.ScheduleMainActivity;
import kr.hmit.dmjs.ui.stockStatus.InventoryHistoryMainActivity;
import kr.hmit.dmjs.ui.stockage_list.StockageListMainActivity;

public class MainDashboardActivity extends BaseActivity {
    //=========================
    // final
    //=========================


    //=========================
    // view
    //=========================
    private ActivityMainDashboardBinding binding;
    private MainMenuListAdapter mainAdapter;
    private SubMenuListAdapter subAdapter;
    private ArrayList<MenuVO> mainList;
    private ArrayList<SubMenuVO> subList;
    //=========================
    // variable
    //=========================


    //=========================
    // initialize
    //=========================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();

    }

    @Override
    protected void initLayout() {

        mainList= new ArrayList<>();
        subList= new ArrayList<>();

        binding.layoutMenu.setMaxWidth(900);

        binding.imgMenu.setOnClickListener(v -> showDrawerMenu());
        binding.imgClose.setOnClickListener(v -> showDrawerMenu());
       // binding.tvMyPage.setOnClickListener(this::onClickMypage);

        binding.imgLogout.setOnClickListener(v ->doLogout());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.layoutFragment.getId(), MainDashboardFragment.newInstance("", ""))
                .commit();


        //binding.imgScanQr.setOnClickListener(v ->  new IntentIntegrator(this).initiateScan());

        binding.imgScanQr.setOnClickListener(this::onClickGoScan);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView2.setLayoutManager(layoutManager2);

        mainList.add(new MenuVO("커뮤니티", new ArrayList<>(Arrays.asList(
                new SubMenuVO("공지사항", "00", NoticeMainActivity.class),
                new SubMenuVO("사내게시판", "01", BulletinBoardMainActivity.class),
                new SubMenuVO("일정관리", "02", ScheduleMainActivity.class)
        ))));

        mainList.add(new MenuVO("발주관리", new ArrayList<>(Arrays.asList(
                new SubMenuVO("발주관리", "10", OrderManagementMainActivity.class),
                new SubMenuVO("입고대기", "11", Odm02MainActivity.class),
                new SubMenuVO("입고현황", "12", Odm03MainActivity.class),
                new SubMenuVO("발주서스캔", "12", OdmScanMainActivity.class)
        ))));

        mainList.add(new MenuVO("출고관리", new ArrayList<>(Arrays.asList(
                new SubMenuVO("수주관리", "20", OderRequestMainActivity.class),
                new SubMenuVO("납품계획", "21", ReleaseMainActivity.class),
                new SubMenuVO("납품관리", "21", ReleaseCurrentActivity.class),
                new SubMenuVO("납품현황", "22", DeliveryStatusMainActivity.class)
        ))));

        mainList.add(new MenuVO("재고관리", new ArrayList<>(Arrays.asList(
                new SubMenuVO("재고조회", "30", StockageListMainActivity.class),
                new SubMenuVO("재고이력", "31", InventoryHistoryMainActivity.class)
        ))));

        mainList.add(new MenuVO("생산관리", new ArrayList<>(Arrays.asList(
                new SubMenuVO("작업지시", "40", Lot01MainActivity.class),
                new SubMenuVO("생산관리", "41", Lot02MainActivity.class),
                new SubMenuVO("생산현황", "42", Lot03MainActivity.class),
                new SubMenuVO("실시간생산현황", "43", Lot04MainActivity.class)
        ))));

        mainList.add(new MenuVO("수매관리", new ArrayList<>(Arrays.asList(
                new SubMenuVO("수매처관리", "50", NGM_ClientManagementMainActivity.class),
                new SubMenuVO("수매관리", "51", PurchaseMainActivity.class),
                new SubMenuVO("출고관리", "52", Ngm04MainActivity.class),
                new SubMenuVO("출고현황", "52", ReleaseAbaloneMainActivity.class),
                new SubMenuVO("입고관리", "53", Ngm05MainActivity.class)
        ))));

        mainList.add(new MenuVO("고객관리", new ArrayList<>(Arrays.asList(
                new SubMenuVO("고객정보", "60", CustomerSaleMainActivity.class),
                new SubMenuVO("고객불만관리", "61", ComplaintMainActivity.class)
        ))));

        mainList.add(new MenuVO("활전복생산", new ArrayList<>(Arrays.asList(
                new SubMenuVO("작업지시", "101", Zag01MainActivity.class),
                new SubMenuVO("생산관리", "201", Zag02MainActivity.class),
                new SubMenuVO("생산현황", "301", Zag03MainActivity.class),
                new SubMenuVO("실시간생산현황", "401", Zag04MainActivity.class),
                new SubMenuVO("활전복투입스캔", "501", Zag05ScanActivity.class)

        ))));

        mainList.add(new MenuVO("수조온도", new ArrayList<>(Arrays.asList(
                new SubMenuVO("수조온도", "111", Temp01MainActivity.class)
        ))));

        mainAdapter = new MainMenuListAdapter(mContext, mainList);
        mainAdapter.setOnItemClickListener(this::onItemClickMainMenu);
        binding.recyclerView.setAdapter(mainAdapter);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        subAdapter = new SubMenuListAdapter(mContext, new ArrayList<>());
        subAdapter.setOnItemClickListener(this::onItemClickSubMenu);
        binding.recyclerView2.setAdapter(subAdapter);
        binding.recyclerView2.setItemAnimator(new DefaultItemAnimator());

    }

    private void onClickManuel() {
        String URL = "http://ns.smfactory.kr/files/%EC%82%AC%EC%9A%A9%EC%9E%90%EB%A7%A4%EB%89%B4%EC%96%BCV1.2.pdf";
        new DownloadTask(mActivity, URL);
    }

    private void onClickMypage(View view) {
      //  ArrayList<SubMenuVO> tempSubList = new ArrayList<>();
     //   for(String str:mSettings.Value.menuList){
      //      tempSubList.add(getMenu(str));
      //  }
      //  subList=tempSubList;
      //  subAdapter.update(subList);
       // menuAdapter.update(-1);
        BaseAlert.show(mContext, "준비 중입니다.");
    }

    @Override
    protected void initialize() {
    }
    private void onItemClickMainMenu(View view, int i) {
        subList=mainList.get(i).SubMenu;
        subAdapter.update(subList);
        mainAdapter.update(i);

    }

    private void onItemClickSubMenu(View view, int i) {
        if(subList.get(i).Cls != null){
            Intent intent = new Intent(mContext, subList.get(i).Cls);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            mContext.startActivity(intent);
        }else{
            Toast.makeText(mContext, "SubMenu Click - " + subList.get(i).Code, Toast.LENGTH_SHORT).show();
        }

    }

    private void doLogout() {
        mSettings.putBooleanItem(SettingsKey.AutoLogin,false);
        finish();
    }

    /**
     * Drawer Menu 보일 지 가릴 지
     */
    @SuppressLint("RtlHardcoded")
    private void showDrawerMenu() {
        if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT))
            binding.drawerLayout.closeDrawer(Gravity.LEFT);
        else
            binding.drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PurchaseScanInfoActivity.REQUEST_CODE) {


        }else{
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            Intent intent = new Intent(mContext, PurchaseScanInfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("SCAN_DATA", result.getContents());
            mActivity.startActivityForResult(intent, PurchaseScanInfoActivity.REQUEST_CODE);
        }
    }

    private void onClickGoScan(View v) {

        Intent intent = new Intent(mContext, PurchaseScanInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, PurchaseScanInfoActivity.REQUEST_CODE);
    }
}
