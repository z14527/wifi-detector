package com.test.gyq.detector;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * Created by alphabeting timer on 2016/10/5.
 */

public class MotorSelectWindow extends PopupWindow {


    private Button btn_ok,btn_close,btn_ok_modify,btn_close_modify,btn_cancel;

    private View mMenuView;

    public MotorSelectWindow(Activity context, View.OnClickListener itemsOnClick){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pattern_motor,null);
        btn_ok = (Button) mMenuView.findViewById(R.id.motorOK);
        btn_close = (Button) mMenuView.findViewById(R.id.motorClose);
        btn_cancel = (Button) mMenuView.findViewById(R.id.cancel);
        btn_ok_modify = (Button) mMenuView.findViewById(R.id.motorOKModify);
        btn_close_modify = (Button) mMenuView.findViewById(R.id.motorCloseModify);

        btn_cancel.setOnClickListener(itemsOnClick);
        btn_ok.setOnClickListener(itemsOnClick);
        btn_close.setOnClickListener(itemsOnClick);
        btn_ok_modify.setOnClickListener(itemsOnClick);
        btn_close_modify.setOnClickListener(itemsOnClick);

        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopupAnimation);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);

        mMenuView.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){

                int height = mMenuView.findViewById(R.id.pattern_menu).getTop();
                int y=(int) event.getY();
                if(event.getAction() ==  MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

}
