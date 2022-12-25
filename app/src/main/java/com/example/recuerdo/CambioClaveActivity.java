package com.example.recuerdo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class CambioClaveActivity extends AppCompatActivity {
private Button btnCambioClave;
private TextInputLayout tilcambioClave;
	SharedPreferences mPrefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cambio_clave);
		referencias();
		eventos();
	}


	//region eventos
	private void eventos(){
		btnCambioClave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				cambiarClave();
			}
		});
	}
//endregion

	//region Referencias
	private void referencias(){
			btnCambioClave=findViewById(R.id.btnCambio);
			tilcambioClave=findViewById(R.id.tilcambiopass);
	}
	//endregion

	//region Metodos
	private void cambiarClave(){
		//SharedPreferences Update
 		mPrefs = getSharedPreferences("datos",MODE_PRIVATE);
		String txtpass=tilcambioClave.getEditText().getText().toString();

		if(txtpass.equals("")) {
			Toast.makeText(this, "Check your ADM de sistema", Toast.LENGTH_SHORT).show();
		}
		else{
			SharedPreferences.Editor editorSP;
			editorSP = mPrefs.edit();
			editorSP.putString("Password", txtpass);
			editorSP.commit();

			// Database Update
			int id=mPrefs.getInt("user_id",0);
			updateClavedb(txtpass,id);

			Intent log = new Intent(CambioClaveActivity.this, LoginActivity.class);
			startActivity(log);

			finish();

		}
	}


	private void updateClavedb(String pwd,int user_id){
		AdministrationDB adbs = new AdministrationDB(this, "BDrecuerdo", null, 1);
		try(SQLiteDatabase miBD = adbs.getWritableDatabase()){
			if(miBD != null){
				// Update con ContentValues
				ContentValues cupd = new ContentValues();
				cupd.put("password", pwd);
				String conv=String.valueOf(user_id);
				String[] args = new String[]{conv};
				miBD.update("RegisterTb", cupd, "register_id=?",args);
				//##################################################################
				ContentValues cvupdUser = new ContentValues();
				String conv1=String.valueOf(user_id);
				cvupdUser.put("password", pwd);
				String[] argss = new String[]{conv1};
				miBD.update("UserTb", cvupdUser, "user_id=?", argss);
				miBD.close();
			}else{
				Intent log = new Intent(CambioClaveActivity.this, RegisterActivity.class);
				startActivity(log);
			}

		}catch (Exception ex){
			Log.e("TAG_", ex.toString());
		}
	}
	//endregion
}