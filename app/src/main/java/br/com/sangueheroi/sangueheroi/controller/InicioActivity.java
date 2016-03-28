package br.com.sangueheroi.sangueheroi.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.sangueheroi.sangueheroi.R;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import br.com.sangueheroi.sangueheroi.R;

public class InicioActivity extends AppCompatActivity {
    Button btnCadastro;
    Button btnLogin;
    //teste

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnCadastro = (Button) findViewById(R.id.btn_cadastro);
        btnLogin = (Button) findViewById(R.id.btn_login);
        //testando git
        //testando 2


    }

    public void sendCadastro(View view) {

        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void sendLogin(View view) {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}

