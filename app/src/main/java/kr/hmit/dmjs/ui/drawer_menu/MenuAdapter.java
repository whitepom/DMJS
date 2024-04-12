package kr.hmit.dmjs.ui.drawer_menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hmit.dmjs.R;

public class MenuAdapter extends RecyclerView.Adapter {
    //=================================
    // final
    //=================================


    //=================================
    // variable
    //=================================
    private Context mContext;
    private ArrayList<MenuVO> mList;

    //=================================
    // interface
    //=================================
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //=================================
    // initialize
    //=================================
    public MenuAdapter(Context context, ArrayList<MenuVO> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer_menu, parent, false);
        RecyclerView.ViewHolder holder = new ViewHolder(convertView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MenuVO vo = mList.get(position);

        ViewHolder finalHolder = (ViewHolder) holder;

        finalHolder.tvMenuTitle.setText(vo.MenuTitle);

        finalHolder.tvMenuTitle.setSelected(vo.Open);

        finalHolder.mAdapter.update(vo.SubMenu);

        finalHolder.recyclerView.setVisibility(vo.Open ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMenuTitle;
        private RecyclerView recyclerView;
        private SubMenuAdapter mAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuTitle = itemView.findViewById(R.id.tvMenuTitle);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new SubMenuAdapter(mContext);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());


            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();

                for (int i = 0; i < mList.size(); i++) {
                    if (i == pos) {
                        mList.get(i).Open = !mList.get(i).Open;
                    } else {
                        mList.get(i).Open = false;
                    }
                    notifyDataSetChanged();
                }

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, pos);
                }
            });
        }
    }
}
