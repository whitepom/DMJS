package kr.hmit.dmjs.ui.m010.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ItemNgm04MainListBinding;
import kr.hmit.dmjs.databinding.ItemNgm05MainListBinding;
import kr.hmit.dmjs.model.vo.NGGK_VO;
import kr.hmit.dmjs.model.vo.NGM_VO;
import kr.hmit.dmjs.model.vo.ODD_VO;


public class Ngm05MainListAdapter extends RecyclerView.Adapter{

    private ArrayList<NGGK_VO> mList = new ArrayList<>();
    private Context mContext;
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    private  ViewHolder finalHolder = null;

    private NGM_VO vo = null;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Ngm05MainListAdapter.ViewHolder(ItemNgm05MainListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        NGGK_VO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;

        finalHolder.binding.nggk02.setText(String.valueOf(vo.NGGK_02));
        finalHolder.binding.nggk07.setText(vo.NGGK_07);
        finalHolder.binding.nggk08.setText(vo.NGGK_08);
        finalHolder.binding.nggk04.setText(String.valueOf(vo.NGGK_04));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setNggkList(ArrayList<NGGK_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemNgm05MainListBinding binding;

        public ViewHolder(@NonNull ItemNgm05MainListBinding binding) {
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

    public ArrayList<NGGK_VO> getCheckList() {
        ArrayList<NGGK_VO> mListCheck = new ArrayList<>();

        for(NGGK_VO vo : mList){
            if(vo.NGGK_04 > 0){
                mListCheck.add(vo);
            }
        }

        return mListCheck;
    }

    public void SetAll(double Nggk04) {

        for(NGGK_VO vo : mList){
            vo.NGGK_04 = Nggk04;
        }

        notifyDataSetChanged();
    }

    //ItemClick Event
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
