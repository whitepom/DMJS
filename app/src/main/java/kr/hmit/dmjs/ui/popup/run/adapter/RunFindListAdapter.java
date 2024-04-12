package kr.hmit.dmjs.ui.popup.run.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemRunFindListBinding;
import kr.hmit.dmjs.model.vo.RUN2_VO;

public class RunFindListAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private ArrayList<RUN2_VO> mList = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RunFindListAdapter.ViewHolder(ItemRunFindListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RUN2_VO vo =  mList.get(position);
        RunFindListAdapter.ViewHolder finalHolder = (RunFindListAdapter.ViewHolder) holder;

        finalHolder.binding.run01.setText(vo.RUN_01);
        finalHolder.binding.run02.setText(vo.RUN_02);
        finalHolder.binding.run09.setText(vo.RUN_09);
        finalHolder.binding.run26.setText(vo.RUN_26);
        finalHolder.binding.run2101.setText(vo.RUN_2101);
        finalHolder.binding.run2201.setText(vo.RUN_2201);
        finalHolder.binding.run2202.setText(vo.RUN_2202);
        finalHolder.binding.run2203.setText(vo.RUN_2203);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(ArrayList<RUN2_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemRunFindListBinding binding;

        public ViewHolder(ItemRunFindListBinding binding) {
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private RunFindListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RunFindListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
