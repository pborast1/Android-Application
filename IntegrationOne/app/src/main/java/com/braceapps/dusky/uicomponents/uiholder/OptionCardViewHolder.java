package com.braceapps.dusky.uicomponents.uiholder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.braceapps.dusky.R;

/**
 * Created by Paresh on 2/26/2015.
 */
public class OptionCardViewHolder extends RecyclerView.ViewHolder {

    public TextView mtagline;
    public TextView mdetails;
    public TextView mstartlink;
    public RelativeLayout mlinkLayout;

    public OptionCardViewHolder(View v) {
        super(v);
        mtagline = (TextView) v.findViewById(R.id.tagLine);
        mdetails = (TextView) v.findViewById(R.id.descriptionText);
        mstartlink = (TextView) v.findViewById(R.id.startLink);
        mlinkLayout = (RelativeLayout) v.findViewById(R.id.relativeLinkLayout);

    }

}
