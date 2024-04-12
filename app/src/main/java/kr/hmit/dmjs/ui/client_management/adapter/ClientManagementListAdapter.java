package kr.hmit.dmjs.ui.client_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ItemClientManagementListBinding;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.ui.UtilBox;

public class ClientManagementListAdapter extends RecyclerView.Adapter{




    public static String getCategory(String src){
        switch (src){
            case "A" : return "매입매출";
            case "B" : return "매입";
            case "C" : return "매출";
            case "G" : return "고객";
            case "Z" : return "기타";
        }
        return "";
    }
    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private ClientManagementListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ClientManagementListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //===========================
    // variable
    //===========================
    private Context mContext;
    private ArrayList<CLT_VO> mList;

    //===========================
    // initialize
    //===========================
    public ClientManagementListAdapter(Context context, ArrayList<CLT_VO> list){
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClientManagementListAdapter.ViewHolder(ItemClientManagementListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CLT_VO vo =  mList.get(position);
        ClientManagementListAdapter.ViewHolder finalHolder = (ClientManagementListAdapter.ViewHolder) holder;

        finalHolder.binding.tvClientNo.setText(vo.CLT_01);
        finalHolder.binding.tvClient.setText(vo.CLT_02);
        finalHolder.binding.tvClientName.setText(vo.CLT_03);
        finalHolder.binding.tvClientNum.setText(UtilBox.phone(vo.CLT_08));
        finalHolder.binding.tvCategory.setText(vo.CLT_29);


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<CLT_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemClientManagementListBinding binding;

        public ViewHolder(ItemClientManagementListBinding binding) {
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
