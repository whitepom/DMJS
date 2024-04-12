package kr.hmit.dmjs.ui.main_dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.hmit.dmjs.databinding.ItemMainDashboardNotificationBinding;
import kr.hmit.dmjs.model.vo.GEM_VO;
import kr.hmit.dmjs.ui.UtilBox;

public class MainDashboardCurrentReceiveListAdapter extends RecyclerView.Adapter{


    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private MainDashboardCurrentReceiveListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MainDashboardCurrentReceiveListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<GEM_VO> mList;

    //===========================
    // initialize
    //===========================
    public MainDashboardCurrentReceiveListAdapter(Context context, ArrayList<GEM_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainDashboardCurrentReceiveListAdapter.ViewHolder(ItemMainDashboardNotificationBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GEM_VO vo =  mList.get(position);
        MainDashboardCurrentReceiveListAdapter.ViewHolder finalHolder = (MainDashboardCurrentReceiveListAdapter.ViewHolder) holder;

        finalHolder.binding.tvNum.setText(vo.GEM_01);
        finalHolder.binding.tvTitle.setText(vo.GEM_04_NM);
        finalHolder.binding.tvDate.setText(datePatternChange(vo.GEM_02));
        finalHolder.binding.tvCount.setText(String.valueOf(vo.GEM_06));

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<GEM_VO> list) {
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

    private String datePatternChange(String date){
        if(date.isEmpty())
            return date;
        SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyymmdd");
        SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date tempDate = null;

        try {
            tempDate = beforeFormat.parse(date);
        } catch (ParseException e) {
        }

        return  afterFormat.format(tempDate);
    }
}
