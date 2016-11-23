package com.asolis.mvpexample.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.recyclerview.models.DrawerItem;
import com.asolis.mvpexample.recyclerview.viewholders.DrawerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angelsolis on 7/31/16.
 */
public class DrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<DrawerItem> data = new ArrayList<>();
    private Context context;
    private int VIEW_TYPE_DRAWER_ITEM = 0;

    private OnclickListener onclickListener;

    public DrawerAdapter(Context context, List<DrawerItem> data) {
        this.context = context;
        this.data.addAll(data);
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DRAWER_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DRAWER_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.drawer_item, parent, false);
            return new DrawerViewHolder(view, onclickListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DrawerViewHolder) {
            ((DrawerViewHolder) holder).bind(data.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void clear() {
        data.clear();
    }

    public ArrayList<DrawerItem> getData() {
        return data;
    }

    public void addItem(DrawerItem item) {
        data.add(item);
        notifyItemInserted(data.size() - 1);
    }

    public void addItems(List<DrawerItem> list) {
        if(list != null){
            data.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void removeLastItem() {
        data.remove(data.size() - 1);
        notifyItemRemoved(data.size());
    }

    public void setOnclickListener(OnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

    public interface OnclickListener {
        void onClick(View view, DrawerItem item);
    }
}