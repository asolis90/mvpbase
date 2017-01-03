package com.asolis.mvpexample.recyclerview.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.recyclerview.adapter.FingerprintAdapter;
import com.asolis.mvpexample.recyclerview.models.DrawerItem;
import com.asolis.mvpexample.recyclerview.models.FingerprintItem;

/**
 * Created by angelsolis on 7/31/16.
 */
public class FingerprintViewHolder extends RecyclerView.ViewHolder {

    public TextView drawerItemTitle;
    public LinearLayout linearLayout;
    private FingerprintItem item;

    public FingerprintViewHolder(View itemView, final FingerprintAdapter.OnclickListener listener) {
        super(itemView);
        drawerItemTitle = (TextView) itemView.findViewById(R.id.drawer_item_title);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.drawer_item_ll);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, item);
            }
        });
    }

    public void bind(FingerprintItem item) {
        this.item = item;
        drawerItemTitle.setText(item.getTitle());
    }
}
