package com.example.safinv.test.view.dialogs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.safinv.test.view.FloatingLayout;
import com.example.safinv.test.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by safin.v on 14.04.2016.
 */
public class DialogWindow extends FloatingLayout {

    public static final int DLG_FOOD = 0;
    public static final int DLG_HOSPITAL = 1;

    private ListView listView;
    private TextView textView;

    public DialogWindow(Context context) {
        super(context);
        init();
    }

    public DialogWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View DlgFoodView = inflater.inflate(R.layout.dlg_window, null);

        listView = (ListView)DlgFoodView.findViewById(R.id.listView);
        textView = (TextView)DlgFoodView.findViewById(R.id.dlg_text_title);


        this.prepareForAddView(DlgFoodView);
    }

    public void dlgShow(int dlg_id, int x, int y){
        final String ATTRIBUTE_NAME_TEXT = "text";
        final String ATTRIBUTE_NAME_IMAGE = "image";


        switch (dlg_id){
            case DLG_FOOD:
                textView.setText("Хавчик");
                int img = R.mipmap.ico_hunger;
                final String[] CountArray = {"100","65","45","76","56"};

                ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
                        CountArray.length);
                Map<String, Object> m;

                for (int i = 0; i < CountArray.length; i++) {
                    m = new HashMap<String, Object>();
                    m.put(ATTRIBUTE_NAME_TEXT, CountArray[i]);
                    m.put(ATTRIBUTE_NAME_IMAGE, img);
                    data.add(m);
                }

                ListAdapter adapter = new SimpleAdapter(getContext(), data, R.layout.food_item,
                        new String[] { ATTRIBUTE_NAME_IMAGE, ATTRIBUTE_NAME_TEXT }, new int[] {
                        R.id.ivImg, R.id.tvText });

                listView.setAdapter(adapter);

                this.setRawX(x);
                this.setRawY(y);
                this.updateViewPosition();
                this.show();
                break;

            case DLG_HOSPITAL:
                //dlgMenuShow();
                break;
        }

    }


}
