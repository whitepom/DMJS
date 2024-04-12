package kr.hmit.dmjs.ui.qr_scan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemShipmentManagementBinding;
import kr.hmit.dmjs.model.vo.RCD_VO;

public class ShipmentDetailAdapter extends RecyclerView.Adapter{

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private ShipmentDetailAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ShipmentDetailAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<RCD_VO> mList;
    private ArrayList<RCD_VO> mAdapterRCD;

    //===========================
    // initialize
    //===========================
    public ShipmentDetailAdapter(Context context, ArrayList<RCD_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShipmentDetailAdapter.ViewHolder(ItemShipmentManagementBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

    }
    public ArrayList<RCD_VO> getCheckList(){
        return mAdapterRCD;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RCD_VO vo =  mList.get(position);
        ShipmentDetailAdapter.ViewHolder finalHolder = (ShipmentDetailAdapter.ViewHolder) holder;

        finalHolder.binding.tvItemNum.setText(vo.RCD_03);
        finalHolder.binding.tvdispatchNum.setText(vo.RCD_04);
        finalHolder.binding.tvsteelGrade.setText(vo.DAH_07_NM);
        finalHolder.binding.tvthicknessWidth.setText(vo.RCD_05+"");
        finalHolder.binding.tvlengthWeight.setText(vo.RCD_07+"");
        finalHolder.binding.tvquantity.setText(vo.RCD_08+"");

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void update(ArrayList<RCD_VO> list) {
        mList = list;
        mAdapterRCD=new ArrayList<>();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemShipmentManagementBinding binding;

        public ViewHolder(ItemShipmentManagementBinding binding) {
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
