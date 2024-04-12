package kr.hmit.dmjs.ui.release.adapter;

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

import kr.hmit.dmjs.databinding.ItemReleasecurrentListBinding;
import kr.hmit.dmjs.model.vo.RUN_VO;

public class ReleaseCurrentListAdapter extends RecyclerView.Adapter{
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private ReleaseCurrentListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ReleaseCurrentListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<RUN_VO> mList;

    //===========================
    // initialize
    //===========================
    public ReleaseCurrentListAdapter(Context context, ArrayList<RUN_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReleaseCurrentListAdapter.ViewHolder(ItemReleasecurrentListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RUN_VO vo  =  mList.get(position);
        ReleaseCurrentListAdapter.ViewHolder finalHolder = (ReleaseCurrentListAdapter.ViewHolder) holder;

        //if(!vo.주문자.equals("")){}
        finalHolder.binding.tvCLT.setText(vo.CLT_02); //주문처

        finalHolder.binding.tvRunDate.setText(vo.RUN_02); //최근출고일



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

    public void update(ArrayList<RUN_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemReleasecurrentListBinding binding;

        public ViewHolder(ItemReleasecurrentListBinding binding) {
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
