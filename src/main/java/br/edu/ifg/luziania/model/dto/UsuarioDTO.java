package br.edu.ifg.luziania.model.dto;

public class UsuarioDTO {

    private long id;

    private String nome;

    private String username;

    private String email;

    private String perfil;

    private String senha;

    private String cpf;

    public UsuarioDTO() {
    }

    public UsuarioDTO(String nome, String username, String email, String cpf, String perfil, String senha) {
        this.nome = nome;
        this.username = username;
        this.cpf = cpf;
        this.email = email;
        this.perfil = perfil;
        this.senha = senha;
    }

    public UsuarioDTO(int id, String nome, String username, String email, String perfil, String senha, String cpf) {
        this.id = id;
        this.nome = nome;
        this.username = username;
        this.email = email;
        this.perfil = perfil;
        this.senha = senha;
        this.cpf = cpf;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
