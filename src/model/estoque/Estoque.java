package model.estoque;

import exception.EstoqueInsuficienteException;
import exception.ProdutoInvalidoException;
import model.produto.Camisa;

import java.util.ArrayList;

public class Estoque {

    private final ArrayList<Camisa> itens = new ArrayList<>();

    public void adicionar(Camisa camisa, int qtd){
        if (camisa == null || qtd <= 0){
            throw new ProdutoInvalidoException("Produto Inválido");
        }
        for (int i = 0; i < qtd; i++) {
            itens.add(camisa);
        }
    }

    public void remover(Camisa camisa, int qtd) throws EstoqueInsuficienteException{
        if (consultar(camisa) < qtd){
            throw new EstoqueInsuficienteException("A camisa do " + camisa.getTime() + " do tamanho: " + camisa.getTamanho() + " está sem estoque!");
        }
        if (camisa == null){
            throw new ProdutoInvalidoException("Produto Inválido!");
        }
        for (int i = 0; i < qtd; i++) {
            itens.remove(camisa);
        }
    }

    public int consultar(Camisa camisa) {
        int total = 0;
        for(Camisa c : itens){
            if (c.equals(camisa)){
                total++;
            }
        }
        return total;
    }
}
