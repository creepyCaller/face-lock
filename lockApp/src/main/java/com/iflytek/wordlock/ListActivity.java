package com.iflytek.wordlock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iflytek.wordlock.database.Dao.PairDao;
import com.iflytek.wordlock.database.Model.Pair;

import static android.R.attr.path;

/**
 * Created by 57628 on 2019/5/23.
 */

public class ListActivity extends Activity implements View.OnClickListener {
    protected TextView add_header_1_textView;
    protected ListView commodities_list_ListView;
    private List<Pair> list;
    private List<Map<String, Object>> disp_buff;
    private PairDao db;
    private SQLiteDatabase sql_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        db = new PairDao(ListActivity.this);
        sql_read = db.getReadableDatabase();
        this.init();
        this.disp_list();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sql_read.close();
        db.close();
    }

    protected void init() {
        add_header_1_textView = (TextView) this.findViewById(R.id.add_header_1_textView);
        commodities_list_ListView = (ListView) this.findViewById(R.id.commodities_list_ListView);
        add_header_1_textView.setOnClickListener(this);
    }

    private void disp_list() {
        list = db.getList(sql_read);
        disp_buff = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < list.size(); ++i) {
            Map<String, Object> list_item = new HashMap<String, Object>();
            list_item.put("value", list.get(i).getValue()); // ¥
            list_item.put("name", list.get(i).getName());
            disp_buff.add(list_item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, disp_buff, R.layout.list_item_layout, new String[]{"name", "value"}, new int[]{R.id.name_textView, R.id.value_textView});
        commodities_list_ListView.setAdapter(adapter);
        commodities_list_ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int id = list.get(i).getId();
                new AlertDialog.Builder(ListActivity.this).setTitle("").setMessage("是否删除该选中表项?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PairDao db = new PairDao(ListActivity.this);
                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        db.deleteData(sqLiteDatabase, id);
                        refresh();
                        Toast.makeText(ListActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
                return true;
            }
        });

        //点击查询
        commodities_list_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(ListActivity.this,QueryActivity.class);
                        final int id = list.get(i).getId();
                        String name = list.get(i).getName();
                        String value = list.get(i).getValue();

                        Bundle bundle = new Bundle();
                        bundle.putString("id", String.valueOf(id));
                        bundle.putString("name", name);
                        bundle.putString("value", value);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_header_1_textView:
                to_add_activity();
                break;
        }
    }

    private void to_add_activity() {
        Intent intent = new Intent(this, NotepadActivity.class);
        intent.putExtra("path", path);
        this.startActivityForResult(intent, 1);
    }

    private void refresh() {
        this.finish();
        Intent intent = new Intent(ListActivity.this, ListActivity.class);
        this.startActivity(intent);
    }

}
