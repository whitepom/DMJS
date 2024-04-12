package kr.hmit.dmjs.ui.main_dashboard.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemMainDashboardOrderBinding;
import kr.hmit.dmjs.databinding.ItemMainDashboardReleaseBinding;
import kr.hmit.dmjs.model.vo.RUN_VO;


public class MainDashboardReleaseListAdapter extends RecyclerView.Adapter {


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private MainDashboardReleaseListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MainDashboardReleaseListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<RUN_VO> mList;

    //===========================
    // initialize
    //===========================
    public MainDashboardReleaseListAdapter(Context context, ArrayList<RUN_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainDashboardReleaseListAdapter.ViewHolder(ItemMainDashboardReleaseBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RUN_VO vo =  mList.get(position);
        MainDashboardReleaseListAdapter.ViewHolder finalHolder = (MainDashboardReleaseListAdapter.ViewHolder) holder;

        finalHolder.binding.tvDate.setText(vo.RUN_02 + "");
        finalHolder.binding.tvCategory.setText(vo.CDO_03);
        finalHolder.binding.tvDAH02.setText(vo.DAH_02);
        finalHolder.binding.tvCount.setText(vo.RUN_06);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<RUN_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMainDashboardReleaseBinding binding;

        public ViewHolder(ItemMainDashboardReleaseBinding binding) {
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
