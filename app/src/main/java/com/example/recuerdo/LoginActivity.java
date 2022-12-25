package com.example.recuerdo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
	private TextInputLayout tilpassword,tilemail;
	private Button btnconexion,btnchangeClave;
	private SharedPreferences mPrefs;
	private ArrayList<String> ArrayString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		referencias();
		eventos();
		getNamefromPrefs();

	}

	//region eventos
	private void eventos(){
		btnconexion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getUsuarioToList();
			}
		});

		btnchangeClave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent log = new Intent(LoginActivity.this, CambioClaveActivity.class);
				startActivity(log);
			}
		});
	}

	//endregion

	//region Metodos
	private void getNamefromPrefs(){
		mPrefs = getSharedPreferences("datos",MODE_PRIVATE);
		String textname = mPrefs.getString("Name","");
		tilemail.getEditText().setText(textname);
	}
	private void getUsuarioToList() {
		String txtpassEnter=tilpassword.getEditText().getText().toString();
		if(txtpassEnter.equals("")){
			Toast.makeText(this, "Password no debe ser Vacio", Toast.LENGTH_SHORT).show();
		}else {
			mPrefs = getSharedPreferences("datos", MODE_PRIVATE);
			String txtname = mPrefs.getString("Name", null);
			String txttiempo = mPrefs.getString("Tiempo", null);
			String txtpass = mPrefs.getString("Password", null);
			String txttitulo = mPrefs.getString("Titulo", "");
			String txtQsecrete = mPrefs.getString("Qsecrete", "");
			if(txtpass.equals(txtpassEnter)) {
				//Fill arrraylist
				ArrayString = new ArrayList<>();
				ArrayString.add(txtname);
				ArrayString.add(txttitulo);
				ArrayString.add(txtpass);
				ArrayString.add(txttiempo);
				ArrayString.add(txtQsecrete);

				Intent userList = new Intent(LoginActivity.this, UserListActivity.class);
				userList.putExtra("User", ArrayString);
				startActivity(userList);
				//pasar data to the view

			}
			else {
				tilpassword.setError("Password incorrect");
			}
		}


	}
	//endregion

	//region Referencias
	private void referencias(){
		btnconexion =  findViewById(R.id.btnconexion);
		tilemail = (TextInputLayout) findViewById(R.id.tilemail);
		tilpassword = (TextInputLayout) findViewById(R.id.tilpassword);
		btnchangeClave = (Button) findViewById(R.id.btnChange);


	}
	//endregion


}