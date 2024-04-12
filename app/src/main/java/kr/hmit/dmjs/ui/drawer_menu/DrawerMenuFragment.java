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
import java.util.Arrays;

import kr.hmit.dmjs.databinding.FragmentDrawerMenuBinding;
import kr.hmit.dmjs.ui.client_management.ClientManagementMainActivity;
import kr.hmit.dmjs.ui.order_management.OrderManagementMainActivity;
import kr.hmit.dmjs.ui.product_info.ProductInfoMainActivity;
import kr.hmit.dmjs.ui.receive.ReceiveMainActivity;
import kr.hmit.dmjs.ui.schedule.ScheduleMainActivity;
import kr.hmit.dmjs.ui.stockage_list.StockageListMainActivity;
import kr.hmit.dmjs.ui.work_management.WorkManagementMainActivity;
import kr.hmit.dmjs.ui.worker_code.WorkerCodeMainActivity;
import kr.hmit.base.base_fragment.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DrawerMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawerMenuFragment extends BaseFragment {
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


    //====================================
    // variable
    //====================================
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<MenuVO> mList;
    private MenuAdapter mAdapter;

    //====================================
    // initialize
    //====================================
    public DrawerMenuFragment() {
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
    public static DrawerMenuFragment newInstance(String param1, String param2) {
        DrawerMenuFragment fragment = new DrawerMenuFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDrawerMenuBinding.inflate(getLayoutInflater(), container, false);

        initLayout();

        return binding.getRoot();
    }

    /**
     * 레이아웃 초기화
     */
    private void initLayout() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);

        mList = new ArrayList<>();

        mList.add(new MenuVO("입고관리", new ArrayList<>(Arrays.asList(
                new SubMenuVO("입고현황", "32", OrderManagementMainActivity.class),
                new SubMenuVO("입고대기", "31", ReceiveMainActivity.class)
        ))));



//        mList.add(new MenuVO("품질관리", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("농가관리", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("택배관리", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("고객관리", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("인증관리", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("프로젝트 관리", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("지원사업", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("홍보/마케팅", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));
//        mList.add(new MenuVO("통계분석", new ArrayList<>(Arrays.asList("제품정보", "BOM관리", "단가이력", "전용품목"))));

        mAdapter = new MenuAdapter(mContext, mList);
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
