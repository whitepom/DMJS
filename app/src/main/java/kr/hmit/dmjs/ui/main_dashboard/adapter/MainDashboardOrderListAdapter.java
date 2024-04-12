package kr.hmit.dmjs.ui.main_dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemMainDashboardOrderBinding;
import kr.hmit.dmjs.model.vo.MAIN_VO;
import kr.hmit.dmjs.ui.UtilBox;

public class MainDashboardOrderListAdapter extends RecyclerView.Adapter{


    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private MainDashboardOrderListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MainDashboardOrderListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<MAIN_VO> mList;

    //===========================
    // initialize
    //===========================
    public MainDashboardOrderListAdapter(Context context, ArrayList<MAIN_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainDashboardOrderListAdapter.ViewHolder(ItemMainDashboardOrderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MAIN_VO vo =  mList.get(position);
        MainDashboardOrderListAdapter.ViewHolder finalHolder = (MainDashboardOrderListAdapter.ViewHolder) holder;

        finalHolder.binding.tvDate.setText(UtilBox.datePatternChange(vo.COL_03));
        finalHolder.binding.tvCategory.setText(vo.COL_04);
        finalHolder.binding.tvCount.setText(vo.COL_05);
        finalHolder.binding.tvDAH02.setText(vo.COL_06);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<MAIN_VO> list) {
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
