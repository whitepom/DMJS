package kr.hmit.dmjs.ui.m010.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.dmjs.databinding.ItemNgm04MainListBinding;
import kr.hmit.dmjs.model.vo.NGM_VO;



public class Ngm04MainListAdapter extends RecyclerView.Adapter{

    private ArrayList<NGM_VO> mList = new ArrayList<>();
    private Context mContext;
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    private  ViewHolder finalHolder = null;
    private NGM_VO vo = null;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Ngm04MainListAdapter.ViewHolder(ItemNgm04MainListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        NGM_VO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;

        finalHolder.binding.ngm01.setText(vo.NGM_01);
        finalHolder.binding.ngm02.setText(vo.NGM_02);
        finalHolder.binding.ngm03.setText(vo.NGM_03);
        finalHolder.binding.ngm04.setText(vo.NGM_04_NM);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setNgmList(ArrayList<NGM_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemNgm04MainListBinding binding;

        public ViewHolder(@NonNull ItemNgm04MainListBinding binding) {
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
