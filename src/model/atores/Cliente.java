package model.atores;

public class Cliente {
    private final String nome;
    private String contato;
    private Endereco endereco;

    public Cliente(String nome, String contato, Endereco endereco) {
        this.nome = nome;
        this.contato = contato;
        this.endereco = endereco;
    }

    public Cliente(String nome, String contato) {
        this.nome = nome;
        this.contato = contato;
    }

    public String getNome() {
        return nome;
    }

    public String getContato() {
        return contato;
    }

    public Endereco getEndereco() {
        return endereco;
    }
}
