package kr.hmit.dmjs.ui.order_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemClientListBinding;
import kr.hmit.dmjs.model.vo.CLT_VO;

public class CilentListAdapter extends RecyclerView.Adapter {

    //=============================
    // region // variable
    //=============================
    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private CilentListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(CilentListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    private Context mContext;

    private ArrayList<CLT_VO> mList;

    //=============================
    // endregion // variable
    //=============================
    public CilentListAdapter(Context context, ArrayList<CLT_VO> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemClientListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CilentListAdapter.ViewHolder finalHolder = (CilentListAdapter.ViewHolder) holder;
        CLT_VO vo = mList.get(position);

        finalHolder.binding.tvOrderNo.setText(vo.CLT_01);
        finalHolder.binding.tvOrderName.setText(vo.CLT_02);
        finalHolder.binding.tvOrderAddress.setText(vo.CLT_1002);
        finalHolder.binding.tvOrderDate.setText(vo.CLT_9904);

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
    public void update(ArrayList<CLT_VO> list) {
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
        ItemClientListBinding binding;

        public ViewHolder(ItemClientListBinding binding) {
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
    //=============================
    // endregion // ViewHolder
    //=============================
}
