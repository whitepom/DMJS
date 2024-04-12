package kr.hmit.dmjs.ui.PurchaseAbalone.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.hmit.dmjs.databinding.ItemAbaloneReleaseMainListBinding;

import kr.hmit.dmjs.model.vo.NGO_VO;


public class AbaloneReleaseListAdapter extends RecyclerView.Adapter{

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private AbaloneReleaseListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AbaloneReleaseListAdapter.OnItemClickListener onItemClickListener) {
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
    public AbaloneReleaseListAdapter(Context context, ArrayList<NGO_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AbaloneReleaseListAdapter.ViewHolder(ItemAbaloneReleaseMainListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NGO_VO vo =  mList.get(position);
        AbaloneReleaseListAdapter.ViewHolder finalHolder = (AbaloneReleaseListAdapter.ViewHolder) holder;
        DecimalFormat dc = new DecimalFormat("###,###,###,###");
        finalHolder.binding.tvNGO02.setText(datePatternChange(vo.NGO_02));
        finalHolder.binding.tvNGO03.setText(vo.NGO_03_NM);
        finalHolder.binding.tvNGO04.setText(vo.NGO_04_NM);
        finalHolder.binding.tvNGO06.setText(vo.NGO_06);
        finalHolder.binding.tvNGG07.setText(vo.NGO_07+"원");

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

    public void update(ArrayList<NGO_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemAbaloneReleaseMainListBinding binding;

        public ViewHolder(ItemAbaloneReleaseMainListBinding binding) {
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
