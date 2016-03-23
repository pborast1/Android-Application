package com.braceapps.dusky.uicomponents.uiadaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.braceapps.dusky.activities.PrePairingActivity;
import com.braceapps.dusky.uicomponents.uiholder.OptionCardViewHolder;
import com.braceapps.dusky.R;
import com.braceapps.dusky.activities.ShowDeviceIdActivity;
import com.braceapps.dusky.uicomponents.uipojo.OptionCardPOJO;

import java.util.List;

/**
 * Created by Paresh on 2/26/2015.
 */
public class OptionCardAdaptor extends RecyclerView.Adapter<OptionCardViewHolder> {

    public List<OptionCardPOJO> list;
    public Context context;

    public OptionCardAdaptor(List<OptionCardPOJO> param_list, Context cntxt) {
        list = param_list;
        context = cntxt;

    }

    @Override
    public OptionCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardrow, viewGroup, false);

        return new OptionCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OptionCardViewHolder optionCardViewHolder, int position) {
        final OptionCardPOJO pj = list.get(position);
        optionCardViewHolder.mtagline.setText(pj.getTag());
        optionCardViewHolder.mdetails.setText(pj.getDetail());
        optionCardViewHolder.mstartlink.setText(pj.getStartlink());
        final View.OnClickListener listener4 = new View.OnClickListener() {
            public void onClick(View v) {
                //Handle onClick
                if (pj.getStartlink().matches("Get Started")) {
                    Intent intent = new Intent(context, PrePairingActivity.class);
                    context.startActivity(intent);
                }
                if (pj.getStartlink().matches("Show me device ID")) {
                    Intent intent = new Intent(context, ShowDeviceIdActivity.class);
                    context.startActivity(intent);

                }
            }
        };
        optionCardViewHolder.mlinkLayout.setOnClickListener(listener4);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
