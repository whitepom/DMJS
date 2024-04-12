package kr.hmit.dmjs.ui.main_dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.hmit.dmjs.databinding.ItemMainDashboardPaymentBinding;
import kr.hmit.dmjs.model.vo.LED_VO;

public class MainDashboardPaymentListAdapter extends RecyclerView.Adapter{

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private MainDashboardPaymentListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MainDashboardPaymentListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<LED_VO> mList;

    //===========================
    // initialize
    //===========================
    public MainDashboardPaymentListAdapter(Context context, ArrayList<LED_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainDashboardPaymentListAdapter.ViewHolder(ItemMainDashboardPaymentBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LED_VO vo =  mList.get(position);
        MainDashboardPaymentListAdapter.ViewHolder finalHolder = (MainDashboardPaymentListAdapter.ViewHolder) holder;

       // finalHolder.binding.tvNo.setText(vo.LED_02);
        finalHolder.binding.tvTitle.setText(vo.LED_04);
        finalHolder.binding.tvDate.setText(getDate(vo.LED_13));
;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static String getDate (String src) {

        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);

        return strDate;
    }

    public void update(ArrayList<LED_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMainDashboardPaymentBinding binding;

        public ViewHolder(ItemMainDashboardPaymentBinding binding) {
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
