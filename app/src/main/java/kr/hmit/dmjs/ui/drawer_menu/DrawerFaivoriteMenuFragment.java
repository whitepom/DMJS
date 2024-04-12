package kr.hmit.dmjs.ui.drawer_menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.FragmentDrawerMenuBinding;
import kr.hmit.dmjs.ui.client_management.ClientManagementMainActivity;
import kr.hmit.dmjs.ui.order_management.OrderManagementMainActivity;
import kr.hmit.dmjs.ui.order_request.OderRequestMainActivity;
import kr.hmit.dmjs.ui.product_info.ProductInfoMainActivity;
import kr.hmit.dmjs.ui.production_plan.ProductionPlanMainActivity;
import kr.hmit.dmjs.ui.receive.ReceiveMainActivity;
import kr.hmit.dmjs.ui.schedule.ScheduleMainActivity;
import kr.hmit.dmjs.ui.stockage_list.StockageListMainActivity;
import kr.hmit.dmjs.ui.work_management.WorkManagementMainActivity;
import kr.hmit.dmjs.ui.worker_code.WorkerCodeMainActivity;
import kr.hmit.base.base_fragment.BaseFragment;
import kr.hmit.base.settings.InterfaceSettings;
import kr.hmit.base.user_interface.InterfaceUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DrawerFaivoriteMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawerFaivoriteMenuFragment extends BaseFragment {
    //====================================
    // final
    //====================================
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //====================================
    // view
    //====================================
    private FragmentDrawerMenuBinding binding;

    protected InterfaceSettings mSettings;
    protected InterfaceUser mUser;


    //====================================
    // variable
    //====================================
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FaivoriteMenuAdapter mAdapter;

    //====================================
    // initialize
    //====================================
    public DrawerFaivoriteMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrawerMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DrawerFaivoriteMenuFragment newInstance(String param1, String param2) {
        DrawerFaivoriteMenuFragment fragment = new DrawerFaivoriteMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public void onStart(){
        super.onStart();
       // CheckBookmarks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDrawerMenuBinding.inflate(getLayoutInflater(), container, false);

        if (mSettings == null)
            mSettings = InterfaceSettings.getInstance(container.getContext());
        if (mUser == null)
            mUser = InterfaceUser.getInstance();



        initLayout();

        return binding.getRoot();
    }

    /**
     * 레이아웃 초기화
     */
    private void initLayout() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);


        // 목록

//        mList.add(new MenuVO("품질관리", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("농가관리", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("택배관리", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("고객관리", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("인증관리", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("프로젝트 관리", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("지원사업", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("홍보/마케팅", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("통계분석", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));

        mAdapter = new FaivoriteMenuAdapter(mContext,  new ArrayList<>());
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());



    }


    public void CheckBookmarks() {
        ArrayList<SubMenuVO> subMenuList = new ArrayList<>();

        for(String str : mSettings.Value.menuList){
            subMenuList.add(getMenu(str));
        }
        ArrayList<MenuVO> mList = new ArrayList<>();
        mList.add(new MenuVO("즐겨찾기",subMenuList));
        mAdapter.update(mList);
    }
    public SubMenuVO getMenu(String str) {

        if(str.equals("00")){
            return new SubMenuVO("업무관리", "00", WorkManagementMainActivity.class);
        }
        else if(str.equals("01")){
            return new SubMenuVO("일정관리", "01", ScheduleMainActivity.class);
        }
        else if(str.equals("10")){
            return new SubMenuVO("거래처관리", "10", ClientManagementMainActivity.class);
        }
        else if(str.equals("11")){
            return new SubMenuVO("작업자코드", "11", WorkerCodeMainActivity.class);
        }
        else if(str.equals("20")){
            return new SubMenuVO("제품정보", "20", ProductInfoMainActivity.class);
        }
        else if(str.equals("30")){
            return new SubMenuVO("발주관리", "30", OrderManagementMainActivity.class);
        }
        else if(str.equals("31")){
            return new SubMenuVO("입고대기", "31", ReceiveMainActivity.class);
        }
        else if(str.equals("50")) {
            return new SubMenuVO("생산계획", "50", ProductionPlanMainActivity.class);
        }
        else if(str.equals("60")){
            return new SubMenuVO("주문접수", "60", OderRequestMainActivity.class);
        }

        else{
            return new SubMenuVO("재고목록", "70", StockageListMainActivity.class);
        }
    }

}
