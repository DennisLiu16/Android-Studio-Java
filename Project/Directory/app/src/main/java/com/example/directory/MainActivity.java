package com.example.directory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Button btn_input_store;
    private Button btn_ouput_search;
    private Button btn_delete;
    private EditText et_input_name;
    private EditText et_input_phone;
    private EditText et_search_name;
    private TextView tv_output_result;

    private String InputName;
    private String InputPhone;
    private String SearchName;
    private String LastSearchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitElements();
    }

    private void InitElements() {
        btn_input_store = findViewById(R.id.btn_input_store);
        btn_ouput_search = findViewById(R.id.btn_output_search);
        btn_delete = findViewById(R.id.btn_delete);
        et_input_name = findViewById(R.id.et_input_Name);
        et_search_name = findViewById(R.id.et_output_Name);
        et_input_phone = findViewById(R.id.et_input_PhoneNumber);
        tv_output_result = findViewById(R.id.tv_output_ShowNumber);

        btn_input_store.setOnClickListener(store);
        btn_ouput_search.setOnClickListener(search);
        btn_delete.setOnClickListener(delete);
    }

    private View.OnClickListener store = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyBoard();
            /* get info from user input and give alert if empty or phone num is negative*/
            InputName = et_input_name.getText().toString();
            if (TextUtils.isEmpty(InputName))
            {
                Alert("Store_EmptyName");
                return;
            }

            InputPhone = et_input_phone.getText().toString();
            if(TextUtils.isEmpty(InputPhone))
            {
                Alert("Store_EmptyPhone");
                return;
            }

            else if(Integer.parseInt(InputPhone) <=0 )
            {
                Alert("Store_NegPhone");
                return;
            }

            /* check file exist or not*/
            /* get all files' names and put it into a string array as ExistFiles */
            String[] ExistFiles = MainActivity.this.fileList();
            if(isInStringArray(ExistFiles,InputName)) {
                Alert("Store_Existed");
                return;
            }
            store_data();
        }
    };

    private View.OnClickListener search = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*search name in files*/
            hideKeyBoard();

            SearchName = et_search_name.getText().toString();
            /* Empty Check */
            if(TextUtils.isEmpty(SearchName))
            {
                Alert("Search_EmptyName");
                return;
            }
            /* read and set the data in textview */
            try{
                FileInputStream fileIn = openFileInput(SearchName);
                BufferedInputStream bufFileIn = new BufferedInputStream(fileIn);
                byte[] bufBytes = new byte[10];
                tv_output_result.setText("");
                do {
                    int c = bufFileIn.read(bufBytes);
                    if(c == -1)
                        break;
                    else
                        tv_output_result.append(new String(bufBytes));
                }while(true);
                bufFileIn.close();

               Toast.makeText(MainActivity.this,
                        getString(R.string.toast_msg_search_ok),
                        Toast.LENGTH_SHORT).show();
               LastSearchName = SearchName;

            }catch (FileNotFoundException e){
                /* If didn't find such contact person */
                Alert("Search_notFind");
                clear("SearchName");
                SearchName = LastSearchName;
                return;
            }catch (Exception e)
            {
                /* Other issue */
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener delete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /* delete last search person if you confirm to do it */
            if(!TextUtils.isEmpty(SearchName))
                Alert("Delete");
            else
                Alert("Delete_EmptyName");
        }
    };

    /* Deal with Alert Type then show */
    private AlertDialog.Builder Alert(String type)
    {
        String title,msg;
        /* set title and msg according to their type */
        switch (type)
        {
            case "Store_EmptyName":
                title = getString(R.string.alert_topic_store);
                msg = getString(R.string.alert_msg_empty_name);
                break;
            case "Store_EmptyPhone":
                title = getString(R.string.alert_topic_store);
                msg = getString(R.string.alert_msg_empty_phone);
                break;
            case "Store_NegPhone":
                title = getString(R.string.alert_topic_store);
                msg = getString(R.string.alert_msg_negative_phone);
                break;
            case "Store_Existed":
                title = getString(R.string.alert_topic_store);
                msg = getString(R.string.alert_msg_existed_name);
                return showAlert(title,msg,true);
            case "Search_EmptyName":
                title = getString(R.string.alert_topic_search);
                msg = getString(R.string.alert_msg_empty_name);
            case "Search_notFind":
                title = getString(R.string.alert_topic_search);
                msg = getString(R.string.alert_msg_notFind);
                break;
            case "Delete":
                title = getString(R.string.alert_topic_delete);
                msg = getString(R.string.alert_msg_delete) + " : " + SearchName + " ?";
                return showAlert(title,msg,true);
            case "Delete_EmptyName":
                title = getString(R.string.alert_topic_delete);
                msg = getString(R.string.alert_msg_deleteEmpty);
                break;
            default:
                title = "Bug";
                msg = "Bug";
        }
        /* call showAlert method */
        return showAlert(title,msg);
    }

    /* show alert */
    private AlertDialog.Builder showAlert(String title, String context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(context);
        alert.show();
        return alert;
    }

    /* show alert with button yes and no */
    private AlertDialog.Builder showAlert(String title, String context,Boolean setButton) {
        if (setButton){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(title);
            alert.setMessage(context);
            alert.setCancelable(false);
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    if(title == getString(R.string.alert_topic_store))
                    {
                        store_data();
                    }
                    else if(title == getString(R.string.alert_topic_delete))
                    {
                        delete_data();
                        SearchName = LastSearchName = "";
                    }

                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clear("Input_All");
                }
            });
            alert.show();
            return alert;
        }
        else
            return showAlert(title,context);
    }

    private void clear(String target)
    {
        /* clear editText */
        switch(target)
        {
            case "InputName":
                et_input_name.setText("");
                break;
            case "InputPhone":
                et_input_phone.setText("");
                break;
            case "SearchName":
                et_search_name.setText("");
                tv_output_result.setText("");
                break;
            case "Input_All":
                et_input_name.setText("");
                et_input_phone.setText("");
                break;
        }
    }
    /* Store Name and Phone as File */
    private void store_data()
    {
        /* Data ok , confirm to cover or store data */
        FileOutputStream fileOut = null;
        BufferedOutputStream bufFileOut = null;

        try{
            fileOut = openFileOutput(InputName, Context.MODE_PRIVATE);
            bufFileOut = new BufferedOutputStream(fileOut);
            bufFileOut.write(InputPhone.getBytes());
            bufFileOut.close();
            /* show short hint - success*/
            Toast.makeText(MainActivity.this,
                    getString(R.string.toast_msg_store_ok),
                    Toast.LENGTH_SHORT).show();
            clear("Input_All");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void delete_data()
    {
        File dir = getFilesDir();
        File file = new File(dir,SearchName);
        if(file.delete())
        {
            Toast.makeText(MainActivity.this,
                    getString(R.string.toast_msg_delete_ok),
                    Toast.LENGTH_SHORT).show();
            clear("SearchName");
        }
    }

    /* return true if find target in the String array - arr*/
    private static boolean isInStringArray(String[] arr,String target)
    {
        return Arrays.asList(arr).contains(target);
    }

    /* Force to hide the KeyBoard after push the store or search btn */
    private void hideKeyBoard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
