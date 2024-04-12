package kr.hmit.dmjs.ui.receive.adapter;

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

import kr.hmit.dmjs.databinding.ItemReceivecurrentListBinding;
import kr.hmit.dmjs.model.vo.GEM_VO;

public class ReceiveCurrentListAdapter extends RecyclerView.Adapter {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private ReceiveCurrentListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ReceiveCurrentListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<GEM_VO> mList;

    //===========================
    // initialize
    //===========================
    public ReceiveCurrentListAdapter(Context context, ArrayList<GEM_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReceiveCurrentListAdapter.ViewHolder(ItemReceivecurrentListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GEM_VO vo =  mList.get(position);
        ReceiveCurrentListAdapter.ViewHolder finalHolder = (ReceiveCurrentListAdapter.ViewHolder) holder;

        finalHolder.binding.tvCLT.setText("입고처 : "+vo.CLT_02);
        finalHolder.binding.tvGEM01.setText(vo.GEM_01);
        finalHolder.binding.tvCount.setText(changeToMoney(String.valueOf(vo.GEM_06)));
        finalHolder.binding.tvGemDate.setText(datePatternChange(vo.GEM_02));
        finalHolder.binding.tvProductName.setText(vo.GEM_04_NM+"("+vo.DAH_14+")");
    }
    private String datePatternChange(String date){

        SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyymmdd");
        SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date tempDate = null;

        try {
            tempDate = beforeFormat.parse(date);
        } catch (ParseException e) {
        }

        return  afterFormat.format(tempDate);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<GEM_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    private String changeToMoney(String str){

        String amount = str;
        amount = amount.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        return amount;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemReceivecurrentListBinding binding;

        public ViewHolder(ItemReceivecurrentListBinding binding) {
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
