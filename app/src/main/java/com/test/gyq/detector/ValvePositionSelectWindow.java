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

public class ValvePositionSelectWindow extends PopupWindow {


    private Button btn_pos1,btn_pos2,btn_pos1_modify,btn_pos2_modify,btn_cancel;

    private View mMenuView;

    public ValvePositionSelectWindow(Activity context, View.OnClickListener itemsOnClick){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.activity_valve_position_select_window,null);
        btn_pos1 = (Button) mMenuView.findViewById(R.id.valvePosition1);
        btn_pos2 = (Button) mMenuView.findViewById(R.id.valvePosition2);
        btn_pos1_modify = (Button) mMenuView.findViewById(R.id.valvePosition1Modify);
        btn_pos2_modify = (Button) mMenuView.findViewById(R.id.valvePosition2Modify);
        btn_cancel = (Button) mMenuView.findViewById(R.id.cancel);

        btn_cancel.setOnClickListener(itemsOnClick);
        btn_pos1.setOnClickListener(itemsOnClick);
        btn_pos2.setOnClickListener(itemsOnClick);
        btn_pos1_modify.setOnClickListener(itemsOnClick);
        btn_pos2_modify.setOnClickListener(itemsOnClick);

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
