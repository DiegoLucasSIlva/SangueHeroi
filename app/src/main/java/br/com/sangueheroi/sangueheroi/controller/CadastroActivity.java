package br.com.sangueheroi.sangueheroi.controller;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import br.com.sangueheroi.sangueheroi.R;
import fr.ganfra.materialspinner.MaterialSpinner;

public class CadastroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String sNome, sFone, sTipo, sEmail, sSenha;
    MaterialSpinner spinner_blood;
    EditText nome, fone, email, senha;
    TextInputLayout iNome, iFone, iEmail, iSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        setupViews();

    }

    //Método para instanciar os widgets da view activity_cadastro

    private void setupViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nome = (EditText) findViewById(R.id.input_name);
        fone = (EditText) findViewById(R.id.input_fone);
        email = (EditText) findViewById(R.id.input_email);
        senha = (EditText) findViewById(R.id.input_password);
        iNome = (TextInputLayout) findViewById(R.id.input_layout_name);
        iFone = (TextInputLayout) findViewById(R.id.input_layout_fone);
        iEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        iSenha = (TextInputLayout) findViewById(R.id.input_layout_password);

        TelephonyManager tMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String meuNumero = tMgr.getLine1Number();
        fone.setText(meuNumero);

        //Instaciando e alimentando o Spinner de escolha do tipo sanguineo.
        String[] ITEMS = {"Selecionar Tipo Sanguineo",
                "Tipo A+",
                "Tipo A-",
                "Tipo B+",
                "Tipo B-",
                "Tipo AB+",
                "Tipo AB-",
                "Tipo O+",
                "Tipo O-"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_blood = (MaterialSpinner) findViewById(R.id.spinner_blood);
        spinner_blood.setAdapter(adapter);
        spinner_blood.setOnItemSelectedListener(this);
    }


    //Método para validar Nome
    private boolean validaNome() {
        if (nome.getText().toString().trim().isEmpty()) {
            iNome.setError(getString(R.string.err_msg_name));
            requestFocus(nome);
            return false;
        } else {
            iNome.setErrorEnabled(false);
        }

        return true;
    }

    //Método para validar Email
    private boolean validaEmail() {
        String em = email.getText().toString().trim();

        if (em.isEmpty() || !isValidEmail(em)) {
            iEmail.setError(getString(R.string.err_msg_email));
            requestFocus(email);
            return false;
        } else {
            iEmail.setErrorEnabled(false);
        }

        return true;
    }

    // Método para validar Senha
    private boolean validaSenha() {
        if (senha.getText().toString().trim().isEmpty()) {
            iSenha.setError(getString(R.string.err_msg_password));
            requestFocus(senha);
            return false;
        } else {
            iSenha.setErrorEnabled(false);
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
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_mode_close_button) {
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
