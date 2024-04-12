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

import kr.hmit.dmjs.databinding.ItemPurchaseMainListBinding;
import kr.hmit.dmjs.model.vo.NGG_VO;


public class PurchaseMainListAdapter extends RecyclerView.Adapter{

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
    public PurchaseMainListAdapter(Context context, ArrayList<NGG_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PurchaseMainListAdapter.ViewHolder(ItemPurchaseMainListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NGG_VO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;

       // finalHolder.binding.tvNGG01.setText(vo.NGG_01);
        finalHolder.binding.tvCLT02.setText(vo.NGG_03_NM);
        finalHolder.binding.tvNGG02.setText(datePatternChange(vo.NGG_02));
        finalHolder.binding.tvCnt.setText(vo.CNT);


    }
    private String datePatternChange(String date){

        SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyymmdd");
        SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date tempDate = null;

        try {
            tempDate = beforeFormat.parse(date);
        } catch (ParseException e) {
        }

        return  afterFormat.format(tempDate);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<NGG_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemPurchaseMainListBinding binding;

        public ViewHolder(ItemPurchaseMainListBinding binding) {
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
