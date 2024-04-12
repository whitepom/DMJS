package kr.hmit.dmjs.ui.notice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemNoticeMainListBinding;
import kr.hmit.dmjs.model.vo.UNO_VO;
import kr.hmit.dmjs.ui.UtilBox;

public class NoticeMainListAdapter extends RecyclerView.Adapter{
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

    private ArrayList<UNO_VO> mList;

    //=============================
    // endregion // variable
    //=============================

    public NoticeMainListAdapter(Context context, ArrayList<UNO_VO> list) {
        mContext = context;
        mList = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemNoticeMainListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder finalHolder = (ViewHolder) holder;
        UNO_VO vo = mList.get(position);

        //finalHolder.binding.tvTitle.setText(vo.BRD_10);
        finalHolder.binding.tvTitle.setText(vo.UNO_04.replace("&nbsp"," "));
        finalHolder.binding.tvWriter.setText(vo.MEM_02);

//        finalHolder.binding.tvCommentsCount.setText(String.valueOf(Math.round(vo.BRD_15)));

    }


    public void update(ArrayList<UNO_VO> list) {
        mList = list;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemNoticeMainListBinding binding;


        public ViewHolder(ItemNoticeMainListBinding binding) {
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
