package com.douglasdantas.branca.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.douglasdantas.branca.ConversaActivity;
import com.douglasdantas.branca.R;
import com.douglasdantas.branca.model.Grupo;
import com.douglasdantas.branca.model.Mensagem;

import java.util.ArrayList;

/**
 * Created by oceanmanaus on 17/11/2016.
 */

public class MensagensAdapter extends RecyclerView.Adapter<MensagensAdapter.ViewHolder> {
    public static class  ViewHolder  extends RecyclerView.ViewHolder{
        TextView tvID;
        TextView tvMsg;

        public ViewHolder(View v) {
            super(v);
            tvID = (TextView) v.findViewById(R.id.tvID); 
            tvMsg = (TextView) v.findViewById(R.id.tvMsg);
        }
    }

    ArrayList<Mensagem> mensagens;
    Context context;

    public MensagensAdapter(Context ctx, ArrayList<Mensagem> mensagens) {
        this.mensagens = mensagens;
        context = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mensagem_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mensagem mensagem = mensagens.get(position);

        holder.tvID.setText(mensagem.id);
        holder.tvMsg.setText(mensagem.msg);
/*
        holder.tvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v.findViewById(R.id.tvGrupo);
                Toast.makeText(context, tv.getText(), Toast.LENGTH_SHORT).show();

                Intent it = new Intent(context, ConversaActivity.class);
                context.startActivity(it);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return (null != mensagens ? mensagens.size() : 0);
    }


}
