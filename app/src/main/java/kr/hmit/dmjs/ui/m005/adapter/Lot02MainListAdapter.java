package kr.hmit.dmjs.ui.m005.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemLot02MainListBinding;
import kr.hmit.dmjs.databinding.ItemZag02MainListBinding;
import kr.hmit.dmjs.model.vo.LOT_VO;


public class Lot02MainListAdapter extends RecyclerView.Adapter{

    private ArrayList<LOT_VO> mList = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemLot02MainListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LOT_VO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;


        finalHolder.binding.lot02.setText(vo.LOT_02);
        finalHolder.binding.lot01.setText(vo.LOT_01);
        finalHolder.binding.lot04.setText(vo.LOT_04 +"/" + vo.LOT_04_NM);
        finalHolder.binding.lot06.setText(String.valueOf((int)vo.LOT_07) +"/" +String.valueOf((int)vo.LOT_06));
        finalHolder.binding.lot1601.setText(vo.LOT_1601 +"~"+vo.LOT_1602 +" (" + String.valueOf(vo.LOT_10) + ")");
        finalHolder.binding.lot11Nm.setText(String.valueOf(vo.LOT_11_NM));
        finalHolder.binding.dah14.setText(vo.DAH_14);
        finalHolder.binding.lot18Nm.setText(vo.LOT_18_NM);
        finalHolder.binding.lotwCnt.setText(vo.LOTW_CNT +" 명");

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setLotList(ArrayList<LOT_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLot02MainListBinding binding;

        public ViewHolder(@NonNull ItemLot02MainListBinding binding) {
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

    //ItemClick Event
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
