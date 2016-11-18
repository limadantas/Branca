package com.douglasdantas.branca.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.douglasdantas.branca.R;
import com.douglasdantas.branca.model.Grupo;

import java.util.ArrayList;

/**
 * Created by oceanmanaus on 17/11/2016.
 */

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.ViewHolder> {
    public static class  ViewHolder  extends RecyclerView.ViewHolder{
        TextView tvGrupo;

        public ViewHolder(View v) {
            super(v);
            tvGrupo = (TextView) v.findViewById(R.id.tvGrupo);
        }
    }

    ArrayList<Grupo> grupos;
    Context context;

    public ConversasAdapter(Context ctx, ArrayList<Grupo> grupos) {
        this.grupos = grupos;
        context = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grupo_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Grupo grupo = grupos.get(position);

        holder.tvGrupo.setText(grupo.id);
    }

    @Override
    public int getItemCount() {
        return (null != grupos ? grupos.size() : 0);
    }


}
