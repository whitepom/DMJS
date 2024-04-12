package kr.hmit.dmjs.ui.m017.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ItemPurchaseMainListBinding;
import kr.hmit.dmjs.databinding.ItemZag01MainListBinding;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import kr.hmit.dmjs.network.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Zag01MainListAdapter extends RecyclerView.Adapter{

    private ArrayList<ZAG_VO> mList = new ArrayList<>();
    private Context mContext;
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    private String ZAG_01 ="";
    private String ZAG_10 ="";
    private  ViewHolder finalHolder = null;
    private ZAG_VO vo = null;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Zag01MainListAdapter.ViewHolder(ItemZag01MainListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ZAG_VO vo =  mList.get(position);
        ViewHolder finalHolder = (ViewHolder) holder;

        finalHolder.binding.zag02.setText(vo.ZAG_02);
        finalHolder.binding.zag01.setText(vo.ZAG_01);
        finalHolder.binding.zag03.setText(vo.ZAG_03 +"/" + vo.ZAG_03_NM);
        finalHolder.binding.zag05.setText(String.valueOf((int)vo.ZAG_05) +"/" +String.valueOf((int)vo.ZAG_04));
        finalHolder.binding.zag08.setText(vo.ZAG_08 +"~"+vo.ZAG_09 +" (" + String.valueOf(vo.ZAG_07) + ")");
        finalHolder.binding.zag06.setText(String.valueOf(vo.ZAG_06));
        finalHolder.binding.dah14.setText(vo.DAH_14);
        finalHolder.binding.zag10Nm.setText(vo.ZAG_10_NM);
        finalHolder.binding.zag10.setText(vo.ZAG_10);

        if(vo.ZAG_10.equals("N")){
            finalHolder.binding.btnGagaInfo.setVisibility(View.VISIBLE);
            finalHolder.binding.btnGagaInfo.setText("생산시작");
            finalHolder.binding.btnGagaInfo2.setVisibility(View.GONE);
        }
        if(vo.ZAG_10.equals("S")){
            finalHolder.binding.btnGagaInfo.setText("시작취소");
            finalHolder.binding.btnGagaInfo2.setText("작업완료");
            finalHolder.binding.btnGagaInfo2.setVisibility(View.VISIBLE);
        }
        if(vo.ZAG_10.equals("Y")){
            finalHolder.binding.btnGagaInfo.setVisibility(View.GONE);
            finalHolder.binding.btnGagaInfo2.setText("완료취소");
            finalHolder.binding.btnGagaInfo2.setVisibility(View.VISIBLE);
        }

        finalHolder.binding.btnGagaInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(finalHolder.binding.zag10.getText().toString().equals("N")){
                    finalHolder.binding.zag10.setText("S");
                    finalHolder.binding.zag10Nm.setText("진행중");
                    ZAG_U("ZAG_START" , finalHolder.binding.zag01.getText().toString(), finalHolder.binding.zag10.getText().toString());

                }else if(finalHolder.binding.zag10.getText().toString().equals("S")){
                    finalHolder.binding.zag10.setText("N");
                    finalHolder.binding.zag10Nm.setText("미완료");
                    ZAG_U("ZAG_CANCEL" , finalHolder.binding.zag01.getText().toString(), finalHolder.binding.zag10.getText().toString());
                }
                
                if(finalHolder.binding.zag10.getText().toString().equals("N")){
                    finalHolder.binding.btnGagaInfo.setText("생산시작");
                    finalHolder.binding.btnGagaInfo2.setVisibility(View.GONE);
                }
                if(finalHolder.binding.zag10.getText().toString().equals("S")){
                    finalHolder.binding.btnGagaInfo.setVisibility(View.VISIBLE);
                    finalHolder.binding.btnGagaInfo.setText("시작취소");
                    finalHolder.binding.btnGagaInfo2.setText("작업완료");
                    finalHolder.binding.btnGagaInfo2.setVisibility(View.VISIBLE);
                }
                if(finalHolder.binding.zag10.getText().toString().equals("Y")){
                    finalHolder.binding.btnGagaInfo.setVisibility(View.GONE);
                    finalHolder.binding.btnGagaInfo2.setText("완료취소");
                    finalHolder.binding.btnGagaInfo2.setVisibility(View.VISIBLE);
                }
            }
        });

        finalHolder.binding.btnGagaInfo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalHolder.binding.zag10.getText().toString().equals("S")){
                    finalHolder.binding.zag10.setText("Y");
                    finalHolder.binding.zag10Nm.setText("완료");
                    ZAG_U("ZAG_FINSH" , finalHolder.binding.zag01.getText().toString(), finalHolder.binding.zag10.getText().toString());

                }else if(finalHolder.binding.zag10.getText().toString().equals("Y")){
                    finalHolder.binding.zag10.setText("S");
                    finalHolder.binding.zag10Nm.setText("진행중");
                    ZAG_U("ZAG_FINSH_CANCEL" , finalHolder.binding.zag01.getText().toString(), finalHolder.binding.zag10.getText().toString());
                }

                if(finalHolder.binding.zag10.getText().toString().equals("N")){
                    finalHolder.binding.btnGagaInfo.setText("생산시작");
                    finalHolder.binding.btnGagaInfo2.setVisibility(View.GONE);
                }
                if(finalHolder.binding.zag10.getText().toString().equals("S")){

                    finalHolder.binding.btnGagaInfo.setText("시작취소");
                    finalHolder.binding.btnGagaInfo.setVisibility(View.VISIBLE);
                    finalHolder.binding.btnGagaInfo2.setText("작업완료");
                    finalHolder.binding.btnGagaInfo2.setVisibility(View.VISIBLE);
                }
                if(finalHolder.binding.zag10.getText().toString().equals("Y")){
                    finalHolder.binding.btnGagaInfo.setVisibility(View.GONE);
                    finalHolder.binding.btnGagaInfo2.setText("완료취소");
                    finalHolder.binding.btnGagaInfo2.setVisibility(View.VISIBLE);
                }
            }
        });
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
        ItemZag01MainListBinding binding;

        public ViewHolder(@NonNull ItemZag01MainListBinding binding) {
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

    public void ZAG_U(String GUBUN ,String ZAG_01, String ZAG_10) {

        paramMap = ((BaseActivity)BaseActivity.BaseContext).setParamMap("ZAG_ID", GUBUN);
        paramMap.put("ZAG_01", ZAG_01);
        paramMap.put("ZAG_10", ZAG_10);

        Http.zag(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).ZAG_U(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ZAG_VO>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<ZAG_VO> call, Response<ZAG_VO> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        ((BaseActivity)BaseActivity.BaseContext).closeLoadingBar();
                        ((BaseActivity)BaseActivity.BaseContext).toast("활전복 생산관리를 등록하였습니다.");

                    }
                }.sendMessage(msg);
            }
            @Override
            public void onFailure(Call<ZAG_VO> call, Throwable t) {
                call.cancel();
                ((BaseActivity)BaseActivity.BaseContext).closeLoadingBar();
            }
        });
    }

    public void ButtonSetting(String ZAG_10){

    }
}
