package kr.hmit.dmjs.ui.PurchaseAbalone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemClientManagementManagerListBinding;
import kr.hmit.dmjs.ui.PurchaseAbalone.vo.NGM_managerVO;


public class NGM_ClientManagementAddListAdapter extends RecyclerView.Adapter{

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private NGM_ClientManagementAddListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(NGM_ClientManagementAddListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<NGM_managerVO> mList;

    //===========================
    // initialize
    //===========================
    public NGM_ClientManagementAddListAdapter(Context context, ArrayList<NGM_managerVO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NGM_ClientManagementAddListAdapter.ViewHolder(ItemClientManagementManagerListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NGM_managerVO vo =  mList.get(position);
        NGM_ClientManagementAddListAdapter.ViewHolder finalHolder = (NGM_ClientManagementAddListAdapter.ViewHolder) holder;

        finalHolder.binding.tvNumber.setText(String.valueOf(position+1));
        finalHolder.binding.tvName.setText(vo.name);
        finalHolder.binding.tvMail.setText(vo.mail);
        finalHolder.binding.tvPhoneNumber.setText(vo.phone);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<NGM_managerVO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemClientManagementManagerListBinding binding;

        public ViewHolder(ItemClientManagementManagerListBinding binding) {
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
