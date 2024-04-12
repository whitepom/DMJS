package kr.hmit.dmjs.ui.order_request.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemOrderRequestMultiAddBinding;
import kr.hmit.dmjs.ui.order_request.model.MultiItemVO;

public class OrderRequestMultiAddListAdapter extends RecyclerView.Adapter{

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OrderRequestMultiAddListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OrderRequestMultiAddListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<MultiItemVO> mList;

    //===========================
    // initialize
    //===========================
    public OrderRequestMultiAddListAdapter(Context context, ArrayList<MultiItemVO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderRequestMultiAddListAdapter.ViewHolder(ItemOrderRequestMultiAddBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MultiItemVO vo =  mList.get(position);
        OrderRequestMultiAddListAdapter.ViewHolder finalHolder = (OrderRequestMultiAddListAdapter.ViewHolder) holder;
        DecimalFormat dc = new DecimalFormat("###,###,###,###");

        finalHolder.binding.tvNumber.setText(vo.DAH_01);
        finalHolder.binding.tvName.setText(vo.DAH_02);
        finalHolder.binding.tvUnit.setText(vo.DAH_04);
        finalHolder.binding.tvMoney.setText(vo.DAH_11+"");

    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<MultiItemVO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrderRequestMultiAddBinding binding;

        public ViewHolder(ItemOrderRequestMultiAddBinding binding) {
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
