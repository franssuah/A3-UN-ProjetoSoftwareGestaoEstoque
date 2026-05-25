package telas;

import modelo.Produto;
import javax.swing.JOptionPane;

public class MenuReajustePreco {

    // ─── ATRIBUTOS ────────────────────────────────────────────────────────────
    // "private" significa que só esta classe pode acessar esses dados diretamente
    private Produto[] produtos;         
    private int total;                  

    // ─── CONSTRUTOR ───────────────────────────────────────────────────────────
    /**
     * Construtor: chamado quando criamos "new MenuReajustePreco(produtos,
     * total)". Recebe os dados do MenuProduto para trabalhar no mesmo array,
     * evitando dados duplicados ou desatualizados.
     */
    public MenuReajustePreco(Produto[] produtos, int total) {
        this.produtos = produtos;       
        this.total = total;             
    }

    // ─── MENU PRINCIPAL ───────────────────────────────────────────────────────
    /**
     * Exibe o menu principal de reajuste em loop até o usuário escolher
     * Retornar. Ponto de entrada chamado pelo Main ou pelo MenuProduto.
     */
    public void menu() {

        // Subrotina de validação: verifica se há produtos antes de qualquer ação
        if (nenhumProdutoCadastrado()) {
            return;                     
        }

        String opcao;                   

        do {                            // do-while garante que o menu aparece pelo menos 1 vez
            opcao = JOptionPane.showInputDialog(
                    "MENU REAJUSTE DE PREÇOS\n\n"
                    + "1 - Reajustar TODOS os produtos\n"
                    + "2 - Reajustar produto INDIVIDUAL\n"
                    + "3 - Consultar preços atuais\n"
                    + "4 - Retornar\n\n"
                    + "Opção: "
            );

            // Se o usuário fechar a janela (X), opcao será null — encerramos para evitar erro
            if (opcao == null) {
                return;
            }

            // Direciona para o método correto conforme a opção escolhida
            switch (opcao.trim()) {     // .trim() remove espaços acidentais antes/depois
                case "1" ->
                    reajustarTodos();       // Chama subrotina de reajuste geral
                case "2" ->
                    reajustarIndividual();  // Chama subrotina de reajuste individual
                case "3" ->
                    consultarPrecos();       // Chama subrotina de consulta
                case "4" -> {
                    return;
                }              // Encerra o menu e volta para quem chamou
                default ->
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
            }

        } while (true);                 // Loop infinito — só sai pelo "return" do case "4"
    }

    // ─── SUBROTINA: REAJUSTAR TODOS ───────────────────────────────────────────
    /**
     * Aplica um mesmo percentual de reajuste em TODOS os produtos cadastrados.
     * Exibe uma prévia completa antes de confirmar para evitar erros.
     */
    private void reajustarTodos() {

        // Subrotina reutilizável que lê e valida o percentual digitado
        double percentual = lerPercentual("REAJUSTE GERAL\n\nPercentual de reajuste (%):");

        // Double.MIN_VALUE é o valor sentinela que lerPercentual() devolve quando
        // o usuário cancela (fecha a janela). Se isso acontecer, abortamos.
        if (percentual == Double.MIN_VALUE) {
            return;
        }

        // Monta a string de prévia usando StringBuilder (mais eficiente que concatenar Strings)
        StringBuilder previa = new StringBuilder();
        previa.append(String.format("PRÉVIA DO REAJUSTE (%.1f%%)\n\n", percentual));
        previa.append(String.format("%-20s %10s  →  %10s%n", "PRODUTO", "ATUAL", "NOVO"));
        previa.append("─".repeat(46)).append("\n");   // Linha separadora

        // Percorre todos os produtos e calcula o novo preço (sem aplicar ainda)
        for (int i = 0; i < total; i++) {
            // Subrotina que faz o cálculo do novo preço com o percentual
            double novoPreco = calcularNovoPreco(produtos[i].preco, percentual);
            previa.append(String.format("%-20s R$%8.2f  →  R$%8.2f%n",
                    produtos[i].nome, produtos[i].preco, novoPreco));
        }

        // Exibe a prévia e pede confirmação antes de alterar qualquer valor
        String confirma = JOptionPane.showInputDialog(previa + "\nCONFIRMA REAJUSTE? (S/N)");

        if (confirma != null && confirma.equalsIgnoreCase("S")) {
            // Confirmado: percorre novamente e aplica o novo preço de fato
            for (int i = 0; i < total; i++) {
                produtos[i].preco = calcularNovoPreco(produtos[i].preco, percentual);
            }
            JOptionPane.showMessageDialog(null,
                    String.format("Reajuste de %.1f%% aplicado em %d produto(s)!", percentual, total));
        } else {
            JOptionPane.showMessageDialog(null, "Reajuste cancelado!");
        }
    }

