package telas;

import modelo.Produto;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.Component;

/**
Classe responsável por gerenciar todo o processo de reajustes de preço de produtos do estoque.
Ela implementa uma interface para três modos de reajustes: reajustes individuais (apenas um produto), reajustes
em lote (para mais de um produto), ou reajustes a 100% do estoque. O processo de reajuste é dinâmico e oferece
atalhos em botões com valores pré-definidos ou permite ao usuário digitar outros valores. Ademais, a classe inclui
validações preventivas para garantir que os reajustes a serem aplicados não fiquem negativos ou zerados, atendendo
às regras de negócio.
@author Franssuah (Responsável Final - Versão Fork Individual)
*/
public class MenuReajustePreco {

    private final Produto[] produtos;         
    private final int total;                  
    
    /**
     * Construtor da classe MenuReajustePreco.
     * * @param produtos Vetor contendo a referência dos produtos cadastrados no sistema.
     * @param total    Quantidade atual de produtos ativos no inventário.
     */
    public MenuReajustePreco(Produto[] produtos, int total) {
        this.produtos = produtos;       
        this.total = total;             
    }

    /**
     * Menu principal de reajuste de preços. Gerencia o fluxo de navegação entre
     * reajuste coletivo, único ou total por meio de caixas de diálogo.
     */
    public void menu() {
        if (nenhumProdutoCadastrado()) {
            return;                     
        }

        String opcao;                   

        do {
            opcao = JOptionPane.showInputDialog(null,
                    "SISTEMA DE CONTROLE DE ESTOQUE\n\n"
                    + "MENU REAJUSTE DE PREÇOS\n\n"
                    + "1 - Reajuste Coletivo (Vários produtos selecionados)\n"
                    + "2 - Reajuste Único (Apenas um produto específico)\n"
                    + "3 - Reajuste Total (Todos os produtos do estoque)\n"
                    + "0 - Retornar\n\n"
                    + "Opção: ",
                    "Tela 1.3 - Reajuste de Preços",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcao == null || opcao.trim().equals("0")) {
                return; // Proteção contra NullPointerException ao fechar ou cancelar.
            }

            switch (opcao.trim()) {
                case "1" -> reajustarColetivo();
                case "2" -> reajustarUnico();
                case "3" -> reajustarTotal();
                default -> JOptionPane.showMessageDialog(null, "Opção inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } while (true);
    }

    /**
     * Realiza o reajuste coletivo selecionando múltiplos produtos específicos 
     * através de busca textual exata e aplicando uma taxa percentual comum.
     */
    private void reajustarColetivo() {
        // Vetor de booleanos para marcar quais índices do vetor principal serão reajustados.
        boolean[] marcados = new boolean[total];
        int qtdSelecionada = 0;

        do {
            String nomeBusca = JOptionPane.showInputDialog(null,
                    "REAJUSTE COLETIVO\n"
                    + "Produtos selecionados atual: " + qtdSelecionada + "\n\n"
                    + "Digite o nome EXATO do produto que deseja incluir no lote:",
                    "Seleção de Itens em Lote", JOptionPane.QUESTION_MESSAGE);

            if (nomeBusca == null) {
                if (qtdSelecionada == 0) return; // Se não escolheu nenhum e cancelou, aborta.
                break; // Se já tem itens, sai da seleção e vai para a aplicação do reajuste.
            }

            nomeBusca = nomeBusca.trim();
            int indiceEncontrado = localizarIndiceProdutoExato(nomeBusca);

            if (indiceEncontrado == -1) {
                JOptionPane.showMessageDialog(null, "Produto não encontrado! Certifique-se de digitar o nome exato.", "Erro", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (marcados[indiceEncontrado]) {
                JOptionPane.showMessageDialog(null, "Este produto já foi adicionado ao lote seletivo!", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                marcados[indiceEncontrado] = true;
                qtdSelecionada++;
                JOptionPane.showMessageDialog(null, "✓ '" + produtos[indiceEncontrado].nome + "' adicionado com sucesso!");
            }

            int respostaMais = JOptionPane.showConfirmDialog(null,
                    "Deseja selecionar mais um produto para este mesmo reajuste?",
                    "Continuar Seleção Coletiva", JOptionPane.YES_NO_OPTION);
            
            if (respostaMais != JOptionPane.YES_OPTION) {
                break;
            }

        } while (true);

        if (qtdSelecionada == 0) return;

        // Início do fluxo de captura do reajuste coletivo.
        Boolean modoAumento = escolherModoReajuste("REAJUSTE COLETIVO (" + qtdSelecionada + " ITENS)");
        if (modoAumento == null) return;

        double percentual = executarInterfaceInteligente("Configurar Reajuste Coletivo", modoAumento, 0.0);
        if (percentual == Double.MIN_VALUE) return;

        if (!modoAumento) {
            percentual = -percentual;
        }

        // Validação Preventiva e Geração da Prévia.
        boolean loteValido = true;
        StringBuilder previa = new StringBuilder();
        previa.append(String.format("PRÉVIA DO REAJUSTE COLETIVO (%.2f%%)\n\n", percentual));
        previa.append(String.format("%-25s %12s  →  %12s%n", "PRODUTO", "PREÇO ATUAL", "NOVO PREÇO"));
        previa.append("─".repeat(55)).append("\n");

        for (int i = 0; i < total; i++) {
            if (marcados[i]) {
                double novoPreco = calcularNovoPreco(produtos[i].preco, percentual);
                if (novoPreco <= 0) {
                    JOptionPane.showMessageDialog(null,
                            "OPERAÇÃO ABORTADA!\n\nO reajuste faria o produto '" + produtos[i].nome 
                            + "' ficar com preço de R$ " + String.format("%.2f", novoPreco) 
                            + "\nO preço final deve ser estritamente maior que zero.",
                            "Regra de Negócio Violada", JOptionPane.ERROR_MESSAGE);
                    loteValido = false;
                    break;
                }
                previa.append(String.format("%-25s R$%10.2f  →  R$%10.2f%n",
                        produtos[i].nome, produtos[i].preco, novoPreco));
            }
        }

        if (!loteValido) return;

        // Confirmação e Efetivação do Reajuste.
        if (confirmarTransacao(previa.toString(), "Confirmar Lote Coletivo")) {
            for (int i = 0; i < total; i++) {
                if (marcados[i]) {
                    produtos[i].preco = calcularNovoPreco(produtos[i].preco, percentual);
                }
            }
            JOptionPane.showMessageDialog(null, "Sucesso! Reajuste aplicado nos produtos selecionados.");
        }
    }

    /**
     * Realiza o reajuste de preço em apenas um produto localizado via nome exato.
     */
    private void reajustarUnico() {
        String nomeBusca = JOptionPane.showInputDialog(null,
                "REAJUSTE ÚNICO\n\nDigite o nome EXATO do produto:",
                "Buscar Produto", JOptionPane.QUESTION_MESSAGE);

        if (nomeBusca == null) return;
        nomeBusca = nomeBusca.trim();

        int idx = localizarIndiceProdutoExato(nomeBusca);
        if (idx == -1) {
            JOptionPane.showMessageDialog(null, "Produto não encontrado! Certifique-se de digitar o nome exato.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Produto produto = produtos[idx];

        Boolean modoAumento = escolherModoReajuste("REAJUSTE ÚNICO: " + produto.nome);
        if (modoAumento == null) return;

        // Passa o preço atual para validação dinâmica em tempo real.
        double percentual = executarInterfaceInteligente("Configurar Reajuste Único", modoAumento, produto.preco);
        if (percentual == Double.MIN_VALUE) return;

        if (!modoAumento) {
            percentual = -percentual;
        }

        double novoPreco = calcularNovoPreco(produto.preco, percentual);

        StringBuilder previa = new StringBuilder();
        previa.append(String.format("PRÉVIA DE ALTERAÇÃO UNITÁRIA (%.2f%%)\n\n", percentual));
        previa.append(String.format("Produto: %s%n", produto.nome));
        previa.append(String.format("Preço Atual: R$ %.2f%n", produto.preco));
        previa.append(String.format("Novo Preço:  R$ %.2f%n", novoPreco));

        if (confirmarTransacao(previa.toString(), "Confirmar Alteração Única")) {
            produto.preco = novoPreco;
            JOptionPane.showMessageDialog(null, "Preço atualizado com sucesso!");
        }
    }

    /**
     * Aplica uma alteração de preço uniforme a todos os produtos ativos do estoque,
     * impedindo ações de descontos em massa iguais ou superiores a 100%.
     */
    private void reajustarTotal() {
        Boolean modoAumento = escolherModoReajuste("REAJUSTE TOTAL DO ESTOQUE");
        if (modoAumento == null) return;

        double percentual = executarInterfaceInteligente("Configurar Reajuste Total", modoAumento, 0.0);
        if (percentual == Double.MIN_VALUE) return;

        if (!modoAumento) {
            percentual = -percentual;
        }

        // Se for redução total, impede a falha catastrófica de zerar tudo (-100%).
        if (percentual <= -100.0) {
            JOptionPane.showMessageDialog(null, 
                    "OPERAÇÃO BLOQUEADA!\nUm desconto total de 100% ou mais eliminaria o valor de todo o estoque.", 
                    "Erro de Consistência", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validação de barreira preventiva para cada item do estoque
        boolean loteValido = true;
        StringBuilder previa = new StringBuilder();
        previa.append(String.format("PRÉVIA DO REAJUSTE TOTAL DO ESTOQUE (%.2f%%)\n\n", percentual));
        previa.append(String.format("%-25s %12s  →  %12s%n", "PRODUTO", "PREÇO ATUAL", "NOVO PREÇO"));
        previa.append("─".repeat(55)).append("\n");

        for (int i = 0; i < total; i++) {
            double novoPreco = calcularNovoPreco(produtos[i].preco, percentual);
            if (novoPreco <= 0) {
                JOptionPane.showMessageDialog(null,
                        "REAJUSTE COMPROMETIDO!\n\nO produto '" + produtos[i].nome + "' quebraria o limite com o valor R$ " 
                        + String.format("%.2f", novoPreco) + "\nA transação geral foi suspensa.",
                        "Erro Matemático Preventivo", JOptionPane.ERROR_MESSAGE);
                loteValido = false;
                break;
            }
            previa.append(String.format("%-25s R$%10.2f  →  R$%10.2f%n",
                    produtos[i].nome, produtos[i].preco, novoPreco));
        }

        if (!loteValido) return;

        if (confirmarTransacao(previa.toString(), "Confirmar Reajuste em Massa")) {
            for (int i = 0; i < total; i++) {
                produtos[i].preco = calcularNovoPreco(produtos[i].preco, percentual);
            }
            JOptionPane.showMessageDialog(null, String.format("Sucesso! Todos os %d produtos do estoque foram reajustados.", total));
        }
    }

    /**
     * Interface gráfica interativa para capturar a porcentagem de reajuste por meio
     * de botões de atalho rápido (+/- 10%, 25%, 50%) ou digitação manual em caixa de texto.
     * * @param titulo        Título customizado da janela de diálogo.
     * @param ehAumento     Sinalizador lógico indicando se é aumento (true) ou redução (false).
     * @param precoContexto Preço atual do item em edição (usado para validações preventivas em tempo real).
     * @return O percentual validado positivo extraído, ou Double.MIN_VALUE se a ação for abortada.
     */
    private double executarInterfaceInteligente(String titulo, boolean ehAumento, double precoContexto) {
        JTextField campoPercentual = new JTextField(10);
        JPanel painelMensagem = new JPanel();
        painelMensagem.setLayout(new BoxLayout(painelMensagem, BoxLayout.Y_AXIS));
        
        String operacaoTexto = ehAumento ? "AUMENTO (+)" : "REDUÇÃO (-)";
        JLabel rotuloMensagem = new JLabel("Operação Ativa: " + operacaoTexto);
        JLabel rotuloInstrucao = new JLabel("Insira o valor desejado ou clique em um atalho rápido:");
        
        rotuloMensagem.setAlignmentX(Component.LEFT_ALIGNMENT);
        rotuloInstrucao.setAlignmentX(Component.LEFT_ALIGNMENT);
        campoPercentual.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        painelMensagem.add(rotuloMensagem);
        painelMensagem.add(rotuloInstrucao);
        painelMensagem.add(new JLabel(" ")); 
        painelMensagem.add(campoPercentual);
        painelMensagem.add(new JLabel(" ")); 

        String sinal = ehAumento ? "+" : "-";
        Object[] opcoesBotoes = {
            sinal + "10%",
            sinal + "25%",
            sinal + "50%",
            "Aplicar Reajuste"
        };

        while (true) {
            int escolhaBotao = JOptionPane.showOptionDialog(
                    null,
                    painelMensagem,
                    titulo,
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcoesBotoes,
                    opcoesBotoes[3]
            );

            if (escolhaBotao == JOptionPane.CLOSED_OPTION) {
                return Double.MIN_VALUE; // Cancelado.
            }

            if (escolhaBotao == 0) return 10.0;
            if (escolhaBotao == 1) return 25.0;
            if (escolhaBotao == 2) return 50.0;

            if (escolhaBotao == 3) {
                String textoDigitado = campoPercentual.getText();
                
                if (textoDigitado == null || textoDigitado.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Preencha a porcentagem ou selecione um atalho!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                try {
                    // Trata da inserção de caracteres positivos ou negativos e vírgulas.
                    String textoLimpo = textoDigitado.replace("-", "").replace("+", "").replace(",", ".").trim();
                    double valorConvertido = Double.parseDouble(textoLimpo);

                    if (valorConvertido <= 0) {
                        JOptionPane.showMessageDialog(null, "O percentual precisa ser um número maior que zero!", "Validação", JOptionPane.WARNING_MESSAGE);
                        continue;
                    }

                    // Se o reajuste violar regras de negócio a operação barra imediatamente o erro e avisa o usuário.
                    if (!ehAumento && precoContexto > 0) {
                        double novoPrecoProjetado = calcularNovoPreco(precoContexto, -valorConvertido);
                        if (novoPrecoProjetado <= 0) {
                            JOptionPane.showMessageDialog(null, 
                                    String.format("Desconto negado! O preço final resultaria em R$ %.2f (Abaixo do limite permitido).", novoPrecoProjetado), 
                                    "Erro de Limite", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                    }

                    return valorConvertido;

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Entrada inválida! Digite apenas valores numéricos decimais. Ex: 15.8", "Erro de Tipo", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Define dinamicamente as legendas dos botões de resposta rápida e captura se
     * o usuário deseja realizar um aumento (+) ou uma redução (-).
     * * @param titulo Título de cabeçalho contextual da caixa de opções.
     * @return Boolean indicando true se for aumento, false se for redução, ou null caso cancelado.
     */
    private Boolean escolherModoReajuste(String titulo) {
        javax.swing.UIManager.put("OptionPane.yesButtonText", "AUMENTO (+)");
        javax.swing.UIManager.put("OptionPane.noButtonText", "REDUÇÃO (-)");
        
        int resposta = JOptionPane.showConfirmDialog(null,
                "Selecione a operação matemática que deseja aplicar ao lote:",
                titulo,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
                
        if (resposta == JOptionPane.CLOSED_OPTION) return null;
        return resposta == JOptionPane.YES_OPTION;
    }
    
    /**
     * Apresenta o sumário/relatório descritivo prévio das alterações de preço e
     * solicita a confirmação final da gravação de dados em memória.
     * * @param conteudo Corpo textual estruturado com a prévia das alterações.
     * @param titulo   Título descritivo da janela de aviso.
     * @return Retorna true caso o usuário aprove a transação, false se rejeitada.
     */
    private boolean confirmarTransacao(String conteudo, String titulo) {
        javax.swing.UIManager.put("OptionPane.yesButtonText", "Sim, Confirmar e Salvar");
        javax.swing.UIManager.put("OptionPane.noButtonText", "Não, Cancelar Operação");
        
        int resposta = JOptionPane.showConfirmDialog(null,
                conteudo + "\n\nDeseja efetivar e gravar estas alterações de preço?",
                titulo,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        return resposta == JOptionPane.YES_OPTION;
    }
    
    /**
     * Localiza linearmente o índice de um produto correspondente no array a partir
     * de uma busca de texto insensível a maiúsculas/minúsculas.
     * * @param nome Nome textual exato do produto desejado.
     * @return O índice inteiro do produto caso seja encontrado, ou -1 caso contrário.
     */
    private int localizarIndiceProdutoExato(String nome) {
        for (int i = 0; i < total; i++) {
            if (produtos[i].nome.equalsIgnoreCase(nome)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Validação interna que verifica o estado atual do inventário impedindo que
     * rotinas operem sobre vetores vazios.
     * * @return true se o estoque contiver zero registros ativos, false caso existam itens.
     */
    private boolean nenhumProdutoCadastrado() {
        if (total == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum produto em inventário!\nCadastre produtos antes de acessar as rotinas de reajuste.",
                    "Erro de Fluxo", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }
    
    /**
     * Realiza o cálculo aritmético direto de reajuste percentual sobre um preço base.
     * * @param precoAtual Preço original do item sob o qual o fator incidirá.
     * @param percentual Índice de reajuste (valores positivos aumentam, negativos decrementam).
     * @return O novo preço calculado resultante.
     */
    private double calcularNovoPreco(double precoAtual, double percentual) {
        return precoAtual * (1 + percentual / 100.0);
    }
}