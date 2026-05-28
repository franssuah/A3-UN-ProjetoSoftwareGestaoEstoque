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

            // NOME - Refatorado com travas de segurança e validação de duplicidade
            while (true) {
                // 1- Captura a entrada interrompendo fechamentos acidentais da janela
                String entradaNome = JOptionPane.showInputDialog(
                        "INCLUSÃO DE PRODUTO\n\nNOME: "
                );

                // Trava Nº 1: Prevenção contra NullPointerException. 
                // Caso o usuário clicar em "Cancelar" ou fechar no "X", o método é abortado.
                if (entradaNome == null) {
                    return;
                }

                // Remove espaços vazios acidentais antes e depois da palavra.
                entradaNome = entradaNome.trim();

                // Trava Nº 2: Impede o cadastro de produtos com o nome em branco.
                if (entradaNome.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "NOME INVÁLIDO!\nO nome do produto não pode ficar em branco.",
                            "Erro de Validação",
                            JOptionPane.ERROR_MESSAGE);
                    continue; // Retorna ao início do laço while para pedir novamente.
                }

                // Trava Nº 3: Regra de Negócio - Verificação de Duplicidade.
                boolean existe = false;

                // Varre o vetor apenas até o limite de itens cadastrados (total), garantindo otimização.
                for (int i = 0; i < total; i++) {
                    // equalsIgnoreCase corrige situações onde "Maçã" e "maçã" são tratados como o mesmo item.
                    if (produtos[i].nome.equalsIgnoreCase(entradaNome)) {
                        existe = true;
                        break; // Encerra a busca imediatamente ao encontrar a primeira duplicata.
                    }
                }

                // Feedback responsivo de acordo com o resultado da busca.
                if (existe) {
                    JOptionPane.showMessageDialog(null,
                            "PRODUTO DUPLICADO!\nJá existe um produto cadastrado com este nome.",
                            "Erro de Validação",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    // Passou em todas as validações: o nome é limpo, único e seguro para o sistema.
                    produto.nome = entradaNome;
                    break; // Libera o usuário do ciclo de repetição, avançando para a próxima etapa do cadastro.
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
                            "PREÇO INVÁLIDO!\n"
                            + "Por favor, introduza um valor numérico maior que zero (Ex: 10.50).",
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

            // QUANTIDADE - Refatorado
            while (true) {
                // 1- Captura a entrada do usuário como texto puro.
                String entradaQtd = JOptionPane.showInputDialog("INCLUSÃO DE PRODUTO\n\nQUANTIDADE: ");

                // 2- Delega a conversão e validação para a nossa classe utilitária.
                int qtdValidada = util.ValidadorUtil.parseEValidarQuantidade(entradaQtd);

                // 3- Aplica a Regra de Negócio: -1 significa que o Validador encontrou um erro.
                if (qtdValidada != -1) {
                    produto.quantidade = qtdValidada; // Atualiza com o valor seguro.
                    break; // Libera o usuário do ciclo de repetição.
                } else {
                    // 4- Feedback responsivo e educativo.
                    JOptionPane.showMessageDialog(null,
                            "QUANTIDADE INVÁLIDA!\n"
                            + "Por favor, introduza um número inteiro maior ou igual a zero.",
                            "Erro de Validação",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            // CONFIRMAÇÃO DE INCLUSÃO
            // Substituição do formato de confirmação S/N pelo uso da sub-rotina gráfica traduzida.
            String dadosLayout = "Nome      : " + produto.nome + "\n"
                               + "Preço     : R$ " + String.format("%.2f", produto.preco) + "\n"
                               + "Unidade   : " + produto.unidade + "\n"
                               + "Quantidade: " + produto.quantidade;

            if (mostrarDialogoConfirmacao(dadosLayout, "Confirmar Inclusão de Produto")) {
                produtos[total] = produto;
                total++;
                JOptionPane.showMessageDialog(null, "Produto incluído com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Inclusão cancelada!");
            }
         
            // CONTROLE DE CICLO (INCLUIR)
            // Uso da sub-rotina privada de ciclo para gerenciar a continuidade por botões nativos.
        } while (mostrarDialogoCiclo("Deseja realizar uma nova inclusão de produto?"));
    }

    public void alterar() {
        String novaAlteracao;

        do {
            String nome = JOptionPane.showInputDialog("ALTERAR PRODUTO\n\n DIGITE O NOME DO PRODUTO: ");
            if (nome == null) break;
            
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
                                    "PREÇO INVÁLIDO!\n"
                                    + "Por favor, introduza um valor numérico maior que zero.",
                                    "Erro de Validação",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    produto.unidade = JOptionPane.showInputDialog("NOVA UNIDADE (KG, L, UN...): ");

                    // NOVA QUANTIDADE - Refatorado
                    while (true) {
                        // 1- Captura a entrada do usuário como texto puro.
                        String entradaNovaQtd = JOptionPane.showInputDialog("NOVA QUANTIDADE: ");

                        // 2- Delega a conversão e validação.
                        int qtdValidada = util.ValidadorUtil.parseEValidarQuantidade(entradaNovaQtd);

                        // 3- Avalia o retorno do Validador.
                        if (qtdValidada != -1) {
                            produto.quantidade = qtdValidada;
                            break;
                        } else {
                            // 4- Feedback de erro padrão.
                            JOptionPane.showMessageDialog(null,
                                    "QUANTIDADE INVÁLIDA!\n"
                                    + "Por favor, introduza um número inteiro maior ou igual a zero.",
                                    "Erro de Validação",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    
                    // CONFIRMAÇÃO DE ALTERAÇÃO
                    // Substituído o input manual de String pela validação lógica via caixa nativa.
                    String dadosAlterados = "Confirmar as alterações para o produto '" + produto.nome + "'?\n\n"
                                          + "Novo Preço     : R$ " + String.format("%.2f", produto.preco) + "\n"
                                          + "Nova Unidade   : " + produto.unidade + "\n"
                                          + "Nova Quantidade: " + produto.quantidade;

                    if (mostrarDialogoConfirmacao(dadosAlterados, "Confirmação de Alteração")) {
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
            
            // CONTROLE DE CICLO (ALTERAR)
            // Eliminação definitiva do input de texto (S/N) no fechamento do método alterar.
            } while (mostrarDialogoCiclo("Deseja fazer uma nova alteração?"));
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
            
            // CONTROLE DE CICLO (CONSULTAR)
            // Atualização da saída do fluxo consultivo de dados.
            } while (mostrarDialogoCiclo("Deseja fazer uma nova consulta?"));
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
                        
                        // CONFIRMAÇÃO DE EXCLUSÃO
                        // Diálogo binário nativo substituindo a antiga validação por digitação.
                        String mensagemExclusao = "Deseja realmente remover " + quantidadeExcluir + " unidade(s) do produto '" + produto.nome + "'?";
                        
                        if (mostrarDialogoConfirmacao(mensagemExclusao, "Confirmação de Exclusão")) {
                            if (quantidadeExcluir < produto.quantidade) {
                                produto.quantidade -= quantidadeExcluir;
                                JOptionPane.showMessageDialog(null, "Quantidade removida com sucesso!\nEstoque restante: " + produto.quantidade);
                            } else {
                                for (int j = i; j < total - 1; j++) {
                                    produtos[j] = produtos[j + 1];
                                }
                                total--;
                                JOptionPane.showMessageDialog(null, "Produto removido completely do inventário!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Exclusão cancelada!");
                        }
                    }
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                JOptionPane.showMessageDialog(null, "Produto não encontrado!");
            }
            
            // CONTROLE DE CICLO (EXCLUIR)
            // Fechamento do ciclo do menu de exclusão de estoque.
            } while (mostrarDialogoCiclo("Deseja fazer uma nova exclusão?"));
    }

    // IMPLEMENTAÇÃO DAS SUB-ROTINAS DE SUPORTE
    // Métodos utilitários que gerenciam as chamadas do UIManager e os retornos do ConfirmDialog.
    private boolean mostrarDialogoConfirmacao(String mensagem, String titulo) {
        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");
        int resposta = JOptionPane.showConfirmDialog(null, mensagem, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return resposta == JOptionPane.YES_OPTION;
    }

    private boolean mostrarDialogoCiclo(String mensagem) {
        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");
        int MathResposta = JOptionPane.showConfirmDialog(null, mensagem, "Continuar Operação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return MathResposta == JOptionPane.YES_OPTION;
    }
}