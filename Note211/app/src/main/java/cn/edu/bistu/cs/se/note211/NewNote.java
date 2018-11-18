package cn.edu.bistu.cs.se.note211;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewNote extends AppCompatActivity {

    EditText ed1,ed2;
    ImageButton imageButton;
    MyDataBase myDatabase;
    Cuns cun;
    int ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        ed1=(EditText) findViewById(R.id.editText1);
        ed2=(EditText) findViewById(R.id.editText2);
        imageButton=(ImageButton) findViewById(R.id.saveButton);
        myDatabase=new MyDataBase(this);

        Intent intent=this.getIntent();
        ids=intent.getIntExtra("ids", 0);
        //默认为0，不为0,则为修改数据时跳转过来的
        if(ids!=0){
            cun=myDatabase.getTiandCon(ids);
            ed1.setText(cun.getTitle());
            ed2.setText(cun.getContent());
        }
        //保存按钮的点击事件，他和返回按钮是一样的功能，所以都调用isSave()方法；
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isSave();
            }
        });
    }

    /*
     * 返回按钮调用的方法。
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(NewNote.this)
                .setTitle("保存")
                .setMessage("是否保存笔记")
                .setNegativeButton("不保存", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(NewNote.this,MainActivity.class);
                        startActivity(intent);
                        NewNote.this.finish();
                    }
                })
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       isSave();
                    }
                })
                .create().show();
    }
    private void isSave(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String times = formatter.format(curDate);
        String title=ed1.getText().toString();
        String content=ed2.getText().toString();
        //是要修改数据
        if(ids!=0){
            cun=new Cuns(title,ids, content, times);
            myDatabase.toUpdate(cun);
            Intent intent=new Intent(NewNote.this,MainActivity.class);
            startActivity(intent);
            NewNote.this.finish();
        }
        //新建日记
        else{
            cun=new Cuns(title,content,times);
            myDatabase.toInsert(cun);
            Intent intent=new Intent(NewNote.this,MainActivity.class);
            startActivity(intent);
            NewNote.this.finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.second, menu);
        return true;
    }

}


