package kr.hmit.dmjs.ui.work_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemWorkManagementListBinding;
import kr.hmit.dmjs.model.vo.WKS_VO;
import kr.hmit.dmjs.ui.work_management.model.EmployeeVO;

public class WorkManagementListAdapter extends RecyclerView.Adapter {
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

    private ArrayList<WKS_VO> mList;

    //=============================
    // endregion // variable
    //=============================


    public WorkManagementListAdapter(Context context, ArrayList<WKS_VO> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemWorkManagementListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder finalHolder = (ViewHolder) holder;
        WKS_VO vo = mList.get(position);

        finalHolder.binding.tvWorkNo.setText(vo.WKS_01);
        finalHolder.binding.tvCategory.setText(vo.WKS_05);
        finalHolder.binding.tvContents.setText(vo.WKS_04);
        finalHolder.binding.tvRequestDate.setText(vo.WKS_02);
        finalHolder.binding.tvCompleteDate.setText(vo.WKS_06);


        ArrayList<EmployeeVO> employees = getEmployee(vo.WKSM_LIST.split("-"));
        finalHolder.mAdapter.update(employees);
        finalHolder.binding.tvState.setText(getState(employees));
        finalHolder.mAdapter.setOnItemClickListener(new WorkManagementListEmployeeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                finalHolder.itemView.performClick();
            }
        });

    }

    private String getState(ArrayList<EmployeeVO> employees) {
        String state = "처리중";

        int readCount = 0;
        int doneCount = 0;

        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).State.equals(EmployeeVO.StateWork.DONE))
                doneCount++;
            else if (employees.get(i).State.equals(EmployeeVO.StateWork.READ))
                readCount++;
        }

        if (employees.size() == doneCount)
            state = "완료";
        else if (readCount == 0&&doneCount==0)
            state = "대기";

        return state;
    }

    private ArrayList<EmployeeVO> getEmployee(String[] strings) {
        ArrayList<EmployeeVO> list = new ArrayList<>();

        if(strings.length>2){
            list.add(new EmployeeVO(strings[2], strings[1], EmployeeVO.StateWork.from(strings[0])));
        }
        if(strings.length>5){
            list.add(new EmployeeVO(strings[5], strings[4], EmployeeVO.StateWork.from(strings[3])));

        }
        if(strings.length>8){
            list.add(new EmployeeVO(strings[8], strings[7], EmployeeVO.StateWork.from(strings[6])));

        }
        if(strings.length>11){
            list.add(new EmployeeVO(strings[11], strings[10], EmployeeVO.StateWork.from(strings[9])));

        }
        if(strings.length>14){
            list.add(new EmployeeVO(strings[14], strings[13], EmployeeVO.StateWork.from(strings[12])));
        }

        return list;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //=============================
    // region // methods
    //=============================

    /**
     * 데이터를 업데이트 한다.
     *
     * @param list
     */
    public void update(ArrayList<WKS_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    //=============================
    // endregion // variable
    //=============================


    //=============================
    // region // ViewHolder
    //=============================
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemWorkManagementListBinding binding;
        WorkManagementListEmployeeAdapter mAdapter;

        public ViewHolder(ItemWorkManagementListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            this.mAdapter = new WorkManagementListEmployeeAdapter(mContext);
            this.binding.recyclerView.setAdapter(mAdapter);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, pos);
                }
            });
        }
    }
    //=============================
    // endregion // ViewHolder
    //=============================
}
