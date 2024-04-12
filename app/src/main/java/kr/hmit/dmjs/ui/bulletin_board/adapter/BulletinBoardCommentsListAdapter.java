package kr.hmit.dmjs.ui.bulletin_board.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.base.settings.InterfaceSettings;
import kr.hmit.base.user_interface.InterfaceUser;
import kr.hmit.dmjs.databinding.ItemBulletinBoardCommentsListBinding;
import kr.hmit.dmjs.model.vo.BRDC_VO;

public class BulletinBoardCommentsListAdapter extends RecyclerView.Adapter {
    protected InterfaceSettings mSettings;
    protected InterfaceUser mUser;

    /**
     * 초기화
     */
    private void init() {
        if (mSettings == null)
            mSettings = InterfaceSettings.getInstance(mContext);
        if (mUser == null)
            mUser = InterfaceUser.getInstance();
    }

    //============================
    // region // interface
    //=================================
    public interface OnItemClickListener {
        public void onItemClick(View view, BRDC_VO vo, String GUBUN);
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

    private ArrayList<BRDC_VO> mList;

    //=============================
    // endregion // variable
    //=============================
    public BulletinBoardCommentsListAdapter(Context context, ArrayList<BRDC_VO> list) {
        mContext = context;
        mList = list;
        init();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemBulletinBoardCommentsListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder finalHolder = (ViewHolder) holder;
        BRDC_VO vo = mList.get(position);

        finalHolder.binding.tvWriter.setText(vo.BRDC_04);
        finalHolder.binding.tvDate.setText(vo.BRDC_08_TR);
        finalHolder.binding.tvComment.setText(vo.BRDC_06);
        finalHolder.binding.tvComment.setVisibility(View.VISIBLE);
        finalHolder.binding.etComment.setVisibility(View.GONE);

        finalHolder.binding.btnUpdate2.setText("수정");

        if (!vo.BRDC_05.equals(mUser.Value.MEM_01)) {
            finalHolder.binding.btnDelete2.setVisibility(View.GONE);
            finalHolder.binding.btnUpdate2.setVisibility(View.GONE);
        } else {
            finalHolder.binding.btnDelete2.setVisibility(View.VISIBLE);
            finalHolder.binding.btnUpdate2.setVisibility(View.VISIBLE);
        }
        finalHolder.binding.btnDelete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, vo, "M_DELETE");

            }
        });
        finalHolder.binding.btnUpdate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.binding.btnUpdate2.setText("저장");
                finalHolder.binding.tvComment.setVisibility(View.GONE);
                finalHolder.binding.etComment.setText(vo.BRDC_06);
                finalHolder.binding.etComment.setVisibility(View.VISIBLE);
                finalHolder.binding.etComment.requestFocus();
                finalHolder.binding.etComment.setSelection(finalHolder.binding.etComment.getText().length());
                finalHolder.binding.btnUpdate2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vo.BRDC_06 = finalHolder.binding.etComment.getText().toString();
                        onItemClickListener.onItemClick(v, vo, "M_UPDATE");
                        finalHolder.binding.btnUpdate2.setText("수정");
                    }
                });
            }
        });


    }

    public void update(ArrayList<BRDC_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBulletinBoardCommentsListBinding binding;


        public ViewHolder(ItemBulletinBoardCommentsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
