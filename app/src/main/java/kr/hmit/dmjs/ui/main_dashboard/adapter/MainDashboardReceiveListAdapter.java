package kr.hmit.dmjs.ui.main_dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemMainDashboardAgendaBinding;
import kr.hmit.dmjs.model.vo.BRD_VO;
import kr.hmit.dmjs.model.vo.ODD_VO;
import kr.hmit.dmjs.ui.UtilBox;

public class MainDashboardReceiveListAdapter extends RecyclerView.Adapter{


    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private MainDashboardReceiveListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MainDashboardReceiveListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<ODD_VO> mList;

    //===========================
    // initialize
    //===========================
    public MainDashboardReceiveListAdapter(Context context, ArrayList<ODD_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainDashboardReceiveListAdapter.ViewHolder(ItemMainDashboardAgendaBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ODD_VO vo =  mList.get(position);
        MainDashboardReceiveListAdapter.ViewHolder finalHolder = (MainDashboardReceiveListAdapter.ViewHolder) holder;


        finalHolder.binding.tvODD01.setText(vo.ODD_01);
        finalHolder.binding.tvDAH.setText(vo.ODD_03_NM);

        finalHolder.binding.tvName.setText(vo.CLT_02);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<ODD_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMainDashboardAgendaBinding binding;

        public ViewHolder(ItemMainDashboardAgendaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(v ->{
                int pos = getAdapterPosition();

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, pos);
                }
            });
        }
    }
}
