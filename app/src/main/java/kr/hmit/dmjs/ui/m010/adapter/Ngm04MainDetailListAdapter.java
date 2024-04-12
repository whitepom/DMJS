package kr.hmit.dmjs.ui.m010.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.dmjs.databinding.ItemNgm04MainDetailListBinding;
import kr.hmit.dmjs.databinding.ItemNgm04MainListBinding;
import kr.hmit.dmjs.model.vo.NGM_VO;
import kr.hmit.dmjs.model.vo.NGO_VO;


public class Ngm04MainDetailListAdapter extends RecyclerView.Adapter{

    private ArrayList<NGO_VO> mList = new ArrayList<>();
    private Context mContext;
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    private  ViewHolder finalHolder = null;
    private NGM_VO vo = null;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Ngm04MainDetailListAdapter.ViewHolder(ItemNgm04MainDetailListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        NGO_VO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;

        finalHolder.binding.ngo80.setText(vo.NGO_80);
        finalHolder.binding.ngg02.setText(vo.NGG_02);
        finalHolder.binding.ngo09.setText(vo.NGO_09);

        finalHolder.binding.ngg03Nm.setText(vo.NGG_03_NM);
        finalHolder.binding.ngo04Nm.setText(vo.NGO_04_NM);

        finalHolder.binding.ngo06.setText(vo.NGO_06);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setNgoList(ArrayList<NGO_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemNgm04MainDetailListBinding binding;

        public ViewHolder(@NonNull ItemNgm04MainDetailListBinding binding) {
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
