package kr.hmit.dmjs.ui.production_plan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemProductionPlanListBinding;
import kr.hmit.dmjs.model.vo.MSM_VO;

public class ProductionPlanListAdapter extends RecyclerView.Adapter{

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private ProductionPlanListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ProductionPlanListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<MSM_VO> mList;

    //===========================
    // initialize
    //===========================
    public ProductionPlanListAdapter(Context context, ArrayList<MSM_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductionPlanListAdapter.ViewHolder(ItemProductionPlanListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MSM_VO vo =  mList.get(position);
        ProductionPlanListAdapter.ViewHolder finalHolder = (ProductionPlanListAdapter.ViewHolder) holder;

        finalHolder.binding.tvProductName.setText("품명: "+vo.DAH_02);
        finalHolder.binding.tvProductNum.setText("품번: ["+vo.MSMS_03+"]");
        finalHolder.binding.tvWeight.setText("중량: "+vo.DAH_05);
        finalHolder.binding.tvWorkType.setText("구분: "+vo.MSM_GUBUN);
        finalHolder.binding.tvStockCount.setText(vo.MSMS_10+"개");


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<MSM_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductionPlanListBinding binding;

        public ViewHolder(ItemProductionPlanListBinding binding) {
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
