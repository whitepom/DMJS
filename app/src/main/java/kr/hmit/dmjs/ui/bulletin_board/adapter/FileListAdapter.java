package kr.hmit.dmjs.ui.bulletin_board.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemBulletinBoardFileListBinding;


public class FileListAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private ArrayList<String> mList;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FileListAdapter(Context context, ArrayList<String> list){
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemBulletinBoardFileListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder finalHolder = (ViewHolder) holder;
        System.out.println("첨부파일 "+mList.get(position).substring(mList.get(position).lastIndexOf("/")+1));
        finalHolder.binding.tvTitle.setText(mList.get(position).substring(mList.get(position).lastIndexOf("/")+1));
        finalHolder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<String> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBulletinBoardFileListBinding binding;

        public ViewHolder(ItemBulletinBoardFileListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
