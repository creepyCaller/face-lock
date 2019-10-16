package com.iflytek.wordlock;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.wordlock.database.Dao.PairDao;
import com.iflytek.wordlock.database.Model.Pair;

/**
 * Created by 57628 on 2019/5/27.
 */

public class QueryActivity extends Activity implements View.OnClickListener  {
    protected EditText name_query_editText;
    protected EditText value_query_editText;
    protected Button query_update_button;
    private PairDao db;
    private SQLiteDatabase sql_read;
    private SQLiteDatabase sql_write;
    private Pair pair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        db = new PairDao(QueryActivity.this);
        sql_read = db.getReadableDatabase();
        sql_write = db.getWritableDatabase();
        init();
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        int id = Integer.parseInt(bundle.getString("id"));
        pair = new Pair();
        pair = db.queryData(sql_read,id);
        name_query_editText.setText(pair.getName());
        value_query_editText.setText(pair.getValue());
        pair.setId(id);
    }

    private void init() {
        name_query_editText = (EditText) this.findViewById(R.id.name_query_editText);
        value_query_editText = (EditText) this.findViewById(R.id.value_query_editText);
        query_update_button = (Button) this.findViewById(R.id.query_update_button);
        query_update_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.query_update_button:
                exec();
                break;
        }
    }

    private void toast_msg(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void exec() {
        String name = name_query_editText.getText().toString();
        String value = value_query_editText.getText().toString();

        pair.setName(name);
        pair.setValue(value);
        if(db.updateData(sql_write,pair) > 0){
            toast_msg("修改成功");
            finish();
        } else {
            toast_msg("修改失败");
        }
    }

}
