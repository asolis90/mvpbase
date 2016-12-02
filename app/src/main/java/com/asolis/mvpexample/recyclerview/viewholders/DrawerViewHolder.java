package com.asolis.mvpexample.recyclerview.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.recyclerview.adapter.DrawerAdapter;
import com.asolis.mvpexample.recyclerview.models.DrawerItem;

/**
 * Created by angelsolis on 7/31/16.
 */
public class DrawerViewHolder extends RecyclerView.ViewHolder {

    public ImageView drawerItemImageView;
    public TextView drawerItemTitle;
    public LinearLayout linearLayout;
    private DrawerItem item;

    public DrawerViewHolder(View itemView, final DrawerAdapter.OnclickListener listener) {
        super(itemView);
        drawerItemImageView = (ImageView) itemView.findViewById(R.id.drawer_item_iv);
        drawerItemTitle= (TextView) itemView.findViewById(R.id.drawer_item_title);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.drawer_item_ll);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, item);
            }
        });
    }

    public void bind(DrawerItem item) {
        this.item = item;
        drawerItemTitle.setText(item.getTitle());
        drawerItemImageView.setImageResource(item.getIcon());
    }
}
