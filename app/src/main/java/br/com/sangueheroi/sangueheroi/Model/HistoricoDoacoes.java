package br.com.sangueheroi.sangueheroi.model;

/**
 * Created by Diego Lucas on 24/04/2016.
 */
public class HistoricoDoacoes {
    private String dataDoacao;

    public String getDataDoacao() {
        return dataDoacao;
    }

    public void setDataDoacao(String dataDoacao) {
        this.dataDoacao = dataDoacao;
    }

    public HistoricoDoacoes(String dataDoacao, String endereco, String cep) {
        this.dataDoacao = dataDoacao;
        this.endereco = endereco;
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;

    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    private String endereco;
    private String cep;

}
