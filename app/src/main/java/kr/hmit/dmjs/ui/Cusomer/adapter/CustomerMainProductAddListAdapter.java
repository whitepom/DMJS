package kr.hmit.dmjs.ui.Cusomer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemCustomerSaleAddListBinding;
import kr.hmit.dmjs.ui.Cusomer.model.MultiItemVO;


public class CustomerMainProductAddListAdapter extends RecyclerView.Adapter{

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private CustomerMainProductAddListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(CustomerMainProductAddListAdapter.OnItemClickListener onItemClickListener) {
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
    public CustomerMainProductAddListAdapter(Context context, ArrayList<MultiItemVO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        
        return new CustomerMainProductAddListAdapter.ViewHolder(ItemCustomerSaleAddListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MultiItemVO vo =  mList.get(position);
        CustomerMainProductAddListAdapter.ViewHolder finalHolder = (CustomerMainProductAddListAdapter.ViewHolder) holder;

        finalHolder.binding.tvRUN2201.setText(vo.RUN_2201);
        finalHolder.binding.tvRUN2202.setText(vo.RUN_2202);
        finalHolder.binding.tvRUN27.setText(vo.RUN_27);
        finalHolder.binding.tvRUN06.setText(vo.RUN_06);
        finalHolder.binding.tvRUN07.setText(vo.RUN_07);

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
        ItemCustomerSaleAddListBinding binding;

        public ViewHolder(ItemCustomerSaleAddListBinding binding) {
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
