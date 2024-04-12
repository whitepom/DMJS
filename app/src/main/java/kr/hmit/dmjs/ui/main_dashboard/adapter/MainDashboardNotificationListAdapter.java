package kr.hmit.dmjs.ui.main_dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemMainDashboardNotificationBinding;
import kr.hmit.dmjs.model.vo.MAIN_VO;
import kr.hmit.dmjs.ui.UtilBox;

public class MainDashboardNotificationListAdapter extends RecyclerView.Adapter{


    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private MainDashboardNotificationListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MainDashboardNotificationListAdapter.OnItemClickListener onItemClickListener) {
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
    public MainDashboardNotificationListAdapter(Context context, ArrayList<MAIN_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainDashboardNotificationListAdapter.ViewHolder(ItemMainDashboardNotificationBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MAIN_VO vo =  mList.get(position);
        MainDashboardNotificationListAdapter.ViewHolder finalHolder = (MainDashboardNotificationListAdapter.ViewHolder) holder;

        finalHolder.binding.tvDate.setText(UtilBox.datePatternChange(vo.COL_02));
        finalHolder.binding.tvTitle.setText(vo.COL_03);
        finalHolder.binding.tvNum.setText(vo.COL_04);
     //   finalHolder.binding.tvName.setText(vo.COL_05);

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
        ItemMainDashboardNotificationBinding binding;

        public ViewHolder(ItemMainDashboardNotificationBinding binding) {
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
