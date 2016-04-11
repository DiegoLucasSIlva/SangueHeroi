package br.com.sangueheroi.sangueheroi.model;

/**
 * Created by hugosa on 12/03/16.
 * Tentando commitar sssss
 */
public class Usuario {

    private String nome, email, senha,logradouro,bairro,estado,cep,tipo_sanguineo,dtnascimento,dtultimadoacao,codigo_heroi;

    public Usuario(){
    }
    public Usuario(String nome,String email, String senha, String logradouro, String bairro, String estado, String cep, String tipo_sanguineo, String dtnascimento, String dtultimadoacao, String codigo_heroi) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.estado = estado;
        this.cep = cep;
        this.tipo_sanguineo = tipo_sanguineo;
        this.dtnascimento = dtnascimento;
        this.dtultimadoacao = dtultimadoacao;
        this.codigo_heroi = codigo_heroi;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", logradouro='" + logradouro + '\'' +
                ", bairro='" + bairro + '\'' +
                ", estado='" + estado + '\'' +
                ", cep='" + cep + '\'' +
                ", tipo_sanguineo='" + tipo_sanguineo + '\'' +
                ", dtnascimento='" + dtnascimento + '\'' +
                ", dtultimadoacao='" + dtultimadoacao + '\'' +
                ", codigo_heroi='" + codigo_heroi + '\'' +
                '}';
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTipo_sanguineo() {
        return tipo_sanguineo;
    }

    public void setTipo_sanguineo(String tipo_sanguineo) {
        this.tipo_sanguineo = tipo_sanguineo;
    }

    public String getDtnascimento() {
        return dtnascimento;
    }

    public void setDtnascimento(String dtnascimento) {
        this.dtnascimento = dtnascimento;
    }

    public String getDtultimadoacao() {
        return dtultimadoacao;
    }

    public void setDtultimadoacao(String dtultimadoacao) {
        this.dtultimadoacao = dtultimadoacao;
    }

    public String getCodigo_heroi() {
        return codigo_heroi;
    }

    public void setCodigo_heroi(String codigo_heroi) {
        this.codigo_heroi = codigo_heroi;
    }
}
