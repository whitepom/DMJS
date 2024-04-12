package kr.hmit.dmjs.ui.order_request.adapter;

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

import kr.hmit.dmjs.databinding.ItemOrderRequestMainListBinding;
import kr.hmit.dmjs.model.vo.REQ_VO;

public class OrderRequestListAdapter extends RecyclerView.Adapter{

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OrderRequestListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OrderRequestListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<REQ_VO> mList;

    //===========================
    // initialize
    //===========================
    public OrderRequestListAdapter(Context context, ArrayList<REQ_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderRequestListAdapter.ViewHolder(ItemOrderRequestMainListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        REQ_VO vo =  mList.get(position);
        OrderRequestListAdapter.ViewHolder finalHolder = (OrderRequestListAdapter.ViewHolder) holder;
        DecimalFormat dc = new DecimalFormat("###,###,###,###");
        finalHolder.binding.tvOrderNumber.setText(vo.REQ_01);
        finalHolder.binding.tvOrderName.setText(vo.REQ_03_NM);
        finalHolder.binding.tvOrderProduct.setText(vo.DAH_02);
        finalHolder.binding.tvQuantity.setText(vo.REQ_06);
        finalHolder.binding.tvOrderDate.setText(datePatternChange(vo.REQ_02));
        finalHolder.binding.tvPrice.setText(dc.format(Integer.parseInt(vo.REQ_07))+" ₩");

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

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<REQ_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrderRequestMainListBinding binding;

        public ViewHolder(ItemOrderRequestMainListBinding binding) {
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
