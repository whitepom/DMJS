package kr.hmit.dmjs.ui.stockage_list.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemStockageListListBinding;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;

public class StockageListAdapter extends RecyclerView.Adapter{

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private StockageListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(StockageListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<ProductionInfoVO> mList;

    //===========================
    // initialize
    //===========================
    public StockageListAdapter(Context context, ArrayList<ProductionInfoVO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StockageListAdapter.ViewHolder(ItemStockageListListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProductionInfoVO vo =  mList.get(position);
        StockageListAdapter.ViewHolder finalHolder = (StockageListAdapter.ViewHolder) holder;

        finalHolder.binding.tvNumber.setText(vo.DAH_01);
        finalHolder.binding.tvProductName.setText(vo.DAH_02);
        finalHolder.binding.tvCDO03.setText("제품분류 : "+vo.CDO_03+"("+vo.DAH_04+")");
        finalHolder.binding.tvStock.setText(changeToMoney(vo.OOK_STC));
        finalHolder.binding.tvIO.setText("최근 : "+getCategory(vo.OOK_06));
        finalHolder.binding.tvPrice.setText("단가 : "+changeToMoney(vo.DAH_11)+"원");

        finalHolder.binding.tvDate.setText("  "+getOOK(vo.OOK_02));
        


        finalHolder.binding.tvMaterial.setText("["+vo.DAH_03+"]");

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<ProductionInfoVO> list) {
        mList = list;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemStockageListListBinding binding;

        public ViewHolder(ItemStockageListListBinding binding) {
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

    public static String moneyFormatToWon(double inputMoney) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(inputMoney);
    }

    public static String getCategory(String src){
        switch (src.trim()){
            case "입고" : return "I";
            case "출고" : return "O";
            case "I" : return "입고";
            case "O" : return "출고";
            case "" : return "입출고이력없음";
        }
        return "";
    }
    private String changeToMoney(String str){

        String amount = str;
        amount = amount.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        return amount;

    }

    public static String getOOK(String src){
       if (src.trim()=="") return "없음";
       else return src;
    }

}
