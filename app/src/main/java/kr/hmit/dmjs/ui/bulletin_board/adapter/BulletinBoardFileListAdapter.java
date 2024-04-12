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
import kr.hmit.dmjs.databinding.ItemBulletinBoardFileListBinding;
import kr.hmit.dmjs.model.vo.FIL_VO;

public class BulletinBoardFileListAdapter extends RecyclerView.Adapter{
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
        public void onItemClick(View view, FIL_VO vo, int position);
    }
    public interface OnItemClickListener2 {
        public void onItemClick(View view, FIL_VO vo);
    }

    private OnItemClickListener onItemClickListener;
    private OnItemClickListener2 onItemClickListener2;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void setOnItemClickListener2(OnItemClickListener2 onItemClickListener) {
        this.onItemClickListener2 = onItemClickListener;
    }

    //=================================
    // endregion // interface
    //============================

    //=============================
    // region // variable
    //=============================
    private Context mContext;

    private ArrayList<FIL_VO> mList;
    //=============================
    // endregion // variable
    //=============================
    public BulletinBoardFileListAdapter(Context context, ArrayList<FIL_VO> list) {
        mContext = context;
        mList = list;
        init();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemBulletinBoardFileListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder finalHolder = (ViewHolder) holder;
        FIL_VO vo = mList.get(position);

        String[] tempArray = vo.FIL_7001.split("/");
        finalHolder.binding.tvTitle.setText(tempArray[tempArray.length-1]);
        finalHolder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, vo, position);
            }
        });
        finalHolder.binding.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener2.onItemClick(v, vo);
            }
        });

        if(mUser.Value.MEM_01.equals(vo.FIL_98)||vo.FIL_98==null){
            finalHolder.binding.btnDelete.setVisibility(View.VISIBLE);
        }else{
            finalHolder.binding.btnDelete.setVisibility(View.GONE);
        }

    }

    public void update(ArrayList<FIL_VO> list) {
        mList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBulletinBoardFileListBinding binding;


        public ViewHolder(ItemBulletinBoardFileListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
