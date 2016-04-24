package br.com.sangueheroi.sangueheroi.wrapper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.sangueheroi.sangueheroi.R;
import br.com.sangueheroi.sangueheroi.model.HistoricoDoacoes;

/**
 * Created by Diego Lucas on 24/04/2016.
 */
public class HistDoacoesAdapter extends RecyclerView.Adapter<HistDoacoesAdapter.MyViewHolder> {
    private ArrayList<HistoricoDoacoes> mList;
    private LayoutInflater mLayoutInflater;


    public HistDoacoesAdapter(Context c, ArrayList<HistoricoDoacoes> l) { // pegar os objetos da outra view
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    //Este metodo e criado para que as intancias dos componentes seja guardada e nao seja necessario ficar instanciando todo hora
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) { //quando cria uma nova view
        Log.i("LOG", "onCreateViewHolder()");
        View v = mLayoutInflater.inflate(R.layout.item_historico_doacao, viewGroup, false); //inflar a view onde esta os itens
        MyViewHolder mvh = new MyViewHolder(v); // passo como parametro para o metodo que inflar os componentes da view
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {  //chamado toda a hora, vincula os dados da lista a view
        Log.i("LOG", "onBindViewHolder()");  // aqui vamos colocar os valores da lista nas views
        myViewHolder.tvDataDocao.setText(mList.get(position).getDataDoacao());
        myViewHolder.tvEndereco.setText(mList.get(position).getEndereco());
    }

    @Override
    public int getItemCount() {  //tamanho da lista
        return mList.size();
    }


    public void addListItem(HistoricoDoacoes hd, int position) {  // para adcionar mais objetos a lista
        mList.add(hd);
        notifyItemInserted(position); //notifica que existe um novo item inserido
    }


    public void removeListItem(int position) { //remover o item
        mList.remove(position);
        notifyItemRemoved(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {  //guarda a view para ser reutilizada(cache)
        public TextView tvEndereco;
        public TextView tvDataDocao;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvEndereco = (TextView) itemView.findViewById(R.id.tv_datadoacao);
            tvDataDocao = (TextView) itemView.findViewById(R.id.tv_endereco);

        }

    }
}