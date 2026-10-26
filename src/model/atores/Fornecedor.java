package model.atores;

public class Fornecedor {
    private final String nome;
    private final String pais;
    private final String contato;

    public Fornecedor(String nome, String pais, String contato) {
        this.nome = nome;
        this.pais = pais;
        this.contato = contato;
    }

    public String getNome() {
        return nome;
    }

    public String getPais() {
        return pais;
    }

    public String getContato() {
        return contato;
    }
}
