package kr.hmit.dmjs.ui.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kr.hmit.dmjs.R;
import kr.hmit.base.base_fragment.BaseFragment;

public class BoardFragment extends BaseFragment {
    //==============================
    // view
    //==============================
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    //==============================
    // variable
    //==============================


    //==============================
    // initialize
    //==============================
    public BoardFragment() {
        // Required empty public constructor
    }

    public static BoardFragment newInstance() {
        BoardFragment fragment = new BoardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_board, container, false);

        initView();

        initialize();

        return view;
    }

    /**
     * 레이아웃 초기화
     */
    private void initView() {
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    /**
     * 데이터 초기화한다.
     */
    private void initialize() {
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

}
