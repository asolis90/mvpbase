package com.asolis.mvpexample.recyclerview.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.recyclerview.adapter.DrawerAdapter;
import com.asolis.mvpexample.recyclerview.models.DrawerItem;

/**
 * Created by angelsolis on 7/31/16.
 */
public class DrawerHeaderViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout relativeLayout;

    public DrawerHeaderViewHolder(View itemView, final DrawerAdapter.OnHeaderClickListener listener) {
        super(itemView);
        relativeLayout = (RelativeLayout) itemView.findViewById(R.id.drawer_item_rl);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }
}
