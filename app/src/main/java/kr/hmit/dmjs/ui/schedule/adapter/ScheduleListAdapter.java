package kr.hmit.dmjs.ui.schedule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemScheduleListBinding;
import kr.hmit.dmjs.model.vo.CALC_VO;
import kr.hmit.base.util.ClsDateTime;

public class ScheduleListAdapter extends RecyclerView.Adapter {

    //=================================
    // interface
    //=================================


    public interface OnItemClickListener {
        void onItemClick(View view, int position,CALC_VO vo);
    }

    private ScheduleListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ScheduleListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<CALC_VO> mList;
    //===========================
    // initialize
    //===========================
    public ScheduleListAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScheduleListAdapter.ViewHolder(ItemScheduleListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CALC_VO vo = mList.get(position);
        ScheduleListAdapter.ViewHolder finalHolder = (ScheduleListAdapter.ViewHolder) holder;

        // 날짜표시
        finalHolder.binding.tvDate.setText(ClsDateTime.ConvertDateToFormat(vo.CALC_02, "yyyyMMdd", "yyyy-MM-dd"));
        //finalHolder.binding.tvCategory.setText("테스트" + position);
        finalHolder.binding.tvContents.setText(vo.CALC_03);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 데이터를 업데이트 한다.
     *
     * @param list
     */
    public void update(ArrayList<CALC_VO> list) {
        if (list == null)
            list = new ArrayList<>();

        mList = list;

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemScheduleListBinding binding;

        public ViewHolder(ItemScheduleListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                CALC_VO vo = mList.get(pos);
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, pos,vo);
                }
            });
        }
    }
}
