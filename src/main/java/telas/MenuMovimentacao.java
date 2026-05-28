package telas;

import javax.swing.JOptionPane;

import modelo.Produto;

public class MenuMovimentacao {

    public Produto[] produtos;
    public int total;

    public void menu() {

        String opcao;

        do {

            opcao = JOptionPane.showInputDialog(
                    "MOVIMENTAÇÃO DE ESTOQUE\n\n"
                    + "1 - Entrada\n"
                    + "2 - Saída\n"
                    + "0 - Retornar\n\n"
                    + "Opção:"
            );

            if (opcao == null) {
                return;
            }

            switch (opcao) {

                case "1":
                    entrada();
                    break;

               case "2":
                    saida();
                    break;

                case "0":
                    return;

                default:
                    JOptionPane.showMessageDialog(null,
                            "Opção inválida!");
            }

        } while (true);
    }

    public void entrada() {

        /*
  * Método responsável pela entrada
  * de produtos no estoque.
         */
        if (total == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum produto cadastrado!\n"
                    + "Cadastre produtos antes de registrar entradas.");
            return;
        }

        String novaEntrada;

        do {

            String nome = JOptionPane.showInputDialog(
                    "ENTRADA DE PRODUTO\n\n"
                    + "DIGITE O NOME DO PRODUTO:"
            );

            if (nome == null) {
                break;
            }

            boolean existe = false;

            // PROCURA O PRODUTO NO VETOR
            for (int i = 0; i < total; i++) {

                if (produtos[i].nome.equalsIgnoreCase(nome.trim())) {

                    Produto produto = produtos[i];

                    JOptionPane.showMessageDialog(null,
                            "PRODUTO ENCONTRADO\n\n"
                            + "Produto: " + produto.nome + "\n"
                            + "Preço: R$ " + String.format("%.2f", produto.preco) + "\n"
                            + "Unidade: " + produto.unidade + "\n"
                            + "Quantidade Atual: " + produto.quantidade
                    );

                    int quantidadeEntrada = 0;

                    // DESIGN PATTERN (FLUXO SEGURO): Consumo do utilitário centralizado de segurança
                    while (true) {
                        String entradaQtd = JOptionPane.showInputDialog("QUANTIDADE DE ENTRADA:");

                        if (entradaQtd == null) {
                            JOptionPane.showMessageDialog(null, "Operação abortada!");
                            return;
                        }

                        int qtdValidada = util.ValidadorUtil.parseEValidarQuantidade(entradaQtd);

                        if (qtdValidada == -1) {
                            JOptionPane.showMessageDialog(null,
                                    "QUANTIDADE INVÁLIDA!\n"
                                    + "Por favor, introduza um número inteiro maior que zero.",
                                    "Erro de Validação",
                                    JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        
                        if (qtdValidada == 0) {
                            JOptionPane.showMessageDialog(null,
                                    "A quantidade de entrada deve ser maior que zero!",
                                    "Erro de Validação",
                                    JOptionPane.WARNING_MESSAGE);
                            continue;
                        }

                        quantidadeEntrada = qtdValidada;
                        break;
                    }

                    // MELHORIA NA INTERFACE 1.0: Confirmação da Transação de Entrada via Botões Nativos.
                    
                    // Função Preditiva: Forçar os botões exibirem "Sim" e "Não" em vez de "Yes" e "No".
                    /** Dependendo da configuração do sistema operacional do usuário há o risco de que a
                    resposta do JOptionPane.showConfirmDialog adote seu padrão nativo (JVM) e mostre os
                    botões em inglês ("Yes" e "No") em vez de pt-br ("Sim" e "Não"), quebrando o padrão
                    da interface feita inteiramente em pt-br.
                    */
                    javax.swing.UIManager.put("OptionPane.yesButtonText", "Sim");
                    javax.swing.UIManager.put("OptionPane.noButtonText", "Não");

                    // CÁLCULO E PRÉVIA DA QUANTIDADE FINAL.
                    int quantidadeFinalCalculada = produto.quantidade + quantidadeEntrada;

                    String mensagemConfirmacao = "CONFIRMA A ENTRADA DE MATERIAL?\n\n"
                            + "Produto              : " + produto.nome + "\n"
                            + "Preço                : R$ " + String.format("%.2f", produto.preco) + "\n"
                            + "Unidade              : " + produto.unidade + "\n"
                            + "Quantidade a adicionar: " + quantidadeEntrada + "\n"
                            + "Estoque Atual        : " + produto.quantidade + "\n"
                            + "🟢 QTDE FINAL (Saldo): " + quantidadeFinalCalculada + "\n\n"
                            + "Deseja efetivar a transação no sistema?";

                    // Interface de diálogo com botões em pt-br.
                    int respostaBotao = JOptionPane.showConfirmDialog(
                            null, 
                            mensagemConfirmacao, 
                            "Confirmação de Entrada", 
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    // Validação binária da resposta do operador.
                    if (respostaBotao == JOptionPane.YES_OPTION) {
                        produto.quantidade = quantidadeFinalCalculada;
                        JOptionPane.showMessageDialog(null,
                                "ENTRADA REALIZADA COM SUCESSO!\n\n"
                                + "Produto    : " + produto.nome + "\n"
                                + "Novo Estoque: " + produto.quantidade
                        );
                    } else {
                        JOptionPane.showMessageDialog(null, "ENTRADA CANCELADA!");
                    }

                    existe = true;
                    break;
                }
            }

            if (!existe) {

                JOptionPane.showMessageDialog(null,
                        "Produto não encontrado!");
            }

            // MELHORIA NA INTERFACE 1.1: Controle de Ciclo de Entrada Dedicado.
            int respostaRepetir = JOptionPane.showConfirmDialog(
                    null,
                    "Deseja realizar uma nova entrada de produto?",
                    "Continuar Operação",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (respostaRepetir != JOptionPane.YES_OPTION) {
                break;
            }

        } while (true);
    }
    
    public void saida() {
        // Sub-rotina de Segurança: Responsável em verificar se há produtos cadastrados no sistema antes mesmo de operar.
        if (total == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum produto cadastrado!\n"
                    + "Cadastre produtos antes de registrar saídas.");
            return;
        }   
        
        String novaSaida;

        do {
            String nome = JOptionPane.showInputDialog(
                    "SAÍDA DE PRODUTO\n\n"
                    + "DIGITE O NOME DO PRODUTO:"
            );

            // Proteção extra contra fechamento abrupto ou clique em Cancelar.
            if (nome == null) {
                break;
            }

            boolean existe = false;

            // Condicional de varredura para localização do item no vetor em memória.
            for (int i = 0; i < total; i++) {

                if (produtos[i].nome.equalsIgnoreCase(nome.trim())) {
                    Produto produto = produtos[i];

                    // Exibição dos metadados atuais do produto encontrado.
                    JOptionPane.showMessageDialog(null,
                            "PRODUTO ENCONTRADO\n\n"
                            + "Produto: " + produto.nome + "\n"
                            + "Preço: R$ " + String.format("%.2f", produto.preco) + "\n"
                            + "Unidade: " + produto.unidade + "\n"
                            + "Quantidade Atual: " + produto.quantidade
                    );
                    
                    int quantidadeSaida = 0;

                    // LAÇO DE VALIDAÇÃO COM CICLO DE CORRETIVO FOCADO EM UX
                    /** O usuário fica bloqueado neste ciclo até que forneça uma entrada considerada 
                    válida e matematicamente funcional de acordo com o saldo disponível em estoque. 
                    */
                    while (true) {
                        String entradaQtd = JOptionPane.showInputDialog("QUANTIDADE DE SAÍDA:");

                        // Alternativa de Escape: Aborta a operação caso o usuário clique em Cancelar ou feche no "X".
                        if (entradaQtd == null) {
                            JOptionPane.showMessageDialog(null, "Operação abortada!");
                            return; // Encerra a execução do fluxo de movimentação.
                        }

                        // Chamada da infraestrutura lógica de segurança para conversão e tratamento de erros.
                        int qtdValidada = util.ValidadorUtil.parseEValidarQuantidade(entradaQtd);

                        if (qtdValidada == -1) {
                            // Interrupção de entradas não numéricas, vazias ou negativas.
                            JOptionPane.showMessageDialog(null,
                                    "QUANTIDADE INVÁLIDA!\n"
                                    + "Por favor, introduza um número inteiro maior que zero.",
                                    "Erro de Validação",
                                    JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        
                        if (qtdValidada == 0) {
                            JOptionPane.showMessageDialog(null,
                                    "A quantidade de saída deve ser maior que zero!",
                                    "Erro de Validação",
                                    JOptionPane.WARNING_MESSAGE);
                            continue;
                        }

                        // TRAVA: Proteção contra inconsistência física e estoque negativo.
                        if (qtdValidada > produto.quantidade) {
                            JOptionPane.showMessageDialog(null,
                                    "ESTOQUE INSUFICIENTE!\n\n"
                                    + "Quantidade disponível: " + produto.quantidade + "\n"
                                    + "Quantidade solicitada: " + qtdValidada + "\n\n"
                                    + "Por favor, reinsira um valor compatível.",
                                    "Erro de Estoque",
                                    JOptionPane.WARNING_MESSAGE);
                            // Permanece no laço while para correção sem precisar perder a pesquisa do produto.
                        } else {
                            // Passou em todas as validações: atualiza a variável local e aprova o fluxo.
                            quantidadeSaida = qtdValidada;
                            break;
                        }
                    }

                    // MELHORIA NA INTERFACE 1.2: Confirmação da Transação de Saída via Botões Nativos.
                    javax.swing.UIManager.put("OptionPane.yesButtonText", "Sim");
                    javax.swing.UIManager.put("OptionPane.noButtonText", "Não");

                    // CÁLCULO E PRÉVIA DA QUANTIDADE FINAL
                    int quantidadeFinalCalculada = produto.quantidade - quantidadeSaida;

                    String mensagemConfirmacao = "CONFIRMA A RETIRADA DE MATERIAL?\n\n"
                            + "Produto             : " + produto.nome + "\n"
                            + "Quantidade retirada : " + quantidadeSaida + "\n"
                            + "Estoque Atual       : " + produto.quantidade + "\n"
                            + "🔴 QTDE FINAL (Saldo): " + quantidadeFinalCalculada + "\n\n"
                            + "Deseja efetivar a transação no sistema?";

                    // Interface de diálogo com botões em pt-br.
                    int respostaBotao = JOptionPane.showConfirmDialog(
                            null, 
                            mensagemConfirmacao, 
                            "Confirmação de Saída", 
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    // Validação binária da resposta do operador.
                    if (respostaBotao == JOptionPane.YES_OPTION) {
                        produto.quantidade = quantidadeFinalCalculada;
                        JOptionPane.showMessageDialog(null,
                                "SAÍDA REALIZADA COM SUCESSO!\n\n"
                                + "Novo estoque: " + produto.quantidade
                        );
                    } else {
                        JOptionPane.showMessageDialog(null, "SAÍDA CANCELADA!");
                    }
            
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                JOptionPane.showMessageDialog(null, "Produto não encontrado!");
            }

            // MELHORIA NA INTERFACE 1.3: Controle de Ciclo de Saída Dedicado.
            int respostaRepetir = JOptionPane.showConfirmDialog(
                    null,
                    "Deseja realizar uma nova saída de produto?",
                    "Continuar Operação",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (respostaRepetir != JOptionPane.YES_OPTION) {
                break;
            }

        } while (true);
    }
}