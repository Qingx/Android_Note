package com.hua.note.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.hua.note.R;
import com.hua.note.create.CreateActivity;
import com.hua.note.data.NoteEntity;

import java.util.List;

/**
 * Created by Iaovy on 2020/3/27 14:37
 *
 * @email Cymbidium@outlook.com
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ItemViewHolder> {
    private List<NoteEntity> entities;
    private Context context;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyler_note, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.text.setText(entities.get(position).getText());
        holder.time.setText(entities.get(position).getTime());
        holder.itemView.setOnClickListener(v -> CreateActivity.Companion.start(context, entities.get(position).getFlag()));
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
