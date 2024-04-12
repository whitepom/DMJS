package kr.hmit.dmjs.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import kr.hmit.dmjs.databinding.ItemDrawerSubMenuListBinding;
import kr.hmit.dmjs.ui.main.vo.SubMenuVO;
public class SubMenuListAdapter extends RecyclerView.Adapter{

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    private SubMenuListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(SubMenuListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    private Context mContext;
    private ArrayList<SubMenuVO> mList;

    public SubMenuListAdapter(Context context, ArrayList<SubMenuVO> list){
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubMenuListAdapter.ViewHolder(ItemDrawerSubMenuListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false)) ;
    }

    @Override
    public void onBindViewHolder( @NonNull RecyclerView.ViewHolder holder, int position) {

        SubMenuVO vo =  mList.get(position);
        SubMenuListAdapter.ViewHolder finalHolder = (SubMenuListAdapter.ViewHolder) holder;

        finalHolder.binding.tvTitle.setText(vo.SubTitle);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<SubMenuVO> list) {
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
