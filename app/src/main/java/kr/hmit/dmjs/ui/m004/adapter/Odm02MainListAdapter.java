package kr.hmit.dmjs.ui.m004.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemLot01MainListBinding;
import kr.hmit.dmjs.databinding.ItemOdm02MainListBinding;
import kr.hmit.dmjs.databinding.ItemReceiveMainListBinding;
import kr.hmit.dmjs.model.vo.LOT_VO;
import kr.hmit.dmjs.model.vo.ODD_VO;
import kr.hmit.dmjs.ui.m005.adapter.Lot01MainListAdapter;

public class Odm02MainListAdapter extends RecyclerView.Adapter {

    private ArrayList<ODD_VO> mList = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Odm02MainListAdapter.ViewHolder(ItemOdm02MainListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ODD_VO vo =  mList.get(position);
        Odm02MainListAdapter.ViewHolder finalHolder = (Odm02MainListAdapter.ViewHolder) holder;

        finalHolder.binding.tvOrderName.setText("입고처 : "+vo.CLT_02); // 입고처 OK
        finalHolder.binding.tvProductNumber.setText(vo.ODD_03); //품번 OK
        finalHolder.binding.tvProductName.setText(vo.ODD_03_NM); // 품명 OK
        finalHolder.binding.tvPrice.setText(String.valueOf(vo.ODD_04)+"원"); // ODD_06 : 금액, ODD_04 : 단가  OK
        finalHolder.binding.tvOrderNumber.setText(vo.ODD_01); // 발주번호 D211201-001 OK
        finalHolder.binding.tvQuantity1.setText("0"); // 입력칸 OK
        finalHolder.binding.tvStandards.setText("("+vo.DAH_04+")"); // 단위 OK
        finalHolder.binding.tvQuantity2.setText(vo.ODD_08 + " / " + vo.ODD_05); // 발주수량,기입고수량
        finalHolder.binding.chkWarehousing.setChecked(false);

        finalHolder.binding.tvQuantity1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tempStr = finalHolder.binding.tvQuantity1.getText().toString();
                int tempIndex = mList.indexOf(vo);
                if (tempStr.equals("")) {
                    vo.GEM_06= 0;
                }else if (Double.parseDouble(tempStr)==0) {
                    vo.GEM_06= 0 ;
                }else {
                    vo.GEM_06= Double.parseDouble(tempStr);
                }

                if(tempIndex>=0){
                    mList.set(tempIndex,vo);
                }
            }
        });
        finalHolder.binding.chkWarehousing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    finalHolder.binding.tvQuantity1.setText(Double.parseDouble(String.valueOf(vo.ODD_05)) - Double.parseDouble(String.valueOf(vo.ODD_08)) + "");
                    vo.GEM_06 = Double.parseDouble(String.valueOf(vo.ODD_05)) - Double.parseDouble(String.valueOf(vo.ODD_08));
                } else {
                    finalHolder.binding.tvQuantity1.setText("0");
                    vo.GEM_06 = 0;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(ArrayList<ODD_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOdm02MainListBinding binding;

        public ViewHolder(@NonNull ItemOdm02MainListBinding binding) {
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

    //ItemClick Event
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    private Odm02MainListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(Odm02MainListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ArrayList<ODD_VO> getCheckList() {
        ArrayList<ODD_VO> mListCheck = new ArrayList<>();

        for(ODD_VO vo : mList){
            if(vo.GEM_06 > 0){
                mListCheck.add(vo);
            }
        }

        return mListCheck;
    }
}
