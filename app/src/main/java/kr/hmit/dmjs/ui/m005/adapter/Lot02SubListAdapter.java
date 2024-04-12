package kr.hmit.dmjs.ui.m005.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemZag02SubGagListBinding;
import kr.hmit.dmjs.model.vo.GAG_VO;


public class Lot02SubListAdapter extends RecyclerView.Adapter{

    private ArrayList<GAG_VO> mList = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemZag02SubGagListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GAG_VO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;

        finalHolder.binding.gag02.setText(String.valueOf(vo.GAG_02));
        finalHolder.binding.gag04.setText(vo.GAG_04);
        finalHolder.binding.gag11.setText(String.valueOf(vo.GAG_11));
        finalHolder.binding.gag51.setText(vo.GAG_51);
        finalHolder.binding.gag52.setText(vo.GAG_52);
        finalHolder.binding.gag53.setText(String.valueOf(vo.GAG_53));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(ArrayList<GAG_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemZag02SubGagListBinding binding;

        public ViewHolder(@NonNull ItemZag02SubGagListBinding binding) {
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
