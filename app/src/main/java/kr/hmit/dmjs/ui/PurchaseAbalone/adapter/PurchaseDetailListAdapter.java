package kr.hmit.dmjs.ui.PurchaseAbalone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import kr.hmit.dmjs.databinding.ItemPurchaseDetailListBinding;
import kr.hmit.dmjs.model.vo.NGG_VO;


public class PurchaseDetailListAdapter extends RecyclerView.Adapter{

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
    private ArrayList<NGG_VO> mList;

    //===========================
    // initialize
    //===========================
    public PurchaseDetailListAdapter(Context context, ArrayList<NGG_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PurchaseDetailListAdapter.ViewHolder(ItemPurchaseDetailListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NGG_VO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;
        finalHolder.binding.tvNGG08.setText(vo.NGG_08);
        finalHolder.binding.tvNGG05.setText(" (단가: "+vo.NGG_05+"원) ");
        finalHolder.binding.tvNGG07.setText(vo.NGG_07+" 원");
        finalHolder.binding.tvNGG08.setText(vo.NGG_08);
        finalHolder.binding.tvNGG0901.setText(vo.NGG_0901);
        finalHolder.binding.tvNGG06.setText(vo.NGG_06);
        finalHolder.binding.tvNGGK04.setText(vo.NGGK_04);
        finalHolder.binding.tvProductName.setText(vo.DAH_02);


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<NGG_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }
    private String datePatternChange(String date){
        if(date.isEmpty())
            return date;
        SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyymmdd");
        SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date tempDate = null;

        try {
            tempDate = beforeFormat.parse(date);
        } catch (ParseException e) {
        }

        return  afterFormat.format(tempDate);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemPurchaseDetailListBinding binding;

        public ViewHolder(ItemPurchaseDetailListBinding binding) {
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
