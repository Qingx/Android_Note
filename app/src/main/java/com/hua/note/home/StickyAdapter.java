package com.hua.note.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.hua.note.R;
import com.hua.note.config.DateFormat;
import com.hua.note.config.MessageEvent;
import com.hua.note.config.Tools;
import com.hua.note.create.CreateActivity;
import com.hua.note.data.NoteEntity;
import com.hua.note.data.StickyEntity;
import com.hua.note.data.UserDaoManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.wl.android.lib.ui.BaseActivity;
import cn.wl.android.lib.utils.Times;

/**
 * Created by Iaovy on 2020/3/27 14:37
 *
 * @email Cymbidium@outlook.com
 */
public class StickyAdapter extends RecyclerView.Adapter<StickyAdapter.ItemViewHolder> {
    private List<StickyEntity> entities;
    private Context context;
    private UserDaoManager userDaoManager;

    public StickyAdapter(Context context, List<StickyEntity> entities) {
        this.context = context;
        this.entities = entities;
    }

    public void updateData(Context context, List<StickyEntity> entities) {
        this.context = context;
        this.entities = entities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        userDaoManager = UserDaoManager.getInstance(context);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyler_sticky, parent, false);
        return new ItemViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.text.setText(Tools.cutStr(entities.get(position).getText(), 16));
        holder.time.setText(DateFormat.yearMonthDayTime(entities.get(position).getTime()) + " " + Tools.getWeekDays());
        holder.itemView.setOnClickListener(v -> {
                    CreateActivity.Companion.start(context, entities.get(position).getId(), "sticky");
                    ((BaseActivity) v.getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
        );
        holder.itemView.setOnLongClickListener(v -> {
            StickyEntity stickyEntity = entities.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            View view = View.inflate(v.getContext(), R.layout.dialog_note, null);
            builder.setView(view);
            builder.setCancelable(true);
            AlertDialog dialog = builder.create();
            dialog.show();
//            Window window = dialog.getWindow();
//            WindowManager.LayoutParams params = window.getAttributes();
//            params.gravity = Gravity.BOTTOM;
//            window.setAttributes(params);
            /**
             * 取消置顶
             */
            ConstraintLayout stickyLayout = view.findViewById(R.id.layout_sticky);
            TextView cancelSticky = view.findViewById(R.id.text_sticky);
            cancelSticky.setText("取消置顶");
            stickyLayout.setOnClickListener(v1 -> {
                NoteEntity noteEntity = new NoteEntity(stickyEntity.getId(), Times.current(), stickyEntity.getText(), stickyEntity.getType(), "default");
                userDaoManager.insertNote(noteEntity);
                userDaoManager.deletesticky(stickyEntity);
                entities.remove(stickyEntity);
                notifyItemRemoved(position);
                notifyDataSetChanged();
                EventBus.getDefault().post(new MessageEvent("updateAdapter"));
                dialog.dismiss();
            });
            /**
             * 删除按钮
             */
            ConstraintLayout removeLayout = view.findViewById(R.id.layout_remove);
            removeLayout.setOnClickListener(v1 -> {
                userDaoManager.deletesticky(stickyEntity);
                entities.remove(stickyEntity);
                notifyItemRemoved(position);
                notifyDataSetChanged();
                dialog.dismiss();
            });


            return false;
        });
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    class ItemViewHolder extends BaseViewHolder {
        private TextView text;
        private TextView time;

        ItemViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.text_item_name);
            time = view.findViewById(R.id.text_item_time);
        }
    }
}
