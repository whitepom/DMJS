package kr.hmit.dmjs.ui.release.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.hmit.dmjs.databinding.ItemReleaseInfoListBinding;
import kr.hmit.dmjs.model.response.RUN_Model;
import kr.hmit.dmjs.model.vo.RUN_VO;

public class ReleaseInfoListAdapter extends RecyclerView.Adapter {
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    private Context mContext;
    private ArrayList<RUN_VO> mList;
    public ReleaseInfoListAdapter(Context context, ArrayList<RUN_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ReleaseInfoListAdapter.ViewHolder(ItemReleaseInfoListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        RUN_VO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;
        finalHolder.binding.tvRUN01.setText(vo.RUN_01); //주문번호
        finalHolder.binding.tvRunDate.setText(vo.RUN_02); //출고일자
        finalHolder.binding.tvDAH01.setText(" ("+vo.DAH_01+") "); //품번
        finalHolder.binding.tvRUN01.setText(vo.RUN_13_NM); //도/소매
        finalHolder.binding.tvCount.setText(moneyFormatToWon(vo.RUN_06)); // 수량
        finalHolder.binding.tvDAH02.setText(vo.DAH_02); //품명
        finalHolder.binding.tvPrice.setText(moneyFormatToWon(vo.RUN_07)); // 금액

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<RUN_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }
    private String datePatternChange(String date){
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
    }

    public static String moneyFormatToWon(String inputMoney) {
        int temp=Integer.parseInt(inputMoney);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(temp);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemReleaseInfoListBinding binding;

        public ViewHolder(ItemReleaseInfoListBinding binding) {
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
