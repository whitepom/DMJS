package kr.hmit.dmjs.ui.m010.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.dmjs.databinding.ItemNgm05MainListBinding;
import kr.hmit.dmjs.model.vo.NGGK_VO;
import kr.hmit.dmjs.model.vo.NGM_VO;


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
        Ngm05MainListAdapter.ViewHolder finalHolder = (Ngm05MainListAdapter.ViewHolder) holder;

        finalHolder.binding.nggk02.setText(String.valueOf(vo.NGGK_02));
        finalHolder.binding.nggk07.setText(vo.NGGK_07);
        finalHolder.binding.nggk08.setText(vo.NGGK_08);
        finalHolder.binding.nggk04.setText(String.valueOf(vo.NGGK_04));

        finalHolder.binding.nggk04.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tempStr = finalHolder.binding.nggk04.getText().toString();
                int tempIndex = mList.indexOf(vo);

                if (tempStr.equals("")) {
                    vo.NGGK_04= 0;
                }else if (Double.parseDouble(tempStr)==0) {
                    vo.NGGK_04= 0 ;
                }else {
                    vo.NGGK_04= Double.parseDouble(tempStr);
                }

                if(tempIndex>=0){
                    mList.set(tempIndex,vo);
                }
            }
        });
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
}
