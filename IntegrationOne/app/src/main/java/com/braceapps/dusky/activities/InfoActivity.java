package com.braceapps.dusky.activities;

import com.braceapps.dusky.Servicehandler.InitStatus;
import com.braceapps.dusky.util.SystemUiHider;
import com.braceapps.dusky.R;
import com.braceapps.dusky.Servicehandler.AppConfig;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class InfoActivity extends Activity {


    TextView next,back;
    int pageid;
    TextView fullscreen_contenttitle,fulscree_detail_Text;
    TextView tandc;
    RelativeLayout layout;
    Context localContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info);
        pageid=0;
        localContext=this;
        tandc = (TextView) findViewById(R.id.tandc);

        tandc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.url+"TnC.html"));
                startActivity(browserIntent);

            }
        });

        layout=(RelativeLayout)findViewById(R.id.Frameinfo);
        fullscreen_contenttitle = (TextView) findViewById(R.id.fullscreen_contenttitle);
        fulscree_detail_Text = (TextView) findViewById(R.id.fulscree_detail_Text);
        fullscreen_contenttitle.setText("Share log");
        fulscree_detail_Text.setText("Stay connected when your traveling and never again miss any important call");

        next = (TextView) findViewById(R.id.nextInfo);
        back = (TextView) findViewById(R.id.backInfo);
        back.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pageid--;
                switch (pageid) {
                    case 0:
                        fullscreen_contenttitle.setText("Share log");
                        fulscree_detail_Text.setText("Stay connected when your traveling and never again miss any important call");
                        layout.setBackgroundColor(Color.parseColor("#0099cc"));
                        back.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        fullscreen_contenttitle.setText("Customize it");
                        fulscree_detail_Text.setText("Flexible enough to suit your requirement so that you get alerts only when you want");
                        layout.setBackgroundColor(Color.parseColor("#FFB300"));
                        next.setText("Next");
                        tandc.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        fullscreen_contenttitle.setText("Easy to use");
                        fulscree_detail_Text.setText("It takes only one time configuration to get it working");
                        tandc.setVisibility(View.VISIBLE);
                        layout.setBackgroundColor(Color.parseColor("#33AC70"));
                        next.setText("I Agree");
                        break;
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pageid++;
                switch (pageid) {
                    case 0:
                       /* fullscreen_contenttitle.setText("Share log");
                        fulscree_detail_Text.setText("Stay connected when your travelling and never again miss any important call");
                        */
                        break;
                    case 1:
                        fullscreen_contenttitle.setText("Customize it");
                        fulscree_detail_Text.setText("Flexible enough to suit your requirement so that you get alerts only when you want");
                        layout.setBackgroundColor(Color.parseColor("#FFB300"));
                        back.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        fullscreen_contenttitle.setText("Easy to use");
                        fulscree_detail_Text.setText("It takes only one time configuration to get it working");
                        tandc.setVisibility(View.VISIBLE);
                        layout.setBackgroundColor(Color.parseColor("#33AC70"));
                        next.setText("I Agree");
                        break;
                    case 3:
                        InitStatus init=new InitStatus();
                        init.storetSpecificParameter(localContext,InitStatus.tncAccepted,"1");
                        finish();
                        init.startDesiredActivity(getApplicationContext());
                }

            }
        });


    }

    @Override
            protected void onPostCreate(Bundle savedInstanceState) {
                super.onPostCreate(savedInstanceState);


            }


        }
