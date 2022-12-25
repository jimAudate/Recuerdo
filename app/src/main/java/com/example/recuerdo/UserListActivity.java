package com.example.recuerdo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {
	private ListView lstview;
	private  AdministrationDB mydb;
	private ArrayList<String> ArrayUsuarios;
	private ArrayAdapter<String> adaptadorUsuarios;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		poblarlist();
		references();
		eventos();
		//finish();
	}



	//region References
	private void references(){
		lstview=findViewById(R.id.lstView);
		lstview =(ListView) findViewById(R.id.lstView);
		adaptadorUsuarios = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ArrayUsuarios);
		lstview.setAdapter(adaptadorUsuarios);
	}
	//endregion

	//region EVENTOS
	private void eventos() {
		lstview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				//ArrayUsuarios.remove(position);

				AlertDialog.Builder builder = new AlertDialog.Builder(UserListActivity.this);
				builder.setMessage("Eliminar Usuario")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								AdministrationDB adbs = new AdministrationDB(UserListActivity.this, "BDrecuerdo", null, 1);
								SQLiteDatabase miBD = adbs.getWritableDatabase();
								SharedPreferences mPrefs = getSharedPreferences("datos",0);
								//Query para buscar un register_id
								Cursor curuser_id = miBD.rawQuery("SELECT user_id FROM UserTb", null);
								curuser_id.moveToLast();

								//remove user from database y de SharedPreferences
								EliminarUserEvents(curuser_id.getInt(0));

								mPrefs.edit().clear().apply();

								Intent intent = new Intent(UserListActivity.this, RegisterActivity.class);
								startActivity(intent);
								finish();
							}
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
							}
						});
				AlertDialog d = builder.create();
				d.setTitle("Are you sure ?");
				d.show();


				adaptadorUsuarios.notifyDataSetChanged();
				return true;

			}
		});


	}
	//endregion

	//region Metodos
	private void poblarlist(){
		ArrayUsuarios = new ArrayList<String>();
		ArrayList<String> tr = (ArrayList<String>) getIntent().getExtras().getSerializable("User");
		ArrayUsuarios.addAll(tr);
	}

	private void EliminarUserEvents(int id){
		AdministrationDB adbs = new AdministrationDB(this, "BDrecuerdo", null, 1);
		try(SQLiteDatabase miBD = adbs.getWritableDatabase()){
			if(miBD != null){
				miBD.execSQL("DELETE FROM UserTB WHERE user_id ="+id);
				Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
			}


		}catch (Exception ex){
			Log.e("TAG_", ex.toString());
		}
	}



	//endregion

}