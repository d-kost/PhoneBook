package com.phonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    SimpleCursorAdapter adapter;
    SQLiteDatabase phoneBookDatabase;
    EditText etName, etNumber;
    TextView tvCount;
    RadioGroup rgChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.phoneList);
        etName = findViewById(R.id.contactName);
        etNumber = findViewById(R.id.inpNumber);
        tvCount = findViewById(R.id.countContacts);
        rgChoice = findViewById(R.id.sortRadio);

        rgChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("mytag", "id = "+checkedId);
                updateViewList();
                //call update
                //pass checkedId

                //getQueryWithOrder(checkedId);
            }
        });

        DBHelper helper = new DBHelper(this);
        phoneBookDatabase = helper.getWritableDatabase();


        Cursor tunes = phoneBookDatabase.rawQuery(getQueryWithOrder(), null);
        String[] number_fields = tunes.getColumnNames();

        int[] views = { R.id.id, R.id.name, R.id.number, R.id.viber, R.id.telegram, R.id.whatsup };

        adapter = new SimpleCursorAdapter(this, R.layout.phone_item, tunes, number_fields, views, 0 );
//        processResponse();
        lv.setAdapter(adapter);

        tvCount.setText(Integer.toString(tunes.getCount()));
    }

    private String getQueryWithOrder() {
        int chosenId = rgChoice.getCheckedRadioButtonId();

        return getQueryWithOrderBody(chosenId);
    }

    private String getQueryWithOrder(int chosenId) {
        return getQueryWithOrderBody(chosenId);
    }

    private String getQueryWithOrderBody(int chosenId) {
        String query = "SELECT * FROM phone";
        String order = getCheckedOrder(chosenId);
        query += order == null ? "" : " ORDER BY " + order;

        return query;
    }


    private String getCheckedOrder(int chosenId) {
//        int chosenId = rgChoice.getCheckedRadioButtonId();

        String result;
        switch (chosenId) {
            case 2131165308: result = "name";
            break;
            case 2131165309: result = "number";
            break;
            default:  result = null;
        }

        return result;
    }

//    private void processResponse() {
//
//    }

    public void onNewContactClick(View view) {
        String name = etName.getText().toString();
        String number = etNumber.getText().toString();

        if (!name.equals("") && !number.equals("")) {
            addNewContact(name, number);
        }

    }

    private void addNewContact(String name, String number) {
        Random r = new Random();

        Object[] args = {name, number, r.nextInt(2),
                r.nextInt(2), r.nextInt(2)};
        phoneBookDatabase.execSQL("DELETE FROM phone WHERE viber=0 AND telegram=0 AND whatsup=1");

        phoneBookDatabase.execSQL("INSERT INTO phone (name, number, viber, telegram, whatsup) values (?, ?, ?, ?, ?)", args);

        updateViewList();

        etName.setText("");
        etNumber.setText("");

    }

    public void updateViewList() {
        Cursor tunes = phoneBookDatabase.rawQuery(getQueryWithOrder(), null );
        adapter.changeCursor(tunes);
        tvCount.setText(Integer.toString(tunes.getCount()));
    }


}