    // ─── SUBROTINA: REAJUSTAR INDIVIDUAL ─────────────────────────────────────
    /**
     * Permite reajustar o preço de um produto específico buscado pelo nome.
     * Repete o processo até o usuário optar por não reajustar outro produto.
     */
    private void reajustarIndividual() {
        String novaAlteracao;           // Armazena a resposta S/N para continuar ou parar

        do {
            String nome = JOptionPane.showInputDialog(
                    "REAJUSTE INDIVIDUAL\n\nDigite o nome do produto: ");

            if (nome == null) {         // Usuário fechou a janela
                return;
            }

            // Subrotina que busca o produto pelo nome e retorna sua posição no array
            // Retorna -1 se o produto não for encontrado
            int idx = buscarProduto(nome.trim());

            if (idx == -1) {
                // Produto não encontrado: avisa e volta ao início do loop
                JOptionPane.showMessageDialog(null, "Produto não encontrado!");
            } else {
                // Produto encontrado: guarda referência direta para facilitar o acesso
                Produto p = produtos[idx];

                // Exibe os dados atuais do produto encontrado
                exibirDadosProduto(p);  // Subrotina que monta e exibe as informações

                // Lê e valida o percentual de reajuste para este produto
                double percentual = lerPercentual(
                        "Percentual de reajuste para\n'" + p.nome + "' (%):");

                if (percentual == Double.MIN_VALUE) {
                    // Usuário cancelou a digitação do percentual — volta ao loop
                    novaAlteracao = JOptionPane.showInputDialog(
                            "DESEJA REAJUSTAR OUTRO PRODUTO? (S/N)");
                    continue;           // Pula direto para a verificação do while
                }

                // Calcula o novo preço sem alterar ainda
                double novoPreco = calcularNovoPreco(p.preco, percentual);

                // Exibe prévia do produto individual e pede confirmação
                String confirma = JOptionPane.showInputDialog(
                        "Preço atual: R$ " + String.format("%.2f", p.preco) + "\n"
                        + "Novo preço:  R$ " + String.format("%.2f", novoPreco) + "\n\n"
                        + "CONFIRMA REAJUSTE? (S/N)");

                if (confirma != null && confirma.equalsIgnoreCase("S")) {
                    p.preco = novoPreco;    // Aplica o novo preço no produto
                    JOptionPane.showMessageDialog(null,
                            "Preço de '" + p.nome + "' atualizado para R$ "
                            + String.format("%.2f", p.preco) + "!");
                } else {
                    JOptionPane.showMessageDialog(null, "Reajuste cancelado!");
                }
            }

            novaAlteracao = JOptionPane.showInputDialog("DESEJA REAJUSTAR OUTRO PRODUTO? (S/N)");

            // Continua o loop enquanto o usuário responder "S"
        } while (novaAlteracao != null && novaAlteracao.equalsIgnoreCase("S"));
    }

    // ─── SUBROTINA: CONSULTAR PREÇOS ──────────────────────────────────────────
    /**
     * Exibe uma tabela com todos os produtos e seus preços atuais. Não realiza
     * nenhuma alteração — somente leitura.
     */
    private void consultarPrecos() {
        StringBuilder lista = new StringBuilder();  // Mais eficiente que somar Strings no loop
        lista.append("LISTA DE PREÇOS ATUAL\n\n");
        lista.append(String.format("%-4s %-20s %10s%n", "Nº", "PRODUTO", "PREÇO"));
        lista.append("─".repeat(38)).append("\n");  // Linha separadora do cabeçalho

        // Percorre todos os produtos e formata cada linha da tabela
        for (int i = 0; i < total; i++) {
            lista.append(String.format("%-4d %-20s R$%7.2f%n",
                    i + 1, produtos[i].nome, produtos[i].preco));
            //  i+1 = número visual (começa em 1, não em 0)
            //  %-20s = texto alinhado à esquerda com 20 caracteres de largura
            //  %7.2f = número com 7 dígitos, 2 decimais, alinhado à direita
        }

        JOptionPane.showMessageDialog(null, lista.toString());
    }

