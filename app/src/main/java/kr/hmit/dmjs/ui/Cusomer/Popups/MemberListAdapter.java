package kr.hmit.dmjs.ui.Cusomer.Popups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemMemberListBinding;
import kr.hmit.dmjs.model.vo.MEM_VO;
import kr.hmit.dmjs.ui.UtilBox;

public class MemberListAdapter extends RecyclerView.Adapter{





    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private MemberListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MemberListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<MEM_VO> mList;

    //===========================
    // initialize
    //===========================
    public MemberListAdapter(Context context, ArrayList<MEM_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberListAdapter.ViewHolder(ItemMemberListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MEM_VO vo =  mList.get(position);
        MemberListAdapter.ViewHolder finalHolder = (MemberListAdapter.ViewHolder) holder;

        finalHolder.binding.tvMEM01.setText(vo.MEM_01);
        finalHolder.binding.tvMEM02.setText(vo.MEM_02);
        finalHolder.binding.tvMEM32.setText(vo.MEM_32);



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<MEM_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMemberListBinding binding;

        public ViewHolder(ItemMemberListBinding binding) {
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
