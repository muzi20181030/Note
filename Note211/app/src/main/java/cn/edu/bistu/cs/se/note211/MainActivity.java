package cn.edu.bistu.cs.se.note211;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    ImageButton imageButton;
    ListView lv;
    LayoutInflater inflater;
    ArrayList<Cuns> array;
    ArrayList<Cuns> findarray;
    MyDataBase mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=(ListView) findViewById(R.id.lv_bwlList);
        imageButton=(ImageButton) findViewById(R.id.btnAdd);
        inflater=getLayoutInflater();
        mdb=new MyDataBase(this);
        array=mdb.getArray();
        MyAdapter adapter=new MyAdapter(inflater,array);
        lv.setAdapter(adapter);
        /*
         * 点击listView里面的item,用来修改日记
         */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent=new Intent(getApplicationContext(),NewNote.class);
                intent.putExtra("ids",array.get(position).getIds() );
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        /*
         * 长点后来判断是否删除数据
         */
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                //AlertDialog,来判断是否删除日记。
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("删除")
                        .setMessage("是否删除笔记")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mdb.toDelete(array.get(position).getIds());
                                ArrayList<Cuns> findarray=new ArrayList<>();
                                findarray=mdb.getArray();
                                MyAdapter adapter=new MyAdapter(inflater,findarray);
                                lv.setAdapter(adapter);
                            }
                        })
                        .create().show();
                return true;
            }
        });
        /*
         * 按钮点击事件，用来新建日记
         */
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),NewNote.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent=new Intent(getApplicationContext(),NewNote.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.item2:
                this.finish();
                break;
            case R.id.item3:
                final EditText inputServer = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("查找").setMessage("请输入要查找的相关标题名称").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        lv.setAdapter(null);
                        String str = inputServer.getText().toString();
                        findarray=mdb.findArray(str);
                        MyAdapter adapter=new MyAdapter(inflater,findarray);
                        lv.setAdapter(adapter);
                        if (findarray.size()==0)
                            Toast.makeText(getApplicationContext(),"未找到结果",Toast.LENGTH_SHORT);
                        else
                            Toast.makeText(getApplicationContext(),"已找到结果",Toast.LENGTH_SHORT);
                    }
                });
                builder.show();
            default:
                break;
        }
        return true;
    }
}

