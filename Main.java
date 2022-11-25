package aula_exercicio;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Main {
    static Map<Integer, Integer> carrinhoCompras = new HashMap<>();
    static Path path = Paths.get("./arquivo.txt");
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean continuar=true;
        String opcaoEntrada;

        while(continuar){
            System.out.println("Escolha um das opções abaixo");
            System.out.println("1-Cadastrar produto");
            System.out.println("2-Editar produto");
            System.out.println("3-Pesquisar produto");
            System.out.println("4-Deletar produto");
            System.out.println("5-Comprar produtos");
            if (carrinhoCompras.entrySet().size() > 0) {
                System.out.println("6-Finalizar compra");
            }
            System.out.println("0-Sair");
            opcaoEntrada = input.nextLine();
            switch (opcaoEntrada){
                case "1"-> criarProduto(input);
                case "2"-> editarProduto(input);
                case "3"-> pesquisarProduto( input);
                case "4"-> deletarProduto(input);
                case "5"-> comprarProduto(input);
                case "6"-> finalizarCompras(input);
                case "0"-> continuar = finalizarPrograma();
                default-> System.out.println("Opção inválida");
            }
        }

    }

    public static void criarArquivo() {
        try {
            if (Files.exists(path)) {
                System.out.println("Arquivo ja existe");
            } else {
                path = Files.createFile(path);
                System.out.println("Arquivo criado em: " + path.toString());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<String> pegarProdutos() {
        List<String> produtos = new ArrayList();

        try {
            produtos = Files.readAllLines(path);

        } catch (Exception ex) {
            System.out.println("Error" + ex.getMessage());
        }
        return produtos;
    }

    public static List<String> listarProdutos() {
        List<String> produtos = pegarProdutos();

        try {
            System.out.println("-=".repeat(5)+"LISTA DE PRODUTOS"+"-=".repeat(5));
            for (int i = 0; i < produtos.size(); i++) {
                String[] produto = produtos.get(i).split("\\|");

                System.out.printf("Id: %d; Nome: %s; Qtde de estoque: %s; Preço: %s;\n", i, produto[0], produto[1],
                        produto[2]);
            }

        } catch (Exception ex) {
            System.out.println("Error" + ex.getMessage());
        }

        return produtos;
    }

    public static void criarProduto(Scanner input){
        // Método de criação do produto

        criarArquivo();

        boolean isInvalid;

        // Do while para inserção dos dados do produto

        do {
            isInvalid = false;
            try {
                System.out.println("Digite: o nome do seu produto");
                String nome = input.nextLine();

                System.out.println("Digite: a quantidade de estoque do produto");
                Integer quantidade = input.nextInt();

                System.out.println("Digite: o preço do seu produto");
                Double preco = input.nextDouble();
                input.nextLine();

                Files.writeString(path, nome + "|" + quantidade + "|" + preco + "\n", StandardOpenOption.APPEND);
                System.out.print("Produto cadastrado com sucesso\n");

            } catch (InputMismatchException ex) {
                System.out.print("Parâmetros incorretos\n");
                isInvalid = true;
                input.nextLine();

            } catch (Exception ex) {
                System.out.println("Error:" + ex.getMessage());
                isInvalid = true;
            }
        } while (isInvalid);


    }
    public static void editarProduto(Scanner input){
        List<String> listaProdutosOriginal = listarProdutos();

        boolean isInvalid;

        do {
            isInvalid = false;
            try {
                System.out.println("Digite: o id do produto a se editar");

                Integer idProduto = input.nextInt();
                input.nextLine();

                if (idProduto >= 0 && idProduto < listaProdutosOriginal.size()) {
                    System.out.println("Digite: o nome do seu produto");
                    String nome = input.nextLine();

                    System.out.println("Digite: a quantidade de estoque do produto");
                    Integer quantidade = input.nextInt();

                    System.out.println("Digite: o preço do seu produto");
                    Double preco = input.nextDouble();
                    input.nextLine();

                    listaProdutosOriginal.set(idProduto, nome + "|" + quantidade + "|" + preco);

                    Files.write(path, listaProdutosOriginal);
                } else {
                    System.out.print("[ERRO] ID inválido!\n\n");
                    isInvalid = true;
                }

            } catch (InputMismatchException ex) {
                System.out.print("Parâmetros incorretos\n");
                isInvalid = true;
                input.nextLine();

            } catch (Exception ex) {
                System.out.println("Error:" + ex.getMessage());
                isInvalid = true;
            }
        } while (isInvalid);

    }
    public static void pesquisarProduto(Scanner input){
        List<String> listaProdutos = pegarProdutos();

        try {
            System.out.println("Digite: o nome do produto a ser pesquisado");
            String produtoPesquisado = input.nextLine().toLowerCase();

            Integer id = 0;
            Integer produtosEncontrados = 0;

            for (String line : listaProdutos) {
                String[] produto = line.split("\\|");
                String nomeProduto = produto[0].toLowerCase();

                if (nomeProduto.contains(produtoPesquisado)) {
                    System.out.printf("Id: %d; Nome: %s; Qtde de estoque: %s; Preço: %s;\n", id, produto[0], produto[1],
                            produto[2]);
                    produtosEncontrados++;
                }
                id++;
            }

            System.out.print("\n");

            if(produtosEncontrados==0){
                System.out.printf("Esse produto '%s' não existe.\n", produtoPesquisado);
            }

        } catch (Exception ex) {
            System.out.println("Error" + ex.getMessage());
        }
    }
    public static void deletarProduto(Scanner input){
        List<String> listaProdutosOriginal = listarProdutos();

        boolean isInvalid;

        do {
            isInvalid = false;

            try {
                System.out.println("Digite: o id do produto a se deletar");

                Integer idProduto = input.nextInt();
                input.nextLine();

                if (idProduto >= 0 && idProduto < listaProdutosOriginal.size()) {

                    boolean rodando = true;
                    while (rodando) {

                        System.out.println("Deseja realmente deletar este produto?");
                        System.out.println("1 - Sim");
                        System.out.println("2 - Não");
                        System.out.print("Digite uma opção: ");
                        String opcao = input.nextLine();

                        switch (opcao) {
                            case "1" -> {
                                listaProdutosOriginal.remove(idProduto.intValue());
                                Files.write(path, listaProdutosOriginal);

                                rodando = false;
                                System.out.print("Produto deletado com sucesso\n\n");
                            }
                            case "2" -> {
                                rodando = false;
                                System.out.print("Voltando...\n\n");
                            }
                            default -> System.out.println("Opção inválida");
                        }

                    }

                } else {
                    System.out.println("[ERRO] ID inválido!");
                }


            } catch (InputMismatchException ex) {
                System.out.print("Parâmetros incorretos\n");
                isInvalid = true;
                input.nextLine();

            } catch (Exception ex) {
                System.out.println("Error:" + ex.getMessage());
                isInvalid = true;
            }

        } while (isInvalid);
    }
    public static void comprarProduto(Scanner input)  {
        try {
            List<String> listaProdutosOriginal = listarProdutos();
            Integer id = 0;

            boolean adicionando = true;

            while (adicionando) {
                System.out.println("Qual o id do produto que você deseja comprar?");

                int idProduto = input.nextInt();
                input.nextLine();

                if (idProduto >= 0 && idProduto < listaProdutosOriginal.size()) {

                    int produtosDisponiveis = Integer.parseInt(listaProdutosOriginal.get(idProduto).split("\\|")[1]);

                    System.out.println("Quantos itens desse produto você deseja comprar?");
                    Integer qtdeProduto = input.nextInt();
                    input.nextLine();

                    Integer somaQtdeProduto = qtdeProduto;

                    if (carrinhoCompras.get(idProduto) != null) {
                        somaQtdeProduto = carrinhoCompras.get(idProduto) + somaQtdeProduto;
                    }

                    if (somaQtdeProduto <= produtosDisponiveis) {
                        carrinhoCompras.put(idProduto, somaQtdeProduto);
                    } else {
                        System.out.printf("No momento existem apenas %d produtos disponíveis no estoque\n", produtosDisponiveis);
                    }
                    System.out.println("");
                    System.out.println("-=".repeat(5) + "CARRINHO DE COMPRAS" + "-=".repeat(5));
                    System.out.println("Produto | Preço | Qtde | Valor Total");
                    for (Map.Entry<Integer, Integer> entry : carrinhoCompras.entrySet()) {
                        String[] produto = listaProdutosOriginal.get(entry.getKey()).split("\\|");
                        String nomeProduto = produto[0];
                        Double precoProduto = Double.valueOf(produto[2]);
                        Integer qtdeCarrinho = entry.getValue();
                        Double valorTotal = qtdeCarrinho * precoProduto;

                        System.out.printf("%s | %.2f | %d | %.2f\n", nomeProduto, precoProduto, qtdeCarrinho, valorTotal);

                    }

                    System.out.println("-=".repeat(5) + "-=-=-=-=-=-=-=-=-=-=-=" + "-=".repeat(5));
                    System.out.println("");

                    System.out.println("Deseja Adicionar mais um produto?");
                    System.out.println("1 - Sim");
                    System.out.println("2 - Não");
                    System.out.print("Digite uma opção: ");
                    String adicionar = input.nextLine();
                    System.out.print("\n");

                    switch (adicionar) {
                        case "1" -> {
                            continue;
                        }
                        case "2" -> adicionando = false;

                        default -> System.out.println("Opção inválida");
                    }


                    boolean finalizando = true;
                    while (finalizando) {

                        System.out.println("Deseja finalizar sua compra agora ?");
                        System.out.println("1 - Sim");
                        System.out.println("2 - Finalizar mais tarde");
                        System.out.print("Digite uma opção: ");
                        String finalizar = input.nextLine();
                        System.out.print("\n");

                        switch (finalizar) {
                            case "1" -> {
                                finalizarCompras(input);
                                finalizando = false;
                            }
                            case "2" -> {
                                finalizando = false;
                                System.out.print("Voltando...\n\n");
                            }
                            default -> System.out.println("Opção inválida");
                        }
                    }

                } else {
                    System.out.print("[ERRO] ID inválido!\n\n");
                }
            }


        } catch (Exception ex) {
            System.out.println("Error" + ex.getMessage());
        }
    }

    public static void finalizarCompras(Scanner input)  {
        try{
            List<String> listaProdutosOriginal = Files.readAllLines(path);
            Integer id = 0;


            System.out.println("-=".repeat(5) +"CARRINHO DE COMPRAS FECHADO" + "-=".repeat(5));
            System.out.println("Produto | Preço | Qtde | Valor Total");
            for (Map.Entry<Integer, Integer> entry : carrinhoCompras.entrySet()) {
                String[] produto = listaProdutosOriginal.get(entry.getKey()).split("\\|");
                String nomeProduto = produto[0];
                Double precoProduto = Double.valueOf(produto[2]);
                Integer qtdeCarrinho = entry.getValue();
                Integer qtdeProdutoEstoque = Integer.valueOf(listaProdutosOriginal.get(entry.getKey()).split("\\|")[1]);
                Double valorTotal = qtdeCarrinho* precoProduto;

                listaProdutosOriginal.set(entry.getKey(), nomeProduto+"|"+(qtdeProdutoEstoque-qtdeCarrinho)+
                        "|"+precoProduto);

                System.out.printf("%s | %.2f | %d | %.2f\n", nomeProduto, precoProduto, qtdeCarrinho, valorTotal);

            }

            System.out.println("-=".repeat(5) +"-=-=-=-=-=-=-=-=-=-=-=" + "-=".repeat(5));

            Files.write(path, listaProdutosOriginal);
            
            System.out.print("Compra Realizada com sucesso\n\n");
            carrinhoCompras = new HashMap<>();

        } catch (Exception ex){
            System.out.println("Error" + ex.getMessage());
        }

    }

    public static boolean finalizarPrograma(){
        System.out.println("-=".repeat(5)+"PROGRAMA FINALIZADO"+"-=".repeat(5));

        return false;
    }

}
