package kr.hmit.dmjs.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemDrawerMainMenuListBinding;
import kr.hmit.dmjs.ui.main.vo.MenuVO;

public class MainMenuListAdapter extends RecyclerView.Adapter{


    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<MenuVO> mList;
    private int index=-1;
    //===========================
    // initialize
    //===========================
    public MainMenuListAdapter(Context context, ArrayList<MenuVO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemDrawerMainMenuListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MenuVO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;

        if(index==position){
            finalHolder.binding.mainLayout.setSelected(true);
            finalHolder.binding.tvTitle.setSelected(true);
        }else{
            finalHolder.binding.mainLayout.setSelected(false);
            finalHolder.binding.tvTitle.setSelected(false);
        }
        finalHolder.binding.tvTitle.setText(vo.MenuTitle);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(int i) {
        index = i;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDrawerMainMenuListBinding binding;

        public ViewHolder(ItemDrawerMainMenuListBinding binding) {
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
