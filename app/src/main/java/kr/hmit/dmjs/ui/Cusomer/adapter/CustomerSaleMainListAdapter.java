package kr.hmit.dmjs.ui.Cusomer.adapter;

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

import kr.hmit.dmjs.databinding.ItemCustomerSaleMainListBinding;
import kr.hmit.dmjs.databinding.ItemCustomerSaleMainListBinding;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.RUN_VO;
import kr.hmit.dmjs.ui.Cusomer.model.MultiItemVO;

public class CustomerSaleMainListAdapter extends RecyclerView.Adapter{
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private CustomerSaleMainListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(CustomerSaleMainListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private Context mContext;
    private ArrayList<CLT_VO> mList;

    public CustomerSaleMainListAdapter(Context context, ArrayList<CLT_VO> list){
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomerSaleMainListAdapter.ViewHolder(ItemCustomerSaleMainListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }
    

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CLT_VO vo  =  mList.get(position);
        CustomerSaleMainListAdapter.ViewHolder finalHolder = (CustomerSaleMainListAdapter.ViewHolder) holder;

        //if(!vo.주문자.equals("")){}
        finalHolder.binding.tvCLT.setText(vo.CLT_02); //주문처

        finalHolder.binding.tvCLT06.setText(vo.CLT_06); //
        finalHolder.binding.tvCLT08.setText(vo.CLT_08); //
        finalHolder.binding.tvCLT13.setText(vo.CLT_13); //


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<CLT_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCustomerSaleMainListBinding binding;

        public ViewHolder(ItemCustomerSaleMainListBinding binding) {
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
    private String changeToMoney(String str){

        String amount = str;
        amount = amount.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        return amount;

    }
}
