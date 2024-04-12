package kr.hmit.dmjs.ui.client_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemClientManagementManagerListBinding;
import kr.hmit.dmjs.ui.client_management.vo.managerVO;

public class ClientManagementAddListAdapter extends RecyclerView.Adapter{

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private ClientManagementAddListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ClientManagementAddListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<managerVO> mList;

    //===========================
    // initialize
    //===========================
    public ClientManagementAddListAdapter(Context context, ArrayList<managerVO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClientManagementAddListAdapter.ViewHolder(ItemClientManagementManagerListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        managerVO vo =  mList.get(position);
        ClientManagementAddListAdapter.ViewHolder finalHolder = (ClientManagementAddListAdapter.ViewHolder) holder;

        finalHolder.binding.tvNumber.setText(String.valueOf(position+1));
        finalHolder.binding.tvName.setText(vo.name);
        finalHolder.binding.tvMail.setText(vo.mail);
        finalHolder.binding.tvPhoneNumber.setText(vo.phone);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<managerVO> list) {
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
