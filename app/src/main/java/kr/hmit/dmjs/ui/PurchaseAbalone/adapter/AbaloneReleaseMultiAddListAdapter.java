package kr.hmit.dmjs.ui.PurchaseAbalone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemAbaloneReleaseMultiAddBinding;
import kr.hmit.dmjs.databinding.ItemAbaloneReleaseMultiAddBinding;
import kr.hmit.dmjs.model.vo.NGOC_VO;
import kr.hmit.dmjs.model.vo.NGO_VO;

public class AbaloneReleaseMultiAddListAdapter extends RecyclerView.Adapter{

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private AbaloneReleaseMultiAddListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AbaloneReleaseMultiAddListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<NGO_VO> mList;

    //===========================
    // initialize
    //===========================
    public AbaloneReleaseMultiAddListAdapter(Context context, ArrayList<NGO_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        
        return new AbaloneReleaseMultiAddListAdapter.ViewHolder(ItemAbaloneReleaseMultiAddBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NGO_VO vo =  mList.get(position);
        AbaloneReleaseMultiAddListAdapter.ViewHolder finalHolder = (AbaloneReleaseMultiAddListAdapter.ViewHolder) holder;
        DecimalFormat dc = new DecimalFormat("###,###,###,###");

        finalHolder.binding.tvNGOC03.setText(vo.NGOC_03);
        finalHolder.binding.tvNGOC04.setText(vo.NGOC_04);
        finalHolder.binding.tvNGOC05.setText(vo.NGOC_05);
        finalHolder.binding.tvNGOC06.setText(vo.NGOC_06+"");

    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<NGO_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemAbaloneReleaseMultiAddBinding binding;

        public ViewHolder(ItemAbaloneReleaseMultiAddBinding binding) {
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
