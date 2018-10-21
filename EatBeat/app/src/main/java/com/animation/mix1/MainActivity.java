package com.animation.mix1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText value1 = (EditText) findViewById(R.id.xtxt);
        final EditText value2 = (EditText) findViewById(R.id.ytxt);
        final Button btn_move = (Button) findViewById(R.id.btn1);
        final Button btn_copy = (Button) findViewById(R.id.btn2);
        final Button btn_paste = (Button) findViewById(R.id.btn3);
        btn_paste.setVisibility(View.INVISIBLE);
        final Animation an = AnimationUtils.loadAnimation(this,R.anim.translator);

        btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = Integer.parseInt(value1.getText().toString());
                int y = Integer.parseInt(value2.getText().toString());
                btn_move.animate().xBy(x).yBy(y);
            }
        });
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_paste.setVisibility(v.VISIBLE);
                btn_paste.startAnimation(an);
            }
        });
    }
}
