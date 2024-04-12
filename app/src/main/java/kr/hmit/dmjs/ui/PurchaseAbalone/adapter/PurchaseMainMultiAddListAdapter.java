package kr.hmit.dmjs.ui.PurchaseAbalone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemOrderRequestMultiAddBinding;
import kr.hmit.dmjs.databinding.ItemPurchaseMainMultiAddBinding;
import kr.hmit.dmjs.ui.PurchaseAbalone.vo.MultiItemVO;

public class PurchaseMainMultiAddListAdapter extends RecyclerView.Adapter{

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private PurchaseMainMultiAddListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(PurchaseMainMultiAddListAdapter.OnItemClickListener onItemClickListener) {
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
    public PurchaseMainMultiAddListAdapter(Context context, ArrayList<MultiItemVO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PurchaseMainMultiAddListAdapter.ViewHolder(ItemPurchaseMainMultiAddBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MultiItemVO vo =  mList.get(position);
        PurchaseMainMultiAddListAdapter.ViewHolder finalHolder = (PurchaseMainMultiAddListAdapter.ViewHolder) holder;
        DecimalFormat dc = new DecimalFormat("###,###,###,###");



        vo.NGG_0901 = String.valueOf(Double.parseDouble(vo.NGG_08) * Double.parseDouble(vo.NGG_06));
        vo.NGG_07 = String.valueOf(Double.parseDouble(vo.NGG_0901) * Double.parseDouble(vo.NGG_05));
        finalHolder.binding.tvDAH.setText(vo.NGG_04_NM);
        finalHolder.binding.tvNGG06.setText(vo.NGG_06);
        finalHolder.binding.tvNGG08.setText("10");
        finalHolder.binding.tvNGG0902.setText(vo.NGG_0902);
        finalHolder.binding.tvMoney.setText(vo.NGG_05);

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
        ItemPurchaseMainMultiAddBinding binding;

        public ViewHolder(ItemPurchaseMainMultiAddBinding binding) {
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
