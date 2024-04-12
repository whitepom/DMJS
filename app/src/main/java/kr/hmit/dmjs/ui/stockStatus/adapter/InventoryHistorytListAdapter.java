package kr.hmit.dmjs.ui.stockStatus.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.hmit.dmjs.databinding.ItemInventoryHistoryListBinding;
import kr.hmit.dmjs.model.vo.OOK_VO;

public class InventoryHistorytListAdapter extends RecyclerView.Adapter{

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
    private final Context mContext;
    private ArrayList<OOK_VO> mList;
    private ArrayList<OOK_VO> mListcheck=new ArrayList<>();

    //===========================
    // initialize
    //===========================
    public InventoryHistorytListAdapter(Context context, ArrayList<OOK_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemInventoryHistoryListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OOK_VO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;

        finalHolder.binding.tvDAH01.setText(vo.DAH_01); //품번
        finalHolder.binding.tvDAH02.setText(vo.DAH_02); //품명
        finalHolder.binding.tvCDO03.setText(vo.CDO_03); //제품분류
        finalHolder.binding.tvDAH14.setText(vo.DAH_14); //규격
        finalHolder.binding.tvOOS01.setText(vo.OOK_01); //이력번호
        finalHolder.binding.tvOOK02.setText(vo.OOK_02); //일자
        finalHolder.binding.tvOOK06.setText(vo.OOK_06); // 입/출고
        finalHolder.binding.tvOOKSTC.setText(commaWithNumber(vo.OOK_STC)); // 입/출고
    }

    // 천단위 콤마설정1 변형.
    @SuppressLint("DefaultLocale")
    public String makeStringComma(String str) {
        if (str.length() == 0) {
            return "";
        }
        if (str.contains(".")) {
        /*
         Type이 float이나 double이면 # 뒤에 .0 붙이면 됨
         */
            float value = Float.parseFloat(str);
            DecimalFormat format = new DecimalFormat("###,###.0");
            return format.format(value);
        } else {
            long value = Long.parseLong(str);
            DecimalFormat format = new DecimalFormat("###,###");
            return format.format(value);
        }

    }
    public static String getCategory2(String src){
        switch (src){
            case "R" : return "원자재";
            case "B" : return "부재료";
            case "P" : return "완제품";
            case "S" : return "반제품";
        }
        return "";
    }

    /**
     * 날짜형식 변경
     *
     * @param date
     * @return YYYYMMDD -> yyyy-MM-dd
     */
    @SuppressLint("SimpleDateFormat")
    public String datePatternChange(String date) {

        if (date == null || TextUtils.isEmpty(date)) {
            return "";
        }
        if (date.isEmpty())
            return date;

        SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date tempDate = null;

        try {
            tempDate = beforeFormat.parse(date);
        } catch (ParseException e) {
        }

        return afterFormat.format(tempDate);
    }

    /**
     * 천단위마다 콤마 추가
     *
     * @param
     * @return comma포함한 숫자(문자열)
     */
    public String commaWithNumber(String str) {
        if (str.contains(",")) {
            str = str.replace(",", "");
        }
        double strToDouble = Double.parseDouble(str);

        return commaWithNumber(strToDouble);
    }
    public String commaWithNumber(double number) {
        DecimalFormat myFormatter = new DecimalFormat("#,###.###");

        return myFormatter.format(number);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public ArrayList<OOK_VO> getCheckList(){
        return mListcheck;
    }

    public void update(ArrayList<OOK_VO> list) {
        mList = list;
        mListcheck=new ArrayList<>();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemInventoryHistoryListBinding binding;

        public ViewHolder(ItemInventoryHistoryListBinding binding) {
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
