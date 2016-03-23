package com.braceapps.dusky.adaptors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.braceapps.dusky.R;
import com.braceapps.dusky.db.dbhandler.MessagePOJO;
import com.braceapps.dusky.db.dbhandler.RequestLoggerPOJO;

import com.braceapps.dusky.contacts.ContactInfo;
import com.braceapps.dusky.db.dbhandler.CallLogPOJO;
import com.braceapps.dusky.telephonymessage.OutGoingMessage.OutGoingSms;

import java.util.ArrayList;

/**
 * Created by paresh.boraste on 7/25/2015.
 */
public class SlaveLogAdaptor extends BaseAdapter {
    ArrayList<RequestLoggerPOJO> list;
    LayoutInflater inflater;

    RequestLoggerPOJO requestLogger;
    CallLogListViewHolder mCallViewHolder;
    MessageListViewHolder mMessageViewHolder;

    public SlaveLogAdaptor(Context mContext, ArrayList<RequestLoggerPOJO> mList) {
        this.list = mList;
        context = mContext;
    }
    Context context;
    private Activity parentActivity;

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
        requestLogger=list.get(position);
        if(requestLogger.gettitle().matches("msg")){
            MessageListViewHolder mViewHolder;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_message_list, null);
            mViewHolder = new MessageListViewHolder();
            mViewHolder.tvCallBack=(TextView) convertView.findViewById(R.id.Callback);
            convertView.setTag(mViewHolder);
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
            MessagePOJO messageLog=new MessagePOJO(requestLogger.getContactNumber(),requestLogger.getMessage(),requestLogger.getTime(), "0",requestLogger.getContactName(),requestLogger.isLvUnorHide());
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

        }else{
            CallLogListViewHolder mViewHolder;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_calllog_list, null);
            mViewHolder = new CallLogListViewHolder();
            convertView.setTag(mViewHolder);

            mViewHolder.tvContact = (TextView) convertView.findViewById(R.id.contactTV);

            mViewHolder.tvTime = (TextView) convertView.findViewById(R.id.timeTV);
            mViewHolder.tvContactNumber = (TextView) convertView.findViewById(R.id.messageTV);
            mViewHolder.tvCallType = (TextView) convertView.findViewById(R.id.calltypeTV);
            mViewHolder.tvColor=(TextView) convertView.findViewById(R.id.colourTV);
            mViewHolder.tvLocalContact=(TextView) convertView.findViewById(R.id.localContactTV);
            mViewHolder.lvHideUnhide=(LinearLayout)convertView.findViewById(R.id.linearLayoutHideUnHide);
            mViewHolder.tvCallBack=(TextView) convertView.findViewById(R.id.Callback);
            mViewHolder.tvMessageBack=(TextView) convertView.findViewById(R.id.messageback);
            mViewHolder.tvColorUnOrHide=(TextView) convertView.findViewById(R.id.colourTVHideUnhide);
            //Handling the call intent here


            CallLogPOJO callLog=new CallLogPOJO(requestLogger.getContactNumber(),requestLogger.getContactName(),requestLogger.gettitle(),requestLogger.getTime());
            mViewHolder.tvTime.setText(callLog.getTime());
            //  mViewHolder.tvContact.setText(callLog.getContactName());  //not to be done here
            mViewHolder.tvContactNumber.setText(callLog.getContactNumber());
            mViewHolder.tvCallType.setText(callLog.getType());
            switch(callLog.getType()){
                case "Missed Call":mViewHolder.tvColor.setBackgroundColor(Color.parseColor("#D11919"));
                    mViewHolder.tvColorUnOrHide.setBackgroundColor(Color.parseColor("#D11919"));
                    mViewHolder.tvCallType.setTextColor(Color.parseColor("#D11919"));break;
                case "Recieved Call":mViewHolder.tvColor.setBackgroundColor(Color.parseColor("#33AD5C"));
                    mViewHolder.tvColorUnOrHide.setBackgroundColor(Color.parseColor("#33AD5C"));
                    mViewHolder.tvCallType.setTextColor(Color.parseColor("#33AD5C"));break;
                case "Dialed Call":mViewHolder.tvColor.setBackgroundColor(Color.parseColor("#FF9147"));
                    mViewHolder.tvColorUnOrHide.setBackgroundColor(Color.parseColor("#FF9147"));
                    mViewHolder.tvCallType.setTextColor(Color.parseColor("#FF9147"));break;

            }
            if(callLog.isLvUnorHide()){
                mViewHolder.lvHideUnhide.setVisibility(View.VISIBLE);
                mViewHolder.tvColorUnOrHide.setVisibility(View.VISIBLE);
            }else{
                mViewHolder.lvHideUnhide.setVisibility(View.GONE);
                mViewHolder.tvColorUnOrHide.setVisibility(View.GONE);
            }
            ContactInfo cinfo=new ContactInfo();
            String contactName=cinfo.getContactName(context,callLog.getContactNumber());
            if(callLog.getContactName().matches("NotinContactList")){
                mViewHolder.tvContact.setText(callLog.getContactNumber());
                if(contactName==null) {
                    mViewHolder.tvLocalContact.setText("");
                }else{
                    mViewHolder.tvLocalContact.setText(contactName+" (Local Device)");
                }
            }else{
                mViewHolder.tvContact.setText(callLog.getContactName());
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

            mViewHolder.tvMessageBack.setOnClickListener(new View.OnClickListener()

                                                         {

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
                                                         }

            );



        }

    return convertView;
    }

    class CallLogListViewHolder {
        public TextView tvContact, tvColor, tvTime, tvCallType, tvContactNumber, tvLocalContact, tvCallBack, tvMessageBack, tvColorUnOrHide;
        LinearLayout lvHideUnhide;
    }
    class MessageListViewHolder {
        public TextView tvContact, tvMessage, tvTime,tvMessageBack,tvColorUnOrHide,tvCallBack,tvLocalContact;
        LinearLayout lvHideUnhide;

    }

}
