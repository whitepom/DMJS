package kr.hmit.dmjs.ui.m017.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.databinding.ItemZag01MainListBinding;
import kr.hmit.dmjs.databinding.ItemZag02MainListBinding;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import kr.hmit.dmjs.network.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Zag02MainListAdapter extends RecyclerView.Adapter{

    private ArrayList<ZAG_VO> mList = new ArrayList<>();
    private Context mContext;
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Zag02MainListAdapter.ViewHolder(ItemZag02MainListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ZAG_VO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;

        finalHolder.binding.zag02.setText(vo.ZAG_02);
        finalHolder.binding.zag01.setText(vo.ZAG_01);
        finalHolder.binding.zag03.setText(vo.ZAG_03 +"/" + vo.ZAG_03_NM);
        finalHolder.binding.zag05.setText(String.valueOf((int)vo.ZAG_05) +"/" +String.valueOf((int)vo.ZAG_04));
        finalHolder.binding.zag08.setText(vo.ZAG_12 +"~"+vo.ZAG_13 +" (" + String.valueOf(vo.ZAG_11) + ")");
        finalHolder.binding.zag06.setText(String.valueOf(vo.ZAG_06));
        finalHolder.binding.dah14.setText(vo.DAH_14);
        finalHolder.binding.zag10Nm.setText(vo.ZAG_10_NM);
        finalHolder.binding.lotwCnt.setText(vo.LOTW_CNT +" 명");

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setZagList(ArrayList<ZAG_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemZag02MainListBinding binding;

        public ViewHolder(@NonNull ItemZag02MainListBinding binding) {
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

    //ItemClick Event
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
