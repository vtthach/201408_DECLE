package com.example.dragtofillcolor;

import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnLongClickListener {
    
    CustomView customView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        customView = (CustomView) findViewById(R.id.customView);
        
        Button btn = (Button) findViewById(R.id.btn1);
        btn.setOnLongClickListener(this);
        btn = (Button) findViewById(R.id.btn2);
        btn.setOnLongClickListener(this);
        btn = (Button) findViewById(R.id.btn3);
        btn.setOnLongClickListener(this);
        
        CheckBox cb = (CheckBox) findViewById(R.id.btn0);
        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                customView.starTestMode(isChecked);
            }
        });
    }
    
    @Override
    public boolean onLongClick(View v) {
        TextView tv = (TextView) v;
        String color = tv.getTextColors().getDefaultColor()+ "";
        ClipData data = ClipData.newPlainText(color,"");
        v.startDrag(data, new DragShadowBuilder(v), v, 0);
        return true;
    }
    
}
