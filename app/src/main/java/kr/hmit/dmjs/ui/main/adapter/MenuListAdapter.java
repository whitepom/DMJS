package kr.hmit.dmjs.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemDrawerMainMenuListBinding;
import kr.hmit.dmjs.databinding.ItemDrawerSubMenuListBinding;
import kr.hmit.dmjs.ui.main.vo.MenuVO;

public class MenuListAdapter extends RecyclerView.Adapter{


    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private MenuListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MenuListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<MenuVO> mList;
    //===========================
    // initialize
    //===========================
    public MenuListAdapter(Context context, ArrayList<MenuVO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuListAdapter.ViewHolder(ItemDrawerSubMenuListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MenuVO vo =  mList.get(position);
        MenuListAdapter.ViewHolder finalHolder = (MenuListAdapter.ViewHolder) holder;

     //   finalHolder.binding.tvTitle.setText(vo.SubTitle.trim());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<MenuVO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDrawerSubMenuListBinding binding;

        public ViewHolder(ItemDrawerSubMenuListBinding binding) {
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
