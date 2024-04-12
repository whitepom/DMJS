package kr.hmit.dmjs.ui.release.adapter;

import static kr.hmit.dmjs.ui.UtilBox.datePatternChange;

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

import kr.hmit.dmjs.databinding.ItemReleaseMainListBinding;
import kr.hmit.dmjs.model.vo.REQ_VO;
import kr.hmit.dmjs.model.vo.RUN_VO;

public class ReleaseListAdapter extends RecyclerView.Adapter {
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
    private ArrayList<REQ_VO> mList;
    private ArrayList<REQ_VO> mListcheck = new ArrayList<>();

    //=============================
    // endregion // variable
    //=============================

    public ReleaseListAdapter(Context context, ArrayList<REQ_VO> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemReleaseMainListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder finalHolder = (ViewHolder) holder;
        REQ_VO vo = mList.get(position);

        finalHolder.binding.tvOrderNumber.setText(vo.REQ_01);

        finalHolder.binding.REQ03NM.setText(vo.REQ_03_NM);
        finalHolder.binding.tvDAH02.setText(vo.DAH_02);
        finalHolder.binding.tvReleaseDate.setText(datePatternChange(vo.REQ_08));
        finalHolder.binding.tvReleaseCnt.setText(vo.REQ_6004+"/"+vo.REQ_06);
        finalHolder.binding.DAH01.setText(vo.REQ_04);
        finalHolder.binding.tvEA.setText("/ 단위("+String.valueOf(vo.DAH_04)+")");
        finalHolder.binding.chkRelease.setChecked(false);
        finalHolder.binding.chkRelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    finalHolder.binding.tvInput.setText(Integer.parseInt(vo.REQ_06)-Integer.parseInt(vo.REQ_6004)+"");
                }else{
                    finalHolder.binding.tvInput.setText("0");
                }
            }
        });
        finalHolder.binding.tvInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tempStr = finalHolder.binding.tvInput.getText().toString();
                int tempIndex = mList.indexOf(vo);
                if (tempStr.equals("")) {
                    vo.REQ_6004N="0";
                }else if (Integer.parseInt(tempStr)==0) {
                    vo.REQ_6004N="0";
                }else {
                    vo.REQ_6004N=tempStr;
                }
                if(tempIndex>=0){
                    mList.set(tempIndex,vo);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public ArrayList<REQ_VO> getCheckList(){
        ArrayList<REQ_VO> mListcheck = new ArrayList<>();
        for(REQ_VO vo : mList){
            if(vo.REQ_6004N!=null&&!vo.REQ_6004N.isEmpty()){
                if(Integer.parseInt(vo.REQ_6004N)>0){
                    mListcheck.add(vo);
                }
            }
        }

        return mListcheck;
    }

    //=============================
    // region // methods
    //=============================

    /**
     * 데이터를 업데이트 한다.
     *
     * @param list
     */
    public void update(ArrayList<REQ_VO> list) {
        mList = list;

        notifyDataSetChanged();
        mListcheck=new ArrayList<>();

    }


    //=============================
    // region // ViewHolder
    //=============================
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemReleaseMainListBinding binding;

        public ViewHolder(ItemReleaseMainListBinding binding) {
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
