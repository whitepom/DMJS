package kr.hmit.dmjs.ui.m004.adapter;

import android.content.Context;
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

import kr.hmit.dmjs.databinding.ItemOdm03MainListBinding;
import kr.hmit.dmjs.databinding.ItemReceivecurrentListBinding;
import kr.hmit.dmjs.model.vo.GEM_VO;
import kr.hmit.dmjs.model.vo.LOT_VO;

public class Odm03MainListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<GEM_VO> mList = new ArrayList<>();

    DecimalFormat Formatter = new DecimalFormat("###,###");

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Odm03MainListAdapter.ViewHolder(ItemOdm03MainListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GEM_VO vo =  mList.get(position);
        Odm03MainListAdapter.ViewHolder finalHolder = (Odm03MainListAdapter.ViewHolder) holder;

        finalHolder.binding.tvGem01.setText(vo.GEM_01);
        finalHolder.binding.tvGem04.setText(vo.GEM_04_NM+"("+vo.DAH_14+")");
        finalHolder.binding.tvGem02.setText(vo.GEM_02);
        finalHolder.binding.tvGem03.setText("입고처 : "+vo.GEM_04_NM);

        String GEM_06 = Formatter.format(vo.GEM_06);
        finalHolder.binding.tvCount.setText(GEM_06);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(ArrayList<GEM_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    private String changeToMoney(String str){
        String amount = str;
        amount = amount.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        return amount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOdm03MainListBinding binding;

        public ViewHolder(ItemOdm03MainListBinding binding) {
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

    private Odm03MainListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(Odm03MainListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
