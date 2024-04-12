package kr.hmit.dmjs.ui.main_dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.hmit.dmjs.databinding.ItemMainDashboardNoteBinding;
import kr.hmit.dmjs.model.vo.LED_VO;

public class MainDashboardNoteListAdapter extends RecyclerView.Adapter{


    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private MainDashboardNoteListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MainDashboardNoteListAdapter.OnItemClickListener onItemClickListener) {
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
    public MainDashboardNoteListAdapter(Context context, ArrayList<LED_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainDashboardNoteListAdapter.ViewHolder(ItemMainDashboardNoteBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)  {
        LED_VO vo =  mList.get(position);
        MainDashboardNoteListAdapter.ViewHolder finalHolder = (MainDashboardNoteListAdapter.ViewHolder) holder;

        finalHolder.binding.tvTitle.setText(vo.LED_04); //제목
        finalHolder.binding.tvDate.setText(getDate(vo.LED_13)); // 작성일시
        finalHolder.binding.tvSender.setText(vo.LED_12_NM); // 누가
        finalHolder.binding.tvReceiver.setText("→ "+vo.LED_21_NM);  // 누구에게

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
        ItemMainDashboardNoteBinding binding;

        public ViewHolder(ItemMainDashboardNoteBinding binding) {
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
