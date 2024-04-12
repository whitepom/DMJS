package kr.hmit.dmjs.ui.m017.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemZag01MainListBinding;
import kr.hmit.dmjs.databinding.ItemZag03MainListBinding;
import kr.hmit.dmjs.model.vo.GAG_VO;
import kr.hmit.dmjs.model.vo.ZAG_VO;


public class Zag03MainListAdapter extends RecyclerView.Adapter{

    private ArrayList<GAG_VO> mList = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Zag03MainListAdapter.ViewHolder(ItemZag03MainListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GAG_VO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;

        finalHolder.binding.gag04.setText(vo.GAG_04);
        finalHolder.binding.gag01.setText(vo.GAG_01_NM);
        finalHolder.binding.gag03.setText(vo.GAG_03 +"/" + vo.GAG_03_NM);
        finalHolder.binding.gag51.setText(vo.GAG_51 +"~"+vo.GAG_52 +" (" + String.valueOf(vo.GAG_53) + ")");
        finalHolder.binding.gag11.setText(String.valueOf(vo.GAG_11));
        finalHolder.binding.dah14.setText(vo.DAH_14);
        finalHolder.binding.gag10Nm.setText(vo.GAG_10_NM);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setGagList(ArrayList<GAG_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemZag03MainListBinding binding;

        public ViewHolder(@NonNull ItemZag03MainListBinding binding) {
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
