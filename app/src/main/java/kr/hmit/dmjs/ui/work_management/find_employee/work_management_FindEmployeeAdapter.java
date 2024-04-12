package kr.hmit.dmjs.ui.work_management.find_employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ItemFindEmployeeBinding;
import kr.hmit.dmjs.model.vo.MEM_ReadVO;

public class work_management_FindEmployeeAdapter extends RecyclerView.Adapter {
    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position, boolean isDelete);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<MEM_ReadVO> mList;

    //===========================
    // initialize
    //===========================

    public work_management_FindEmployeeAdapter(Context context, ArrayList<MEM_ReadVO> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemFindEmployeeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MEM_ReadVO vo = mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;

        finalHolder.binding.tvTextName.setText(vo.MEM_02);
        finalHolder.binding.tvTextPosition.setText(vo.MEM_32_NM);
        finalHolder.binding.tvTextSelect.setText(vo.isSelected ? "제거" : "추가");
        finalHolder.binding.tvTextSelect.setSelected(vo.isSelected);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 데이터 업데이트
     *
     * @param list
     */
    public void update(ArrayList<MEM_ReadVO> list) {
        mList = list;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemFindEmployeeBinding binding;

        public ViewHolder(ItemFindEmployeeBinding binding) {
            super(binding.getRoot());

            this.binding = binding;


            binding.tvTextSelect.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (onItemClickListener != null) {
                    if (getSelectedCount() >= 5 && !mList.get(pos).isSelected) {
                        BaseAlert.show(mContext, R.string.find_employee_5);
                        return;
                    }

                    mList.get(pos).isSelected = !mList.get(pos).isSelected;
                    onItemClickListener.onItemClick(v, pos, !mList.get(pos).isSelected);

                    notifyDataSetChanged();
                }
            });
        }
    }


    /**
     * 선택된 갯수를 가져온다.
     *
     * @return
     */
    private int getSelectedCount() {
        int count = 0;

        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isSelected)
                count++;
        }

        return count;
    }
}
