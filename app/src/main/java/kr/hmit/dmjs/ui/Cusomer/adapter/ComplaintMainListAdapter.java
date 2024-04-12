package kr.hmit.dmjs.ui.Cusomer.adapter;

import static kr.hmit.dmjs.ui.UtilBox.datePatternChange;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.hmit.dmjs.databinding.ItemComplaintMainListBinding;
import kr.hmit.dmjs.model.vo.GCM_VO;

public class ComplaintMainListAdapter extends RecyclerView.Adapter{
    //============================
    // region // interface
    //=================================
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    //=================================
    // endregion // interface
    //============================

    //=============================
    // region // variable
    //=============================
    private Context mContext;

    private ArrayList<GCM_VO> mList;

    //=============================
    // endregion // variable
    //=============================

    public ComplaintMainListAdapter(Context context, ArrayList<GCM_VO> list) {
        mContext = context;
        mList = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemComplaintMainListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder finalHolder = (ViewHolder) holder;
        GCM_VO vo = mList.get(position);

        //finalHolder.binding.tvTitle.setText(vo.BRD_10);
        finalHolder.binding.tvGCM03.setText(vo.CLT_02);
        finalHolder.binding.tvGCM02.setText(datePatternChange(vo.GCM_02));
        finalHolder.binding.tvGCM04.setText(getCategoryGCM04(vo.GCM_04));
        finalHolder.binding.tvGCM05.setText(getCategoryGCM05(vo.GCM_05));
        finalHolder.binding.tvGCM06.setText(getCategoryGCM06(vo.GCM_06));
        finalHolder.binding.tvDAH02.setText(vo.DAH_02);
        finalHolder.binding.tvGCM09NM.setText(vo.GCM_09_NM);
        finalHolder.binding.tvGCM12.setText(datePatternChange(vo.GCM_12));
        finalHolder.binding.tvGCM14.setText(vo.GCM_14);
        finalHolder.binding.tvGCM11NM.setText(vo.GCM_11_NM);
        finalHolder.binding.tvGCM15NM.setText(vo.GCM_15_NM);
//        finalHolder.binding.tvCommentsCount.setText(String.valueOf(Math.round(vo.BRD_15)));

    }


    public void update(ArrayList<GCM_VO> list) {
        mList = list;

        notifyDataSetChanged();
    }
    public static String getCategoryGCM04(String src){
        switch (src.trim()){
            case "y" : return "처리완료";
            case "N" : return "처리중";
           
        }
        return "처리중";
    }
    public static String getCategoryGCM05(String src){
        switch (src.trim()){
            case "1" : return "변심";
            case "2" : return "배송";
            case "3" : return "규격";
            case "4" : return "이상";
        }
        return "기타";
    }
    public static String getCategoryGCM06(String src){
        switch (src.trim()){
            case "1" : return "교환";
            case "2" : return "환불";
            case "3" : return "변상";

        }
        return "기타";
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemComplaintMainListBinding binding;


        public ViewHolder(ItemComplaintMainListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, pos);
                }
            });
        }
    }
}
