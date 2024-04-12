package kr.hmit.dmjs.ui.order_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.hmit.dmjs.databinding.ItemOrderManagementInfoListBinding;
import kr.hmit.dmjs.model.vo.ODD_VO;

public class OrderManagementInfoListAdapter extends RecyclerView.Adapter{

    DecimalFormat Formatter = new DecimalFormat("###,###");
    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
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
    public OrderManagementInfoListAdapter(Context context, ArrayList<ODD_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderManagementInfoListAdapter.ViewHolder(ItemOrderManagementInfoListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ODD_VO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;

        finalHolder.binding.tvOdd03.setText(vo.ODD_03 +"/"+vo.DAH_02);
        finalHolder.binding.tvOrderDDay.setText(vo.ODD_10);

        String ODD_04 = Formatter.format(vo.ODD_04);
        String ODD_05 = Formatter.format(vo.ODD_05);
        String ODD_06 = Formatter.format(vo.ODD_06);

        finalHolder.binding.odd04.setText(ODD_04);
        finalHolder.binding.odd05.setText(ODD_05);
        finalHolder.binding.odd06.setText(ODD_06);

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
        ItemOrderManagementInfoListBinding binding;

        public ViewHolder(ItemOrderManagementInfoListBinding binding) {
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
