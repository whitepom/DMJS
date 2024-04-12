package kr.hmit.dmjs.ui.Cusomer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemCustomerSaleMainListBinding;
import kr.hmit.dmjs.model.vo.CLT_VO;

public class CustomerSaleMainAdapter extends RecyclerView.Adapter{

    private ArrayList<CLT_VO> mList = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomerSaleMainAdapter.ViewHolder(ItemCustomerSaleMainListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }
    

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        CLT_VO vo  =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;

        finalHolder.binding.tvCLT.setText(vo.CLT_02);
        finalHolder.binding.tvCLT06.setText(vo.CLT_06);
        finalHolder.binding.tvCLT08.setText(vo.CLT_08);
        finalHolder.binding.tvCLT13.setText(vo.CLT_13);
    }

    public void setList(ArrayList<CLT_VO> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCustomerSaleMainListBinding binding;

        public ViewHolder(ItemCustomerSaleMainListBinding binding) {
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

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
