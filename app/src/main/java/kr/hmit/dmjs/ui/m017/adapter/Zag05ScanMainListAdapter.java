package kr.hmit.dmjs.ui.m017.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemZag03MainListBinding;
import kr.hmit.dmjs.databinding.ItemZag05ScanMainListBinding;
import kr.hmit.dmjs.model.vo.GAG_VO;
import kr.hmit.dmjs.model.vo.LOTG_VO;
import kr.hmit.dmjs.model.vo.NGG_VO;


public class Zag05ScanMainListAdapter extends RecyclerView.Adapter{

    private ArrayList<LOTG_VO> mList = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Zag05ScanMainListAdapter.ViewHolder(ItemZag05ScanMainListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LOTG_VO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;

        finalHolder.binding.lotg07.setText(vo.LOTG_07);
        finalHolder.binding.lotg09.setText(vo.LOTG_09);
        finalHolder.binding.ngg03.setText(vo.NGG_03);
        finalHolder.binding.nggk04.setText(String.valueOf(vo.NGGK_04));
        finalHolder.binding.ngg02.setText(vo.NGG_02);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(ArrayList<LOTG_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemZag05ScanMainListBinding binding;

        public ViewHolder(@NonNull ItemZag05ScanMainListBinding binding) {
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
