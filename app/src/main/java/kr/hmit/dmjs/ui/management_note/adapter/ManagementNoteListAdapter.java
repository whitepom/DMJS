package kr.hmit.dmjs.ui.management_note.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.hmit.dmjs.databinding.ItemManagementNoteBinding;
import kr.hmit.dmjs.model.vo.LED_VO;

public class ManagementNoteListAdapter extends RecyclerView.Adapter {

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
    private Context mContext;
    private ArrayList<LED_VO> mList;

    //===========================
    // initialize
    //===========================
    public ManagementNoteListAdapter(Context context, ArrayList<LED_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ManagementNoteListAdapter.ViewHolder(ItemManagementNoteBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)  {
        LED_VO vo =  mList.get(position);

        ViewHolder finalHolder = (ViewHolder) holder;

        finalHolder.binding.tvClientNo.setText(vo.LED_04); //제목
        finalHolder.binding.tvCategory.setText(vo.LED_ID); //회사이름
        finalHolder.binding.tvClient.setText(getDate(vo.LED_13)); // 작성일시
        finalHolder.binding.tvClientName.setText(vo.LED_12_NM); // 누가
        finalHolder.binding.tvClientNum.setText(vo.LED_21_NM);  // 누구에게




        switch (vo.LED_11) {
            case "0":
                finalHolder.binding.tvCheckType.setSelected(false);

                break;
            case "1":
                finalHolder.binding.tvCheckType.setSelected(true);

                break;

        }



    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<LED_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public static String getDate (String src) {

        long time = Long.parseLong(src.replaceFirst("^.*Date\\((\\d+)\\).*$", "$1"));
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        return format1.format(new Date(time));

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemManagementNoteBinding binding;

        public ViewHolder(ItemManagementNoteBinding binding) {
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
