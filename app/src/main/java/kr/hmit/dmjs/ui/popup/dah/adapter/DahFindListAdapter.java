package kr.hmit.dmjs.ui.popup.dah.adapter;

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

import kr.hmit.dmjs.databinding.ItemDahFindListBinding;
import kr.hmit.dmjs.databinding.ItemOrderRequestMainListBinding;
import kr.hmit.dmjs.model.vo.REQ_VO;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;

public class DahFindListAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private ArrayList<ProductionInfoVO> mList = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DahFindListAdapter.ViewHolder(ItemDahFindListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProductionInfoVO vo =  mList.get(position);
        DahFindListAdapter.ViewHolder finalHolder = (DahFindListAdapter.ViewHolder) holder;

        finalHolder.binding.tvProductNumber.setText(vo.DAH_01);
        finalHolder.binding.tvProductName.setText(vo.DAH_02);
        finalHolder.binding.tvSizeValue.setText(vo.DAH_14);
        finalHolder.binding.tvStandards.setText(vo.DAH_03);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(ArrayList<ProductionInfoVO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDahFindListBinding binding;

        public ViewHolder(ItemDahFindListBinding binding) {
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private DahFindListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(DahFindListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
