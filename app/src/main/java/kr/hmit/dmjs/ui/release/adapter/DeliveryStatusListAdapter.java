package kr.hmit.dmjs.ui.release.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemReleaseInfoListBinding;
import kr.hmit.dmjs.databinding.ItemReleaseMainListBinding;
import kr.hmit.dmjs.model.vo.RUN_VO;

public class DeliveryStatusListAdapter extends RecyclerView.Adapter {
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
    private ArrayList<RUN_VO> mList;
    private ArrayList<RUN_VO> mListcheck = new ArrayList<>();

    //=============================
    // endregion // variable
    //=============================

    public DeliveryStatusListAdapter(Context context, ArrayList<RUN_VO> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemReleaseInfoListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder finalHolder = (ViewHolder) holder;
        RUN_VO vo = mList.get(position);

        finalHolder.binding.tvRUN01.setText(vo.RUN_01);
        finalHolder.binding.tvRUN13.setText(vo.RUN_13_NM);
        finalHolder.binding.tvDAH02.setText(vo.DAH_02);
        finalHolder.binding.tvDAH01.setText(" ("+vo.DAH_01+") ");
        finalHolder.binding.tvRunDate.setText(vo.RUN_02);
        finalHolder.binding.tvCount.setText(vo.RUN_06);
        finalHolder.binding.tvCLT02.setText(vo.RUN_03_NM);
        finalHolder.binding.tvPrice.setText(vo.RUN_07);



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
    public void update(ArrayList<RUN_VO> list) {
        mList = list;

        notifyDataSetChanged();
        mListcheck=new ArrayList<>();

    }


    //=============================
    // region // ViewHolder
    //=============================
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemReleaseInfoListBinding binding;

        public ViewHolder(ItemReleaseInfoListBinding binding) {
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
