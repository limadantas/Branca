package com.douglasdantas.branca;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.douglasdantas.branca.adapter.ConversasAdapter;
import com.douglasdantas.branca.model.Grupo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView rcvConversas;
    ArrayList<Grupo> grupos = new ArrayList<Grupo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        rcvConversas = (RecyclerView) findViewById(R.id.rv_conversas);
        rcvConversas.setLayoutManager(new LinearLayoutManager(this));
        grupos.add(new Grupo("UEA/EST"));
        grupos.add(new Grupo("Futebol"));
        grupos.add(new Grupo("Basquete"));
        grupos.add(new Grupo("Trabalho"));
        rcvConversas.setAdapter(new ConversasAdapter(this, grupos));

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
        try {
            jsonGrupos = getJSONObjectFromURL("http://projetos.oceanmanaus.com?q="+q);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    public static JSONArray getJSONObjectFromURL(String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));

        char[] buffer = new char[1024];

        String jsonString = new String();

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();

        jsonString = sb.toString();

        System.out.println("JSON: " + jsonString);

        return new JSONArray(jsonString);
    }
}
