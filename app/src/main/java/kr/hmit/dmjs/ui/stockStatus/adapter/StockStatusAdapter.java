package kr.hmit.dmjs.ui.stockStatus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.hmit.dmjs.databinding.ItemStockstatusListBinding;
import kr.hmit.dmjs.model.vo.OOK_VO;

public class StockStatusAdapter extends RecyclerView.Adapter{

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private StockStatusAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(StockStatusAdapter.OnItemClickListener onItemClickListener) {
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
    public StockStatusAdapter(Context context, ArrayList<OOK_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StockStatusAdapter.ViewHolder(ItemStockstatusListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OOK_VO vo =  mList.get(position);
        StockStatusAdapter.ViewHolder finalHolder = (StockStatusAdapter.ViewHolder) holder;

        finalHolder.binding.tvNumber.setText(vo.OOK_03);
        finalHolder.binding.tvProductName.setText(vo.OOK_04_NM);
        finalHolder.binding.tvMaterial.setText(vo.OOK_04);
        finalHolder.binding.tvDate.setText(vo.OOK_02);//임시
        finalHolder.binding.tvStock.setText("물류재고: "+vo.OOK_07);
        finalHolder.binding.tvPrice.setText("단가: "+vo.OOK_09); // 임시
        finalHolder.binding.tvIO.setText("최근: "+vo.OOK_06);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<OOK_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }
    public static String getDate (String src) {

        long time = Long.parseLong(src.replaceFirst("^.*Date\\((\\d+)\\).*$", "$1"));
        SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd");

        return format1.format(new Date(time));
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemStockstatusListBinding binding;

        public ViewHolder(ItemStockstatusListBinding binding) {
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
