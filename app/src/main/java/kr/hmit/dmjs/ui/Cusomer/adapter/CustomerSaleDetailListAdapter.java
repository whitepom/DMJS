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

import kr.hmit.dmjs.databinding.ItemCustomerDetailListBinding;
import kr.hmit.dmjs.ui.Cusomer.model.MultiItemVO;
import kr.hmit.dmjs.ui.UtilBox;

public class CustomerSaleDetailListAdapter extends RecyclerView.Adapter{

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<MultiItemVO> mList;

    //===========================
    // initialize
    //===========================
    public CustomerSaleDetailListAdapter(Context context, ArrayList<MultiItemVO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomerSaleDetailListAdapter.ViewHolder(ItemCustomerDetailListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MultiItemVO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;

        finalHolder.binding.tvRUN2201.setText(vo.RUN_2201);
        finalHolder.binding.tvRUN09.setText(vo.RUN_09);
        finalHolder.binding.tvRUN2202.setText(vo.RUN_2202==null?"":UtilBox.phone(vo.RUN_2202));
        finalHolder.binding.tvRUN04.setText(vo.RUN_04);
        finalHolder.binding.tvRUN23.setText(vo.RUN_23);
        finalHolder.binding.tvRUN07.setText(vo.RUN_07);
        finalHolder.binding.tvRUN02.setText(datePatternChange(vo.RUN_02));


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<MultiItemVO> list) {
        mList = list;
        notifyDataSetChanged();
    }
    private String datePatternChange(String date){
        if(date.isEmpty() || date ==null)
            return date;
        SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyymmdd");
        SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date tempDate = null;

        try {
            tempDate = beforeFormat.parse(date);
        } catch (ParseException e) {
        }

        return  afterFormat.format(tempDate);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCustomerDetailListBinding binding;

        public ViewHolder(ItemCustomerDetailListBinding binding) {
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
