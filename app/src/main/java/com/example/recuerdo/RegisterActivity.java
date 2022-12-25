package com.example.recuerdo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
private Button btnRegister;
private TextInputLayout tilname,tilpassword,tiltitulo;
private EditText edtTiempo;
private ArrayAdapter<String> miAdaptador;
private ArrayList<String> Respuestas;
private Spinner spnQuestions;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);
    poblar();
    referencias();
    eventos();

}
    //region Eventos
private void eventos() {
    btnRegister.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Function Guarda info to (Register_tbl)
            UserRegister();


        }
    });

    edtTiempo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePick();
        }
    });



}
    //endregion

    //region Referencias
    private void referencias(){
        tilname = findViewById(R.id.tilname);
        tiltitulo=findViewById(R.id.tiltitulo);

        edtTiempo = findViewById(R.id.edtTiempo);

        spnQuestions=findViewById(R.id.spnQuestions);
        tilpassword = findViewById(R.id.tilpassword);
        btnRegister = findViewById(R.id.btnregister);
        miAdaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Respuestas);

        spnQuestions.setAdapter(miAdaptador);

    }
    //endregion

    //region Metodos
    private void DatePick() {
        // on below line we are getting
        // the instance of our calendar.
        final Calendar c = Calendar.getInstance();
        // on below line we are getting
        // our day, month and year.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our edit text.
                        edtTiempo.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, year, month, day);
        datePickerDialog.show();
    }
    private void poblar() {
        Respuestas = new ArrayList<String>();
        Respuestas.add("What's your animal favorite?");
        Respuestas.add("Cat");
        Respuestas.add("Dog");
        Respuestas.add("lion");

    }
    private void limpiarPantalla(){
        tilpassword.getEditText().setText("");
        tilname.getEditText().setText("");
        tiltitulo.getEditText().setText("");
        edtTiempo.setText("");
        tilpassword.getEditText().setText("");
        spnQuestions.setSelection(0);

    }
    private void GuardarUserPrefs(int user_id) {
        //get userID from database
        SharedPreferences mPrefs = getSharedPreferences("datos",MODE_PRIVATE);
        SharedPreferences.Editor editorSP=mPrefs.edit();
        editorSP.putInt("user_id",user_id);
        editorSP.putString("Name",tilname.getEditText().getText().toString());
        editorSP.putString("Titulo",tiltitulo.getEditText().getText().toString());
        editorSP.putString("Tiempo",edtTiempo.getText().toString());
        editorSP.putString("Password",tilpassword.getEditText().getText().toString());
        editorSP.putString("Qsecrete",spnQuestions.getSelectedItem().toString());

        editorSP.commit();


    }
    private void UserRegister(){
        try{
            //Function FILL databases
            insertToDB();
            limpiarPantalla();
        }catch (Exception ex){
            Log.e("TAG_", ex.toString());
        }

    }
    private void insertToDB(){
        AdministrationDB adbs = new AdministrationDB(this, "BDrecuerdo", null, 1);
        try(SQLiteDatabase miBD = adbs.getWritableDatabase()){
            //Forma clásica DML
            String[] parametros = new String[6];
            parametros[0] = null;
            parametros[1] = tilname.getEditText().getText().toString();
            parametros[2] = tiltitulo.getEditText().getText().toString();
            parametros[3] = edtTiempo.getText().toString();
            parametros[4] = tilpassword.getEditText().getText().toString();
            parametros[5] = spnQuestions.getSelectedItem().toString();

            if(miBD != null&& tilpassword!=null&& tilname!=null&&tiltitulo!=null) {


                //####################################################################.
                //get id from userTb
                ContentValues cUser = new ContentValues();
                cUser.put("firstname", tilname.getEditText().getText().toString());
                cUser.put("password", tilpassword.getEditText().getText().toString());
                miBD.insert("UserTb", null, cUser);
                Cursor curuser_id = miBD.rawQuery("SELECT user_id FROM UserTb", null);
                curuser_id.moveToLast();
                //#######################################################################
                String currentDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

                ContentValues cReg = new ContentValues();
                cReg.put("firstname", tilname.getEditText().getText().toString());
                cReg.put("password", tilpassword.getEditText().getText().toString());
                cReg.put("qSecrete",spnQuestions.getSelectedItem().toString());
                cReg.put("user_id",curuser_id.getInt(0));
                miBD.insert("RegisterTB", null, cReg);
	            //####################################################################.
	            ContentValues cvEventos = new ContentValues();
	            cvEventos.put("titulo", tiltitulo.getEditText().getText().toString());
	            cvEventos.put("fecha", currentDate);
	            cvEventos.put("tiempo", edtTiempo.getText().toString());
	            cvEventos.put("user_id", curuser_id.getInt(0));
	            miBD.insert("EventTb", null, cvEventos);
	            //#######################################################################.

	            GuardarUserPrefs(curuser_id.getInt(0));
                miBD.close();

                Intent log = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(log);
                finish();
            }else
            {
                Toast.makeText(this, "Campo(s) no debe ser vacio(s)", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception ex){
            Log.e("TAG_", ex.toString());
        }
    }
    //endregion
}