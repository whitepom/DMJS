package kr.hmit.dmjs.ui.receive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


import kr.hmit.dmjs.databinding.ItemConstructionsightListBinding;
import kr.hmit.dmjs.model.vo.REM_VO;

public class ConstructionSightListAdapter extends RecyclerView.Adapter {


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private ConstructionSightListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ConstructionSightListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private Context mContext;
    private ArrayList<REM_VO> mList;

    public ConstructionSightListAdapter(Context context, ArrayList<REM_VO> list){
        mContext = context;
        mList = list;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        return new ConstructionSightListAdapter.ViewHolder(ItemConstructionsightListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        REM_VO vo =  mList.get(position);

        ConstructionSightListAdapter.ViewHolder finalHolder = (ConstructionSightListAdapter.ViewHolder) holder;


        finalHolder.binding.REM01.setText(vo.REM_01);
        finalHolder.binding.REM16.setText(vo.REM_16);
        finalHolder.binding.REM03.setText(vo.REM_03);
        finalHolder.binding.REM06.setText(vo.REM_06);
        finalHolder.binding.REM04.setText(vo.REM_04);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<REM_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemConstructionsightListBinding binding;

        public ViewHolder(ItemConstructionsightListBinding binding) {
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
