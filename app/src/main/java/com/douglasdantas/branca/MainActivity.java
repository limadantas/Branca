package com.douglasdantas.branca;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.douglasdantas.branca.adapter.ConversasAdapter;
import com.douglasdantas.branca.model.Grupo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_PHONE_STATE = 123;
    SearchView searchView;
    RecyclerView rcvConversas;
    ArrayList<Grupo> grupos = new ArrayList<Grupo>();
    TelephonyManager tManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcvConversas = (RecyclerView) findViewById(R.id.rv_conversas);
        rcvConversas.setLayoutManager(new LinearLayoutManager(this));
        grupos.add(new Grupo("UEA/EST"));
        grupos.add(new Grupo("Futebol"));
        grupos.add(new Grupo("Basquete"));
        grupos.add(new Grupo("Trabalho"));
        rcvConversas.setAdapter(new ConversasAdapter(this, grupos));

        //final String clientId = MqttClient.generateClientId();
        tManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("tel_id", tManager.getDeviceId());
            editor.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);


        MenuItem myActionMenuItem = menu.findItem( R.id.search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("duglas",query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("duglas",s);
                ArrayList<Grupo> grupos = pesquisar(s);
                MainActivity.this.grupos.clear();
                MainActivity.this.grupos.addAll(grupos);

                rcvConversas.getAdapter().notifyDataSetChanged();
                return false;
            }
        });

        return true;
    }

    ArrayList<Grupo> pesquisar(String q) {
        JSONArray jsonGrupos = null;
            JSONThread gth = new JSONThread("http://projetos.oceanmanaus.com/branca.php?q="+q);

            (new Thread(gth)).start();
            while (!gth.isFinished()) {

            }
            jsonGrupos = gth.array;

            //jsonGrupos = getJSONObjectFromURL("http://projetos.oceanmanaus.com?q="+q);

        ArrayList<Grupo> grupos = new ArrayList<>();
        try {
            for (int i = 0; i < jsonGrupos.length(); i++) {
                grupos.add(new Grupo(jsonGrupos.getJSONObject(i).getString("id")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return grupos;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("tel_id", tManager.getDeviceId());
                    editor.commit();
                }
                break;

            default:
                break;
        }
    }

}