    /**
     * ═════════════════════════════════════════════════════════════════════════
     * SUBROTINAS AUXILIARES (utilitários reutilizados em vários métodos)
     * ═════════════════════════════════════════════════════════════════════════
     * /** SUBROTINA: Verifica se não há produtos cadastrados. Centraliza essa
     * verificação para não repetir o mesmo bloco em todo lugar. Retorna true se
     * estiver vazio (e já exibe o aviso), false se houver produtos.
     */
    private boolean nenhumProdutoCadastrado() {
        if (total == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum produto cadastrado!\n"
                    + "Cadastre produtos antes de reajustar.");
            return true;
        }
        return false;
    }

    /**
     * SUBROTINA: Busca um produto pelo nome (sem diferenciar
     * maiúsculas/minúsculas). Retorna o índice (posição) do produto no array,
     * ou -1 se não encontrar. Centraliza a busca para não repetir o for em
     * reajustarIndividual e outros métodos.
     */
    private int buscarProduto(String nome) {
        for (int i = 0; i < total; i++) {
            // equalsIgnoreCase: compara ignorando maiúsculas — "notebook" == "Notebook"
            if (produtos[i].nome.equalsIgnoreCase(nome)) {
                return i;               // Retorna a posição assim que encontrar
            }
        }
        return -1;                      // Não encontrado
    }

    /**
     * SUBROTINA: Exibe os dados atuais de um produto em uma janela de
     * informação. Evita repetir o mesmo bloco de showMessageDialog em vários
     * métodos.
     */
    private void exibirDadosProduto(Produto p) {
        JOptionPane.showMessageDialog(null,
                "PRODUTO ENCONTRADO\n\n"
                + "Nome:        " + p.nome + "\n"
                + "Preço atual: R$ " + String.format("%.2f", p.preco) + "\n"
                + "Unidade:     " + p.unidade + "\n"
                + "Quantidade:  " + p.quantidade);
    }

    /**
     * SUBROTINA: Lê e valida o percentual de reajuste digitado pelo usuário.
     * Fica em loop até receber um número válido e diferente de zero. Retorna
     * Double.MIN_VALUE como sinal de cancelamento (usuário fechou a janela).
     */
    private double lerPercentual(String mensagem) {
        while (true) {                  // Loop até entrada válida ou cancelamento
            String entrada = JOptionPane.showInputDialog(
                    mensagem + "\n(Use valor negativo para redução de preço)");

            if (entrada == null) {      // Usuário fechou a janela sem digitar
                return Double.MIN_VALUE;// Valor sentinela: sinaliza "cancelado" para quem chamou
            }

            try {
                // .replace(",", ".") permite que o usuário digite vírgula como separador decimal
                double valor = Double.parseDouble(entrada.replace(",", ".").trim());

                if (valor == 0) {
                    JOptionPane.showMessageDialog(null, "O percentual não pode ser zero!");
                } else {
                    return valor;       // Valor válido: retorna para o método que chamou
                }
            } catch (NumberFormatException e) {
                // Lançada quando o usuário digita texto em vez de número (ex: "abc")
                JOptionPane.showMessageDialog(null, "Digite um número válido! Ex: 10 ou -5");
            }
        }
    }

    /**
     * SUBROTINA: Calcula o novo preço aplicando o percentual de reajuste.
     * Fórmula: precoAtual × (1 + percentual ÷ 100) Exemplo: R$100 com 10% → 100
     * × 1,10 = R$110 Exemplo: R$100 com -5% → 100 × 0,95 = R$95
     */
    private double calcularNovoPreco(double precoAtual, double percentual) {
        return precoAtual * (1 + percentual / 100.0);
        // 100.0 (e não 100) garante divisão decimal — em Java, 10/100 = 0, mas 10/100.0 = 0.1
    }
}
