package kr.hmit.dmjs.ui.stockage_list.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemStockAdjustmentListBinding;
import kr.hmit.dmjs.model.vo.OOK_VO;

public class StockageListAdjustmentAdapter extends RecyclerView.Adapter{

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private StockageListAdjustmentAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(StockageListAdjustmentAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<OOK_VO> mList;

    //===========================
    // initialize
    //===========================
    public StockageListAdjustmentAdapter(Context context, ArrayList<OOK_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StockageListAdjustmentAdapter.ViewHolder(ItemStockAdjustmentListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OOK_VO vo =  mList.get(position);
        StockageListAdjustmentAdapter.ViewHolder finalHolder = (StockageListAdjustmentAdapter.ViewHolder) holder;

        finalHolder.binding.tvState.setText(getCategory(vo.OOK_06));
        finalHolder.binding.tvPart.setText(getCategory2(vo.OOK_05));
        finalHolder.binding.tvDate.setText(vo.OOK_02);
        finalHolder.binding.tvCount.setText(vo.OOK_07+"");
        finalHolder.binding.tvPrice.setText(moneyFormatToWon(vo.OOK_MONEY)+"원");

    }
    public static String getCategory(String src){
        switch (src){
            case "입고" : return "I";
            case "출고" : return "O";
            case "I" : return "입고";
            case "O" : return "출고";
        }
        return "";
    }
    public static String getCategory2(String src){
        switch (src){
            case "R" : return "[R]자재";
            case "P" : return "[P]생산";
            case "S" : return "[S]영업";
            case "O" : return "[0]외주";
            case "C" : return "[C]조정";
            case "D" : return "[D]폐기";
        }
        return "해당없음";
    }

  /*  private String datePatternChange(String date){
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
    }*/
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<OOK_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemStockAdjustmentListBinding binding;

        public ViewHolder(ItemStockAdjustmentListBinding binding) {
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
}
