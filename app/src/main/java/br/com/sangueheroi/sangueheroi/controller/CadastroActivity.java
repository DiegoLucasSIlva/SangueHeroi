package br.com.sangueheroi.sangueheroi.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.ksoap2.serialization.SoapPrimitive;

import br.com.sangueheroi.sangueheroi.R;
import br.com.sangueheroi.sangueheroi.model.Usuario;
import br.com.sangueheroi.sangueheroi.network.RequestWS;
import fr.ganfra.materialspinner.MaterialSpinner;

public class CadastroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MaterialSpinner spinner_blood;
    EditText nome,  email, senha, confirmaSenha;
    TextInputLayout iNome, iEmail, iSenha, iConfirmaSenha;
    private Usuario usuario = new Usuario();
    Button          btnSinup;
    String          tipoSanguineoAux;
    RequestWS       requestWs;
    ProgressBar     mProgress;
    private final String TAG = "LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "CadastroActivity- onCreate:");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        setupViews();

        btnSinup = (Button) findViewById(R.id.btn_signup);
        btnSinup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "onClickButton- onCreate:");
                if(validaNome() &&validaEmail() && validaSenha() && validaTipoSanguineo()){
                  //  Toast.makeText(CadastroActivity.this, "Response" + usuario.toString(), Toast.LENGTH_LONG).show();
                    AsyncCallWSCadastro task = new AsyncCallWSCadastro();
                    task.execute();
                }
            }
        });
    }

    //Método para instanciar os widgets da view activity_cadastro

    private void setupViews() {
        Log.d(TAG, "CadastroActivity- setupViews:");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProgress = (ProgressBar) findViewById(R.id.carregando);
        mProgress.setVisibility(View.INVISIBLE);
        nome = (EditText) findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.input_email);
        senha = (EditText) findViewById(R.id.input_password);
        confirmaSenha = (EditText) findViewById(R.id.input_password_confirm);

        iNome = (TextInputLayout) findViewById(R.id.input_layout_name);
        iEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        iSenha = (TextInputLayout) findViewById(R.id.input_layout_password);
        iConfirmaSenha = (TextInputLayout) findViewById(R.id.input_layout_password_confirm);


        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
                R.array.tipos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_blood = (MaterialSpinner) findViewById(R.id.spinner_blood);
        spinner_blood.setAdapter(adapter);
        spinner_blood.setOnItemSelectedListener(this);
    }


    //Método para validar Nome
    private boolean validaNome() {
        Log.d(TAG, "CadastroActivity- validaNome():");

        if (nome.getText().toString().trim().isEmpty()) {
            iNome.setError(getString(R.string.err_msg_name));
            requestFocus(nome);
            return false;
        } else {
            iNome.setErrorEnabled(false);
            usuario.setNome(nome.getText().toString());
        }

        return true;
    }

    //Método para validar Email
    private boolean validaEmail() {
        Log.d(TAG, "CadastroActivity- validaEmail():");

        String em = email.getText().toString().trim();

        if (em.isEmpty() || !isValidEmail(em)) {
            iEmail.setError(getString(R.string.err_msg_email));
            requestFocus(email);
            return false;
        } else {
            iEmail.setErrorEnabled(false);
            usuario.setEmail(email.getText().toString().trim());

        }

        return true;
    }


    //Método para validar Tipo Sanguineo
    private boolean validaTipoSanguineo() {
        Log.d(TAG, "CadastroActivity- validaTipoSanguineo():");

        Log.d(TAG, tipoSanguineoAux);

        if (tipoSanguineoAux.equals("Selecionar Tipo Sanguineo")) {
            Toast.makeText(CadastroActivity.this, getString(R.string.err_msg_tipo_sanguineo), Toast.LENGTH_LONG).show();
            return false;
        } else {
            usuario.setTipo_sanguineo(tipoSanguineoAux);
        }
        return true;
    }





    // Método para validar Senha
    private boolean validaSenha() {
        Log.d(TAG, "CadastroActivity- validaSenha():");

        if (senha.getText().toString().trim().isEmpty()) {
            Log.d(TAG, "CadastroActivity- validaSenha- Um dos campos Esta vazio():");

            iSenha.setError(getString(R.string.err_msg_password));
            requestFocus(senha);
            return false;
        }
       else if (confirmaSenha.getText().toString().trim().isEmpty()) {
            Log.d(TAG, "CadastroActivity- validaSenha- Um dos campos Esta vazio():");

            iConfirmaSenha.setError(getString(R.string.err_msg_password_confirm));
            requestFocus(confirmaSenha);
            return false;
        }
        else if (!senha.getText().toString().equals(confirmaSenha.getText().toString())) {
            Log.d(TAG, "CadastroActivity- validaSenha- Senha divergente():");

            iConfirmaSenha.setError(getString(R.string.err_msg_password_confirm));
            requestFocus(confirmaSenha);
            return false;
        }
        else {
            iSenha.setErrorEnabled(false);
            iConfirmaSenha.setErrorEnabled(false);
            usuario.setSenha(senha.getText().toString().trim());

        }

        return true;
    }

    private static boolean isValidEmail(String email) {

        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        tipoSanguineoAux = (String) parent.getItemAtPosition(position);



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class AsyncCallWSCadastro extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "AsyncCallWSCadastro- onPreExecute");
            mProgress.setVisibility(View.VISIBLE);
            mProgress.setIndeterminate(false);


        }

        @Override
        protected String doInBackground(Void... params) {
            Log.i(TAG, "AsyncCallWSCadastro- doInBackground");

            while (!isCancelled()) {
                requestWs = new RequestWS();

                SoapPrimitive result = requestWs.callServiceCadastraUsuario(usuario);
                if (result == null) {
                    return "false";
                }
                return result.toString();
            }
            return "false";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "AsyncCallWS-Cadastro");

            mProgress.setVisibility(View.INVISIBLE);
            if(result.toString().equals("false")) {
                Toast.makeText(CadastroActivity.this, "Ops, Ocorreu um erro, tente novamente", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(CadastroActivity.this, "Cadastro efetuado com sucesso", Toast.LENGTH_SHORT).show();
                //  Intent main = new Intent(LoginActivity.this, HomeActivity.class);
                //  startActivity(main);
            }


        }
        }


    }

