package com.douglasdantas.branca;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.douglasdantas.branca.adapter.MensagensAdapter;
import com.douglasdantas.branca.model.Mensagem;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConversaActivity extends AppCompatActivity {

    private static final int REQUEST_READ_PHONE_STATE = 123;
    SearchView searchView;
    RecyclerView rcvMensagens;
    ArrayList<Mensagem> mensagens = new ArrayList<>();public final static String TAG = "SensorSender";
    public final int QOS = 0;
    MqttAndroidClient client;
    String clientId;
    boolean conectado;
    String topico_pai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        if (getIntent() != null) {
            Bundle b = getIntent().getExtras();

            String id = b.getString("grupo");
            boolean grupoFlag = b.getBoolean("grupoFlag");

            if (grupoFlag) {
                topico_pai = "/branca/grupos/"+id;
            }
            else {
                topico_pai = "/branca/usuarios/"+id;
            }
        }
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        clientId = sharedPref.getString("tel_id", MqttClient.generateClientId());
        //clientId = MqttClient.generateClientId();
        client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://iot.oceanmanaus.com:1883",
                        clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
/*
                    String payload = "Leitor " + clientId + " está online!";
                    byte[] encodedPayload;*/

                    try {
                        /*encodedPayload = payload.getBytes("UTF-8");

                        MqttMessage message = new MqttMessage(encodedPayload);
                        client.publish("/ocean/sensores/debug", message);*/

                        IMqttToken subToken = client.subscribe(topico_pai, QOS);
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.d(TAG, topico_pai + " sobrescrito");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards

                            }
                        });
                    } catch (MqttPersistenceException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.i(TAG, topic + " --> " + message.toString());

                    JSONObject jsonMsg = new JSONObject(message.toString());
                    Mensagem msg = new Mensagem(jsonMsg.getString("id"), jsonMsg.getString("msg"));

                    mensagens.add(msg);
                    rcvMensagens.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }




        mensagens.add(new Mensagem("branca", "OIIIII"));
        mensagens.add(new Mensagem("douglas", "bora almocarrrrrrrrrrrrrrrrrrrrrrrrrrr"));
        mensagens.add(new Mensagem("branca", "OIIIII"));
        mensagens.add(new Mensagem("Bambam", "Byyyyrrrrrlll"));
        mensagens.add(new Mensagem("branca", "OIIIII"));

        rcvMensagens = (RecyclerView) findViewById(R.id.rv_mensagens);

        rcvMensagens.setLayoutManager(new LinearLayoutManager(this));
        rcvMensagens.setAdapter(new MensagensAdapter(this,mensagens));
    }

    ArrayList<Mensagem> pesquisar(String q) {
        JSONArray jsonMensagens = null;
        JSONThread gth = new JSONThread("http://projetos.oceanmanaus.com/branca.php?q="+q);

        (new Thread(gth)).start();
        while (!gth.isFinished()) {

        }
        jsonMensagens = gth.array;

        //jsonMensagens = getJSONObjectFromURL("http://projetos.oceanmanaus.com?q="+q);

        ArrayList<Mensagem> mensagens = new ArrayList<>();
        try {
            for (int i = 0; i < jsonMensagens.length(); i++) {
                mensagens.add(new Mensagem(
                        jsonMensagens.getJSONObject(i).getString("id"),
                        jsonMensagens.getJSONObject(i).getString("mensagem")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mensagens;
    }
}
