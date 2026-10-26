import enums.Tamanho;
import enums.Versao;
import exception.EstoqueInsuficienteException;
import exception.PagamentoRecusadoException;
import exception.ProdutoInvalidoException;
import model.atores.Cliente;
import model.atores.Endereco;
import model.atores.Fornecedor;
import model.estoque.Estoque;
import model.produto.Camisa;
import model.produto.CamisaAtual;
import model.produto.CamisaRetro;
import model.venda.ItemPedido;
import model.venda.Pedido;
import pagamento.Pagamento;
import pagamento.PagamentoCartao;
import pagamento.PagamentoPix;
import service.PedidoService;
import service.PrecificacaoService;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final double COTACAO_DOLAR = 4.9;
    private static final Scanner sc = new Scanner(System.in);

    private static final ArrayList<Camisa> catalogo = new ArrayList<>();
    private static final ArrayList<Cliente> clientes = new ArrayList<>();
    private static final ArrayList<Pedido> pedidos = new ArrayList<>();

    private static final Estoque estoque = new Estoque();
    private static final Fornecedor fornecedor =
            new Fornecedor("Fornecedor Padrão", "China", "contato@fornecedor.com");
    private static final PedidoService pedidoService = new PedidoService(estoque);
    private static final PrecificacaoService precificacao = new PrecificacaoService(COTACAO_DOLAR);

    public static void main(String[] args) {
        carregarDadosIniciais();

        int opcao;
        do {
            exibirMenu();
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();
            System.out.println();
            switch (opcao) {
                case 1 -> cadastrarCamisa();
                case 2 -> listarCatalogo();
                case 3 -> reporEstoque();
                case 4 -> cadastrarCliente();
                case 5 -> novoPedido();
                case 6 -> listarPedidos();
                case 7 -> finalizarPedido();
                case 8 -> relatorioPrecificacao();
                case 0 -> System.out.println("Encerrando o sistema. Até logo!");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    // MENU

    private static void exibirMenu() {
        System.out.println("\n========== Lojão Camisa Time ==========");
        System.out.println("1 - Cadastrar camisa");
        System.out.println("2 - Listar catálogo e estoque");
        System.out.println("3 - Repor estoque");
        System.out.println("4 - Cadastrar cliente");
        System.out.println("5 - Novo pedido");
        System.out.println("6 - Listar pedidos");
        System.out.println("7 - Finalizar pedido (pagar)");
        System.out.println("8 - Relatório de custo, lucro e margem");
        System.out.println("0 - Sair");
        System.out.println("=============================");
    }

    // CAMISAS E ESTOQUE

    private static void cadastrarCamisa() {
        System.out.println("--- Cadastrar camisa ---");
        System.out.print("Time: ");
        String time = sc.nextLine();
        System.out.print("Temporada (ex.: 2025): ");
        String temporada = sc.nextLine();
        Tamanho tamanho = escolherTamanho();
        Versao versao = escolherVersao();
        System.out.print("Custo em USD: ");
        double custoUsd = sc.nextDouble();
        sc.nextLine();
        System.out.print("Preço de venda em R$: ");
        double preco = sc.nextDouble();
        sc.nextLine();
        System.out.print("Tipo (1 - Atual, 2 - Retrô): ");
        int tipo = sc.nextInt();
        sc.nextLine();

        Camisa camisa;
        if (tipo == 1) {
            camisa = new CamisaAtual(time, temporada, tamanho, versao, custoUsd, preco, fornecedor);
        } else {
            camisa = new CamisaRetro(time, temporada, tamanho, versao, custoUsd, preco, fornecedor);
        }
        catalogo.add(camisa);

        System.out.print("Quantidade inicial em estoque (0 para nenhuma): ");
        int qtd = sc.nextInt();
        sc.nextLine();
        if (qtd > 0) {
            estoque.adicionar(camisa, qtd);
        }
        System.out.println("Camisa cadastrada!");
    }

    private static void listarCatalogo() {
        System.out.println("--- Catálogo ---");
        if (catalogo.isEmpty()) {
            System.out.println("Nenhuma camisa cadastrada.");
            return;
        }
        for (int i = 0; i < catalogo.size(); i++) {
            Camisa c = catalogo.get(i);
            System.out.printf("%d) [%s] %s %s | %s | %s | R$ %.2f | estoque: %d%n",
                    i + 1, c.getClass().getSimpleName(), c.getTime(), c.getTemporada(),
                    c.getTamanho(), c.getVersao(), c.getPrecoVendaReais(), estoque.consultar(c));
        }
    }

    private static void reporEstoque() {
        System.out.println("--- Repor estoque ---");
        Camisa camisa = escolherCamisa();
        if (camisa == null) return;
        System.out.print("Quantidade a adicionar: ");
        int qtd = sc.nextInt();
        sc.nextLine();
        try {
            estoque.adicionar(camisa, qtd);
            System.out.println("Estoque atualizado: " + estoque.consultar(camisa) + " unidades.");
        } catch (ProdutoInvalidoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // CLIENTES

    private static void cadastrarCliente() {
        System.out.println("--- Cadastrar cliente ---");
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Contato: ");
        String contato = sc.nextLine();
        System.out.print("Informar endereço? (1 - Sim, 2 - Não): ");
        int comEndereco = sc.nextInt();
        sc.nextLine();

        if (comEndereco == 1) {
            System.out.print("Rua: ");
            String rua = sc.nextLine();
            System.out.print("Bairro: ");
            String bairro = sc.nextLine();
            System.out.print("Cidade: ");
            String cidade = sc.nextLine();
            System.out.print("Estado: ");
            String estado = sc.nextLine();
            clientes.add(new Cliente(nome, contato, new Endereco(rua, bairro, cidade, estado)));
        } else {
            clientes.add(new Cliente(nome, contato));
        }
        System.out.println("Cliente cadastrado!");
    }

    // PEDIDOS

    private static void novoPedido() {
        System.out.println("--- Novo pedido ---");
        Cliente cliente = escolherCliente();
        if (cliente == null) return;
        Camisa camisa = escolherCamisa();
        if (camisa == null) return;
        System.out.print("Quantidade: ");
        int qtd = sc.nextInt();
        sc.nextLine();

        Pedido pedido = new Pedido(cliente, camisa, qtd);

        System.out.print("Adicionar outra camisa? (1 - Sim, 2 - Não): ");
        int mais = sc.nextInt();
        sc.nextLine();
        while (mais == 1) {
            Camisa outra = escolherCamisa();
            if (outra == null) break;
            System.out.print("Quantidade: ");
            int q = sc.nextInt();
            sc.nextLine();
            pedido.adicionarItem(outra, q);
            System.out.print("Adicionar outra camisa? (1 - Sim, 2 - Não): ");
            mais = sc.nextInt();
            sc.nextLine();
        }

        pedidos.add(pedido);
        System.out.printf("Pedido #%d criado. Subtotal: R$ %.2f%n", pedidos.size(), pedido.getSubtotalReais());
    }

    private static void listarPedidos() {
        System.out.println("--- Pedidos ---");
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido criado.");
            return;
        }
        for (int i = 0; i < pedidos.size(); i++) {
            Pedido p = pedidos.get(i);
            System.out.printf("Pedido #%d | cliente: %s | status: %s%n",
                    i + 1, p.getCliente().getNome(), p.getStatus());
            for (ItemPedido item : p.getItens()) {
                System.out.printf("   %dx %s %s (%s) = R$ %.2f%n",
                        item.getQuantidade(), item.getCamisa().getTime(),
                        item.getCamisa().getTemporada(), item.getCamisa().getTamanho(),
                        item.getSubtotalReais());
            }
            System.out.printf("   Subtotal: R$ %.2f%n", p.getSubtotalReais());
        }
    }

    private static void finalizarPedido() {
        System.out.println("--- Finalizar pedido ---");
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido criado.");
            return;
        }
        listarPedidos();
        System.out.print("Número do pedido: ");
        int indice = sc.nextInt();
        sc.nextLine();
        Pedido pedido = pedidos.get(indice - 1);

        System.out.print("Forma de pagamento (1 - Pix, 2 - Cartão): ");
        int forma = sc.nextInt();
        sc.nextLine();
        Pagamento pagamento;
        if (forma == 1) {
            System.out.print("Chave Pix: ");
            String chave = sc.nextLine();
            pagamento = new PagamentoPix(chave);
        } else {
            System.out.print("Número do cartão (16 dígitos): ");
            String numero = sc.nextLine();
            System.out.print("Parcelas (1 a 12): ");
            int parcelas = sc.nextInt();
            sc.nextLine();
            pagamento = new PagamentoCartao(numero, parcelas);
        }

        double totalPrevisto = pedido.getSubtotalReais() * (1 - pagamento.getTipo().getPercentualDesconto());
        System.out.printf("Total previsto com desconto do %s: R$ %.2f%n", pagamento.getTipo(), totalPrevisto);

        try {
            pedidoService.finalizar(pedido, pagamento);
            System.out.println("Pedido #" + indice + " finalizado com sucesso!");
        } catch (EstoqueInsuficienteException | PagamentoRecusadoException e) {
            System.out.println("Não foi possível finalizar: " + e.getMessage());
        }
        System.out.println("Status do pedido: " + pedido.getStatus());
    }

    // RELATÓRIO

    private static void relatorioPrecificacao() {
        System.out.printf("--- Relatório (cotação R$ %.2f) ---%n", COTACAO_DOLAR);
        if (catalogo.isEmpty()) {
            System.out.println("Nenhuma camisa cadastrada.");
            return;
        }
        for (Camisa c : catalogo) {
            System.out.printf("%s %s (%s) | custo R$ %.2f | com tributos R$ %.2f | venda R$ %.2f | lucro R$ %.2f | margem %.1f%%%n",
                    c.getTime(), c.getTemporada(), c.getTamanho(),
                    precificacao.calcularCustoReais(c),
                    precificacao.calcularCustoComTributos(c),
                    c.getPrecoVendaReais(),
                    precificacao.calcularLucro(c),
                    precificacao.calcularMargem(c) * 100);
        }
    }

    // ESCOLHAS

    private static Camisa escolherCamisa() {
        if (catalogo.isEmpty()) {
            System.out.println("Nenhuma camisa cadastrada.");
            return null;
        }
        listarCatalogo();
        System.out.print("Número da camisa: ");
        int indice = sc.nextInt();
        sc.nextLine();
        return catalogo.get(indice - 1);
    }

    private static Cliente escolherCliente() {
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado. Use a opção 4 primeiro.");
            return null;
        }
        for (int i = 0; i < clientes.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, clientes.get(i).getNome());
        }
        System.out.print("Número do cliente: ");
        int indice = sc.nextInt();
        sc.nextLine();
        return clientes.get(indice - 1);
    }

    private static Tamanho escolherTamanho() {
        Tamanho[] tamanhos = Tamanho.values();
        for (int i = 0; i < tamanhos.length; i++) {
            System.out.printf("%d - %s%n", i + 1, tamanhos[i]);
        }
        System.out.print("Tamanho: ");
        int opcao = sc.nextInt();
        sc.nextLine();
        return tamanhos[opcao - 1];
    }

    private static Versao escolherVersao() {
        Versao[] versoes = Versao.values();
        for (int i = 0; i < versoes.length; i++) {
            System.out.printf("%d - %s%n", i + 1, versoes[i]);
        }
        System.out.print("Versão: ");
        int opcao = sc.nextInt();
        sc.nextLine();
        return versoes[opcao - 1];
    }

    // ===================== DADOS INICIAIS =====================

    private static void carregarDadosIniciais() {
        Camisa flamengo = new CamisaAtual("Flamengo", "2025", Tamanho.M, Versao.TORCEDOR, 25.0, 399.90, fornecedor);
        Camisa palmeiras = new CamisaAtual("Palmeiras", "2025", Tamanho.G, Versao.JOGADOR, 30.0, 599.90, fornecedor);
        Camisa brasil = new CamisaRetro("Brasil", "1970", Tamanho.M, Versao.TORCEDOR, 20.0, 499.90, fornecedor);

        catalogo.add(flamengo);
        catalogo.add(palmeiras);
        catalogo.add(brasil);

        estoque.adicionar(flamengo, 10);
        estoque.adicionar(palmeiras, 5);
        estoque.adicionar(brasil, 4);

        clientes.add(new Cliente("Ana", "81999990000",
                new Endereco("Rua das Flores", "Boa Viagem", "Recife", "PE")));
    }
}