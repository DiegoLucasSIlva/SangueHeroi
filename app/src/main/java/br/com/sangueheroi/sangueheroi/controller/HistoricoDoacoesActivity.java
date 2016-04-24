package br.com.sangueheroi.sangueheroi.controller;

import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import br.com.sangueheroi.sangueheroi.R;
import br.com.sangueheroi.sangueheroi.model.HistoricoDoacoes;
import br.com.sangueheroi.sangueheroi.network.RequestWS;
import br.com.sangueheroi.sangueheroi.wrapper.HistDoacoesAdapter;

public class HistoricoDoacoesActivity extends Fragment {
    private final String TAG = "LOG";
    RequestWS requestWs;
    ProgressBar mProgress;
    String email;
    private RecyclerView mRecyclerView;
    ArrayList<HistoricoDoacoes> listHistoricoDoacoes = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "HistoricoDoacaoesActiviy- onCreateView");

        View view = inflater.inflate(R.layout.activity_historico_doacoes, container, false);
        mProgress = (ProgressBar) view.findViewById(R.id.carregando);
        mProgress.setVisibility(View.INVISIBLE);

        email = ((HomeActivity) getActivity()).profile.getEmail().toString();

        AsyncCallWSHistoricoDoacoes task = new AsyncCallWSHistoricoDoacoes();
        task.execute();

        setRecycleView(view);


        return view;
    }

    private void setRecycleView(View view){
        Log.i(TAG, "HistoricoDoacaoesActiviy- setRecycleView");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);  // o tamanho do recyclerView vai ser sempre o mesmo idependente da quantdade de itens
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() { //carregar mais itens na rolagem
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {  //nao iremos implementar
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) { //quando a barra for rolada, ira carregar mais
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager(); //Gerenciar os itens
                HistDoacoesAdapter adapter = (HistDoacoesAdapter) mRecyclerView.getAdapter(); // vincula o recyclerView com o nosso adpter

            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity()); //Gerenciar os itens
        llm.setOrientation(LinearLayoutManager.VERTICAL); //orientacao dos itens
        mRecyclerView.setLayoutManager(llm); //setar o layout dos itens




    }
    private class AsyncCallWSHistoricoDoacoes extends AsyncTask<Void, Void, ArrayList<HistoricoDoacoes>> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "AsyncCallWSHistoricoDoacoes- onPreExecute");
            mProgress.setVisibility(View.VISIBLE);
            mProgress.setIndeterminate(false);


        }

        @Override
        protected ArrayList<HistoricoDoacoes> doInBackground(Void... params) {
            Log.i(TAG, "AsyncCallWSHistoricoDoacoes- doInBackground");

            while (!isCancelled()) {
                requestWs = new RequestWS();

                listHistoricoDoacoes = requestWs.callServiceHistoricoDoacoes(email);
                if (listHistoricoDoacoes == null) {
                    return null;
                }
                return listHistoricoDoacoes;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<HistoricoDoacoes> result) {
            Log.i(TAG, "AsyncCallWSHistoricoDoacoes-Cadastro");

            mProgress.setVisibility(View.INVISIBLE);
            if(result!= null){
            HistDoacoesAdapter adapter = new HistDoacoesAdapter(getActivity(), listHistoricoDoacoes);// retornara o nosso adptar personalizado
            mRecyclerView.setAdapter(adapter); // setamos o nosso adpter no recyclerView

            }
           else{
            Toast.makeText(getActivity(),"Você não possui nenhuma doação registrada", Toast.LENGTH_SHORT).show();

        }


        }
    }
    }


