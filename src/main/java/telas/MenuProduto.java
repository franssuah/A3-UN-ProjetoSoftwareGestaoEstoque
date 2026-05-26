package telas;

import javax.swing.*;

import modelo.Produto;

public class MenuProduto {

    public Produto[] produtos = new Produto[100];
    public int total = 0;

    public void menu() {
        String opcao;
// Menu inicial
        do {
            opcao = JOptionPane.showInputDialog(
                    "Menu de Produtos\n"
                    + "1 - Cadastrar Produto\n"
                    + "2 - Alterar Produtos\n"
                    + "3 - Consultar Produtos\n"
                    + "4 - Excluir Produtos\n"
                    + "5 - Retornar\n"
                    + "Opção: "
            );

            switch (opcao) {
                case "1":
                    incluir();
                    break;
                case "2":
                    alterar();
                    break;
                case "3":
                    consultar();
                    break;
                case "4":
                    excluir();
                    break;
                case "5":
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        } while (true);
    }

    public void incluir() {

        if (total == 100) {
            JOptionPane.showMessageDialog(null, "Limite de produtos atingido!");
            return;
        }

        String novaInclusao;

        do {
            Produto produto = new Produto();

            // NOME
            while (true) {
                produto.nome = JOptionPane.showInputDialog(
                        "INCLUSÃO DE PRODUTO\n\nNOME: "
                );
                produto.nome = produto.nome.trim();

                boolean existe = false;

                for (int i = 0; i < total; i++) {
                    if (produtos[i].nome.equalsIgnoreCase(produto.nome)) {
                        existe = true;
                        break;
                    }
                }

                if (existe) {
                    JOptionPane.showMessageDialog(null, "Já existe um produto com este nome!");
                } else {
                    break;
                }
            }

            // PREÇO - Refatorado
            while (true) {
            // 1- Captura a entrada do usuário como texto puro sem tentar converter de imediato.
            String entradaPreco = JOptionPane.showInputDialog("INCLUSÃO DE PRODUTO\n\nPREÇO: ");

            // 2- Delega a conversão e validação (abrange também contra letras, nulos e valores vazios).
            double precoValidado = util.ValidadorUtil.parseEValidarPreco(entradaPreco);

            // 3- Aplica a Regra de Negócio: -1.0 significa que o Validador interceptou um erro.
                if (precoValidado != -1.0) {
                produto.preco = precoValidado; // Atualiza o preço com um valor limpo e seguro.
                    break; // Libera o usuário do ciclo de repetição.
            } else {
            // 4- Feedback responsivo informando como o usuário deve proceder de forma correta.
            JOptionPane.showMessageDialog(null, 
            "PREÇO INVÁLIDO!\n" +
            "Por favor, introduza um valor numérico maior que zero (Ex: 10.50).", 
            "Erro de Validação", 
            JOptionPane.ERROR_MESSAGE);
        }
    }

            // UNIDADE
            produto.unidade = JOptionPane.showInputDialog("UNIDADE (KG, L, UN...): ");
            produto.unidade = produto.unidade.trim().toUpperCase();

            while (!produto.unidade.matches("[A-Z]+")) {
                JOptionPane.showMessageDialog(null, "Digite apenas letras!(KG, L, UN...)");

                produto.unidade = JOptionPane.showInputDialog("UNIDADE (KG, L, UN...): ");
                produto.unidade = produto.unidade.trim().toUpperCase();
            }

            // QUANTIDADE
            while (true) {
                produto.quantidade = Integer.parseInt(
                        JOptionPane.showInputDialog("QUANTIDADE: ")
                );

                if (produto.quantidade >= 0) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Quantidade deve ser maior ou igual a zero!");
                }
            }

            // CONFIRMACAO
            String confirma = JOptionPane.showInputDialog("CONFIRMA INCLUSÃO? (S/N)");

            if (confirma.equalsIgnoreCase("S")) {
                produtos[total] = produto;
                total++;

                JOptionPane.showMessageDialog(null, "Produto Incluido com sucesso!");
            }

            novaInclusao = JOptionPane.showInputDialog("NOVO INCLUSÃO DE PRODUTO? (S/N)");
        } while (novaInclusao.equals("S"));
    }

