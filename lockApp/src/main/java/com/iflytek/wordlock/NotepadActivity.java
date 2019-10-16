package com.iflytek.wordlock;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.wordlock.database.Model.Pair;
import com.iflytek.wordlock.database.Dao.PairDao;

public class NotepadActivity extends Activity implements View.OnClickListener {
    //声明组件
    protected TextView list_header_textView;
    protected EditText name_editText;
    protected EditText value_editText;
    protected Button add_button;
    //其他变量
    public PairDao db;
    public SQLiteDatabase sql_write;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
        db = new PairDao(NotepadActivity.this);
        sql_write = db.getWritableDatabase();
        this.init();
    }

    protected void init() {
        list_header_textView = (TextView) this.findViewById(R.id.list_header_textView);
        name_editText = (EditText) this.findViewById(R.id.name_editText) ;
        value_editText = (EditText) this.findViewById(R.id.value_editText);
        add_button = (Button) this.findViewById(R.id.add_button);
        list_header_textView.setOnClickListener(this);
        add_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.list_header_textView:
                to_list_activity();
                break;
            case R.id.add_button:
                if(check_fill()) {
                    add_commodities();
                }
                break;
        }
    }

    private boolean check_fill() {
        if (name_editText.getText().toString().isEmpty()) {
            toast_msg("请检查主题是否已填");
            return false;
        }
        return true;
    }

    private void toast_msg(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void add_commodities() {
        Pair pair = new Pair();
        String name = name_editText.getText().toString();
        String value = value_editText.getText().toString();
        pair.setName(name);
        pair.setValue(value);
        if (db.addData(sql_write, pair) > 0) {
            toast_msg("添加成功!");
            to_list_activity();
        } else {
            toast_msg("添加失败!");
        }
    }

    private void to_list_activity() {
        this.setResult(1);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sql_write.close();
        db.close();
    }

}
