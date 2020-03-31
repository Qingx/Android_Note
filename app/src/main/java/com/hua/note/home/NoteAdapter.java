package com.hua.note.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
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

import java.sql.Time;
import java.util.List;

import cn.wl.android.lib.ui.BaseActivity;
import cn.wl.android.lib.utils.Times;

/**
 * Created by Iaovy on 2020/3/27 14:37
 *
 * @email Cymbidium@outlook.com
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ItemViewHolder> {
    private List<NoteEntity> entities;
    private Context context;
    private UserDaoManager userDaoManager;

    public NoteAdapter(Context context, List<NoteEntity> entities) {
        this.context = context;
        this.entities = entities;
    }

    public void updateData(Context context, List<NoteEntity> entities) {
        this.context = context;
        this.entities = entities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        userDaoManager = UserDaoManager.getInstance(context);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyler_note, parent, false);
        return new ItemViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.text.setText(Tools.cutStr(entities.get(position).getText(), 16));
        holder.time.setText(DateFormat.yearMonthDayTime(entities.get(position).getTime()) + " " + Tools.getWeekDays());
        holder.itemView.setOnClickListener(v -> {
                    CreateActivity.Companion.start(context, entities.get(position).getId(), "default");
                    ((BaseActivity) v.getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
        );
        holder.itemView.setOnLongClickListener(v -> {
            NoteEntity noteEntity = entities.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            View view = View.inflate(v.getContext(), R.layout.dialog_note, null);
            builder.setView(view);
            builder.setCancelable(true);
            AlertDialog dialog = builder.create();
            dialog.show();

            /**
             * 置顶按钮
             */
            ConstraintLayout stickyLayout = view.findViewById(R.id.layout_sticky);
            stickyLayout.setOnClickListener(v1 -> {
                StickyEntity stickyEntity = new StickyEntity(noteEntity.getId(), Times.current(), noteEntity.getText(), noteEntity.getType(), "sticky");
                userDaoManager.stickyNote(stickyEntity);
                userDaoManager.deleteNote(noteEntity);
                entities.remove(noteEntity);
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
                userDaoManager.deleteNote(noteEntity);
                entities.remove(noteEntity);
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
