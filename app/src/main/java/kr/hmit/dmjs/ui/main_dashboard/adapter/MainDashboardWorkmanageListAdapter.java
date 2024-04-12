package kr.hmit.dmjs.ui.main_dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemMainDashboardOrderBinding;
import kr.hmit.dmjs.model.vo.WKS_VO;

public class MainDashboardWorkmanageListAdapter extends RecyclerView.Adapter{


    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private MainDashboardWorkmanageListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MainDashboardWorkmanageListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<WKS_VO> mList;

    //===========================
    // initialize
    //===========================
    public MainDashboardWorkmanageListAdapter(Context context, ArrayList<WKS_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainDashboardWorkmanageListAdapter.ViewHolder(ItemMainDashboardOrderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WKS_VO vo =  mList.get(position);
        MainDashboardWorkmanageListAdapter.ViewHolder finalHolder = (MainDashboardWorkmanageListAdapter.ViewHolder) holder;

        finalHolder.binding.tvDate.setText(vo.WKS_02 + "");
        finalHolder.binding.tvCategory.setText(vo.WKS_05);
        finalHolder.binding.tvDAH02.setText(vo.WKS_04);
        finalHolder.binding.tvCount.setText(vo.MEM_02);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<WKS_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMainDashboardOrderBinding binding;

        public ViewHolder(ItemMainDashboardOrderBinding binding) {
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
