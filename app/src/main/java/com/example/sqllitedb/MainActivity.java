package com.example.sqllitedb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.print.PrinterInfo;
import android.text.StaticLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;
import java.util.stream.LongStream;

import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity implements  Button.OnClickListener{
EditText editRollno,editName,editMarks;
Button btnAdd,btnDelete,btnModify,btnView,btnViewAll,btnShowInfo;
SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editRollno=findViewById(R.id.editRollno);
        editName=findViewById(R.id.editName);
        editMarks=findViewById(R.id.editMarks);
        btnAdd=findViewById(R.id.btnAdd);
        btnDelete=findViewById(R.id.btnDelete);
        btnModify=findViewById(R.id.btnModify);
        btnView=findViewById(R.id.btnView);
        btnViewAll=findViewById(R.id.btnViewAll);
        btnShowInfo=findViewById(R.id.btnShow);

        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnViewAll.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnShowInfo.setOnClickListener(this);
        btnModify.setOnClickListener(this);

        db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnAdd:
                if (editRollno.getText().toString().trim().length()==0||editName.getText().toString().trim().length()==0||editMarks.getText().toString().trim().length()==0){
                    Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
                    shmsg("Error","Invalid Input");
                   return;
                }
                db.execSQL("INSERT INTO student VALUES('"+editRollno.getText()+"','"+editName.getText()+
                        "','"+editMarks.getText()+"');");
                shmsg("Success", "Record added");
                clearText();
                break;

            case R.id.btnDelete:
                if(editRollno.getText().toString().trim().length()==0)
                {
                    shmsg("Error", "Please enter Rollno");
                    return;
                }
                Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+editRollno.getText()+"'", null);
                if(c.moveToFirst())
                {
                    db.execSQL("DELETE FROM student WHERE rollno='"+editRollno.getText()+"'");
                    shmsg("Success", "Record Deleted");
                }
                else
                {
                    shmsg("Error", "Invalid Rollno");
                }
                clearText();
                break;

            case R.id.btnModify:
                if(editRollno.getText().toString().trim().length()==0)
                {
                    shmsg("Error", "Please enter Rollno");
                    return;
                }
                c=db.rawQuery("SELECT * FROM student WHERE rollno='"+editRollno.getText()+"'", null);
                if(c.moveToFirst())
                {
                    db.execSQL("UPDATE student SET name='"+editName.getText()+"',marks='"+editMarks.getText()+
                            "' WHERE rollno='"+editRollno.getText()+"'");
                    shmsg("Success", "Record Modified");
                }
                else
                {
                    shmsg("Error", "Invalid Rollno");
                }
                clearText();
                break;

            case R.id.btnView:
                if(editRollno.getText().toString().trim().length()==0)
                {
                    shmsg("Error", "Please enter Rollno");
                    return;
                }
                 c=db.rawQuery("SELECT * FROM student WHERE rollno='"+editRollno.getText()+"'", null);
                if(c.moveToFirst())
                {
                    editName.setText(c.getString(1));
                    editMarks.setText(c.getString(2));
                }
                else
                {
                    shmsg("Error", "Invalid Rollno");
                    clearText();
                }
                break;

            case R.id.btnViewAll:
                c=db.rawQuery("SELECT * FROM student", null);
                if(c.getCount()==0)
                {
                    shmsg("Error", "No records found");
                    return;
                }
                StringBuffer buffer=new StringBuffer();
                while(c.moveToNext())
                {
                    buffer.append("Rollno: "+c.getString(0)+"\n");
                    buffer.append("Name: "+c.getString(1)+"\n");
                    buffer.append("Marks: "+c.getString(2)+"\n\n");
                }
                shmsg("Student Details", buffer.toString());
                break;

            case R.id.btnShow:
               shmsg("Developed By ", "Swapnil Vahile");
                break;
        }
    }

    private void clearText() {
        editMarks.setText("");
        editName.setText("");
        editRollno.setText("");
    }

    private void shmsg(String title, String msg) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setCancelable(true);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.mipmap.ic_launcher_round);
        alertDialog.show();

    }

}
