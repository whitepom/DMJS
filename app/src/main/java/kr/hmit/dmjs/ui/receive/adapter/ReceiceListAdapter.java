package kr.hmit.dmjs.ui.receive.adapter;

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

import kr.hmit.dmjs.databinding.ItemReceiveMainListBinding;
import kr.hmit.dmjs.model.vo.ODD_VO;

public class ReceiceListAdapter extends RecyclerView.Adapter {

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private ReceiceListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ReceiceListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<ODD_VO> mList;

    //===========================
    // initialize
    //===========================
    public ReceiceListAdapter(Context context, ArrayList<ODD_VO> list) {
        mContext = context;
        mList = list;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ReceiceListAdapter.ViewHolder(ItemReceiveMainListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ODD_VO vo = mList.get(position);
        ReceiceListAdapter.ViewHolder finalHolder = (ReceiceListAdapter.ViewHolder) holder;

        finalHolder.binding.tvOrderName.setText("입고처 : "+vo.CLT_02); // 입고처 OK
        finalHolder.binding.tvProductNumber.setText(vo.ODD_03); //품번 OK
        finalHolder.binding.tvProductName.setText(vo.ODD_03_NM); // 품명 OK
        //finalHolder.binding.tvPrice.setText(moneyFormatToWon(String.valueOf(vo.ODD_04))+"원"); // ODD_06 : 금액, ODD_04 : 단가  OK
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
                    vo.ODD_07="0";
                }else if (Integer.parseInt(tempStr)==0) {
                    vo.ODD_07="0";
                }else {
                    vo.ODD_07=tempStr;
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
                    //finalHolder.binding.tvQuantity1.setText(Integer.parseInt(String.valueOf(vo.ODD_05)) - Integer.parseInt(vo.ODD_08) + "");
                } else {
                    //finalHolder.binding.tvQuantity1.setText("0");
                }
            }
        });
    }

    public ArrayList<ODD_VO> getCheckList() {
        ArrayList<ODD_VO> mListcheck = new ArrayList<>();
        for(ODD_VO vo : mList){
            if(vo.ODD_07!=null&&!vo.ODD_07.isEmpty()){
                if(Integer.parseInt(vo.ODD_07)>0){
                    mListcheck.add(vo);
                }
            }
        }

        return mListcheck;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<ODD_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemReceiveMainListBinding binding;

        public ViewHolder(ItemReceiveMainListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static String moneyFormatToWon(String inputMoney) {
        int newint = Integer.parseInt(inputMoney);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(newint);
    }

}