    public void alterar() {
        String novaAlteracao;

        do {
            String nome = JOptionPane.showInputDialog("ALTERAR PRODUTO\n\n DIGITE O NOME DO PRODUTO: ");

            boolean existe = false;

            for (int i = 0; i < total; i++) {
                if (produtos[i].nome.equalsIgnoreCase(nome)) {
                    Produto produto = produtos[i];

                    JOptionPane.showMessageDialog(null,
                            "PRODUTO ENCONTRADO\n\n"
                            + "Produto   : " + produto.nome + "\n"
                            + "Preço     : R$ " + String.format("%.2f", produto.preco) + "\n"
                            + "Unidade   : " + produto.unidade + "\n"
                            + "Quantidade: " + formatarUnidade(produto.unidade, produto.quantidade) + "\n"
                    );

                    // NOVO PREÇO - Refatorado
                    while (true) {
                        
                        // 1- Captura a entrada do usuário como texto puro sem tentar converter de imediato.
                        String entradaNovoPreco = JOptionPane.showInputDialog("NOVO PREÇO: ");
                            
                        // 2- Delega a conversão e validação (abrange também contra letras, nulos e valores vazios).
                        double precoValidado = util.ValidadorUtil.parseEValidarPreco(entradaNovoPreco);
                        
                        // 3- Aplica a Regra de Negócio: -1.0 significa que o Validador interceptou um erro.
                        if (precoValidado != -1.0) {
                            produto.preco = precoValidado; // Atualiza o preço com um valor limpo e seguro.
                            break; // Libera o usuário do ciclo de repetição.
                        } else {
                        
                            // 4- Feedback responsivo informando como o usuário deve proceder de forma correta.
                            JOptionPane.showMessageDialog(null,
                                "PREÇO INVÁLIDO!\n" +
                                "Por favor, introduza um valor numérico maior que zero.",
                                "Erro de Validação",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    produto.unidade = JOptionPane.showInputDialog("NOVA UNIDADE (KG, L, UN...): ");

                    while (true) {
                        int novaQtd = Integer.parseInt(
                                JOptionPane.showInputDialog("NOVA QUANTIDADE: ")
                        );
                        if (novaQtd >= 0) {
                            produto.quantidade = novaQtd;
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Quantidade deve ser maior ou igual a zero!");
                        }
                    }

                    String confirma = JOptionPane.showInputDialog("CONFIRMA ALTERAÇÃO? (S/N)");
                    if (confirma.equalsIgnoreCase("S")) {
                        JOptionPane.showMessageDialog(null, "Produto alterado com sucesso!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Alteração cancelada!");
                    }

                    existe = true;
                    break;
                }
            }
            if (!existe) {
                JOptionPane.showMessageDialog(null,
                        "Produto não encontrado!");
            }
            novaAlteracao = JOptionPane.showInputDialog("DESEJA FAZER UMA NOVA ALTERAÇÃO? (S/N)");
        } while (novaAlteracao.equalsIgnoreCase("S"));
    }

    public void consultar() {
        String novaConsulta;
        do {
            boolean existe = false;
            String nome = JOptionPane.showInputDialog(
                    "CONSULTAR PRODUTO\n\nDIGITE O NOME DO PRODUTO: "
            );

            if (nome == null) {
                return;
            }

            for (int i = 0; i < total; i++) {
                if (produtos[i].nome.equalsIgnoreCase(nome.trim())) {

                    String unidadeFormatada = formatarUnidade(
                            produtos[i].unidade,
                            produtos[i].quantidade
                    );
                    // layout da consulta
                    JOptionPane.showMessageDialog(null,
                            "======= DADOS DO PRODUTO =======\n\n"
                            + "Produto   : " + produtos[i].nome + "\n"
                            + "Preco     : R$ " + String.format("%.2f", produtos[i].preco) + "\n"
                            + "Unidade   : " + produtos[i].unidade + "\n"
                            + "Quantidade: " + unidadeFormatada + "\n"
                            + "================================"
                    );
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                JOptionPane.showMessageDialog(null, "Produto nao encontrado!");
            }

            novaConsulta = JOptionPane.showInputDialog(
                    "DESEJA FAZER UMA NOVA CONSULTA? (S/N)"
            );

        } while (novaConsulta != null && novaConsulta.equalsIgnoreCase("S"));
    }
//  Formata a quantidade de acordo com a unidade

    private String formatarUnidade(String unidade, int quantidade) {
        switch (unidade.toUpperCase()) {
            case "KG":
                return quantidade + " KG (Quilogramas)";
            case "G":
                return quantidade + " G (Gramas)";
            case "L":
                return quantidade + " L (Litros)";
            case "ML":
                return quantidade + " ML (Mililitros)";
            case "UN":
                return quantidade + " UN (Unidades)";
            case "CX":
                return quantidade + " CX (Caixas)";
            case "PC":
                return quantidade + " PC (Pacotes)";
            default:
                return quantidade + " " + unidade;
        }
    }

    public void excluir() {

    String novaExclusao;

    do {

        String nome = JOptionPane.showInputDialog(
                "EXCLUSÃO DE PRODUTO\n\nDIGITE O NOME DO PRODUTO:"
        );

        if (nome == null) {
            break;
        }

        boolean existe = false;

        for (int i = 0; i < total; i++) {

            if (produtos[i].nome.equalsIgnoreCase(nome)) {

                Produto produto = produtos[i];

                JOptionPane.showMessageDialog(null,
                        "PRODUTO ENCONTRADO\n\n"
                        + "Produto: " + produto.nome + "\n"
                        + "Preço: R$ " + String.format("%.2f", produto.preco) + "\n"
                        + "Unidade: " + produto.unidade + "\n"
                        + "Quantidade em estoque: " + produto.quantidade
                );

                String quantidadeStr = JOptionPane.showInputDialog(
                        "Quantos itens deseja excluir?"
                );

                if (quantidadeStr == null) {
                    break;
                }

                int quantidadeExcluir = Integer.parseInt(quantidadeStr);

                if (quantidadeExcluir <= 0) {

                    JOptionPane.showMessageDialog(null,
                            "Digite uma quantidade válida!");

                } else if (quantidadeExcluir > produto.quantidade) {

                    JOptionPane.showMessageDialog(null,
                            "Quantidade maior que o estoque!");

                } else {

                    String confirma = JOptionPane.showInputDialog(
                            "CONFIRMA EXCLUSÃO DE "
                            + quantidadeExcluir
                            + " UNIDADE(S)? (S/N)"
                    );

                    if (confirma != null && confirma.equalsIgnoreCase("S")) {

                        // REMOVE APENAS UMA PARTE
                        if (quantidadeExcluir < produto.quantidade) {

                            produto.quantidade -= quantidadeExcluir;

                            JOptionPane.showMessageDialog(null,
                                    "Quantidade removida com sucesso!\n\n"
                                    + "Estoque restante: "
                                    + produto.quantidade
                            );

                        }

                        // REMOVE O PRODUTO INTEIRO
                        else {

                            for (int j = i; j < total - 1; j++) {
                                produtos[j] = produtos[j + 1];
                            }

                            total--;

                            JOptionPane.showMessageDialog(null,
                                    "Produto removido completamente!");
                        }
                    }
                }

                existe = true;
                break;
            }
        }

        if (!existe) {

            JOptionPane.showMessageDialog(null,
                    "Produto não encontrado!");

        }

        novaExclusao = JOptionPane.showInputDialog(
                "DESEJA FAZER UMA NOVA EXCLUSÃO? (S/N)"
        );

        if (novaExclusao == null) {
            break;
        }

    } while (novaExclusao.equalsIgnoreCase("S"));
}}