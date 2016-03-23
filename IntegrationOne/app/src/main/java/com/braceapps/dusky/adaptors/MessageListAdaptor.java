package com.braceapps.dusky.adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.braceapps.dusky.contacts.ContactInfo;
import com.braceapps.dusky.db.dbhandler.MessagePOJO;
import com.braceapps.dusky.telephonymessage.OutGoingMessage.OutGoingSms;
import com.braceapps.dusky.R;

import java.util.ArrayList;


/**
 * Created by Paresh on 1/22/2015.
 */
public class MessageListAdaptor extends BaseAdapter {
    ArrayList<MessagePOJO> list;
    LayoutInflater inflater;
    Context context;

    public MessageListAdaptor(Context mContext, ArrayList<MessagePOJO> mList) {
        this.list = mList;
        context = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MessageListViewHolder mViewHolder;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_message_list, null);
            mViewHolder = new MessageListViewHolder();
            mViewHolder.tvCallBack=(TextView) convertView.findViewById(R.id.Callback);


            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MessageListViewHolder) convertView.getTag();

        }

        /**/
      //  mViewHolder.ivContactImage = (ImageView) convertView.findViewById(R.id.contactImageIV);
        mViewHolder.tvContact = (TextView) convertView.findViewById(R.id.contactTV);
        mViewHolder.tvLocalContact = (TextView) convertView.findViewById(R.id.localContactTV);
        mViewHolder.tvMessage = (TextView) convertView.findViewById(R.id.messageTV);
        mViewHolder.tvTime = (TextView) convertView.findViewById(R.id.timeTV);
        mViewHolder.lvHideUnhide=(LinearLayout)convertView.findViewById(R.id.linearLayoutHideUnHide);

        mViewHolder.tvMessageBack=(TextView) convertView.findViewById(R.id.messageback);
        mViewHolder.tvColorUnOrHide=(TextView) convertView.findViewById(R.id.colourTVHideUnhide);

        mViewHolder.tvCallBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String number = "tel:" + list.get(position).toString().trim();
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(callIntent);
            }
        });
        mViewHolder.tvMessageBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v){
                final EditText sendmessage = new EditText(context);
                new AlertDialog.Builder(context)
                        .setTitle("Send Message")
                        .setMessage("Enter text")
                        .setView(sendmessage)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String actualmessage = sendmessage.getText().toString();
                                new OutGoingSms().sendSms(list.get(position).getContactNumber().trim(),actualmessage);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
            }

        });
        MessagePOJO messageLog=list.get(position);
        mViewHolder.tvMessage.setText(messageLog.getMessage());
        mViewHolder.tvTime.setText(messageLog.getTime());
        mViewHolder.tvContact.setText(messageLog.getContactNumber());

        if(messageLog.isLvUnorHide()){
            mViewHolder.lvHideUnhide.setVisibility(View.VISIBLE);
            mViewHolder.tvColorUnOrHide.setVisibility(View.VISIBLE);
        }else{
            mViewHolder.lvHideUnhide.setVisibility(View.GONE);
            mViewHolder.tvColorUnOrHide.setVisibility(View.GONE);
        }

        ContactInfo cinfo=new ContactInfo();
        String contactName=cinfo.getContactName(context,messageLog.getContactNumber());
        if(messageLog.getContactName().matches("NotinContactList")){
            mViewHolder.tvContact.setText(messageLog.getContactNumber());
            if(contactName==null) {
                mViewHolder.tvLocalContact.setText("");
            }else{
                mViewHolder.tvLocalContact.setText(contactName+" (Local Device)");
            }
        }else{
            mViewHolder.tvContact.setText(messageLog.getContactName());
            if(contactName==null) {
                mViewHolder.tvLocalContact.setText("");
            }else{
                mViewHolder.tvLocalContact.setText(contactName);
            }

        }
        mViewHolder.tvCallBack.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View v){
                String number = "tel:" + list.get(position).getContactNumber().trim();
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(callIntent);
            }
        });

        return convertView;


    }
}

class MessageListViewHolder {
    public TextView tvContact, tvMessage, tvTime,tvMessageBack,tvColorUnOrHide,tvCallBack,tvLocalContact;
    LinearLayout lvHideUnhide;
    public ImageView ivContactImage;
}