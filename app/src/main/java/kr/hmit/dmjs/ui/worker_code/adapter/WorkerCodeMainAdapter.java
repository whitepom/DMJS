package kr.hmit.dmjs.ui.worker_code.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemWorkerCodeListBinding;
import kr.hmit.dmjs.ui.worker_code.model.WorkerCodeVO;

public class WorkerCodeMainAdapter extends RecyclerView.Adapter {
    //=================================
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
    //=================================

    //=============================
    // region // variable
    //=============================
    private Context mContext;

    private ArrayList<WorkerCodeVO> mList;

    //=============================
    // endregion // variable
    //=============================


    public WorkerCodeMainAdapter(Context context, ArrayList<WorkerCodeVO> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemWorkerCodeListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder finalHolder = (ViewHolder) holder;
        WorkerCodeVO vo = mList.get(position);

        //finalHolder.binding.imgProfile.setImageResource(R.drawable.no_profile);

        finalHolder.binding.tvWorkerName.setText(vo.WOC_02);
        finalHolder.binding.tvPart.setText(vo.WOC_09);
        finalHolder.binding.tvPosition.setText(vo.WOC_10);
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
    public void update(ArrayList<WorkerCodeVO> list) {
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
        ItemWorkerCodeListBinding binding;

        public ViewHolder(ItemWorkerCodeListBinding binding) {
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
    //=============================
    // endregion // ViewHolder
    //=============================
}
