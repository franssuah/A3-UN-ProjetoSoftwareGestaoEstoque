package telas;       // Define que esta classe pertence ao pacote "telas"

import modelo.Produto;                 // Importa a classe Produto do pacote modelo
import javax.swing.JOptionPane;        // Importa o JOptionPane para criar janelas de diálogo

/**
 * Tela responsável pelo reajuste de preços dos produtos. Recebe o array de
 * produtos e o total cadastrado do MenuProduto, garantindo que ambos trabalhem
 * com os mesmos dados.
 */
public class MenuReajustePreco {

    public Produto[] produtos = new Produto[100];
    public int total = 0;

    public MenuReajustePreco(Produto[] produtos, int total) {
        this.produtos = produtos;  // "this.produtos" = atributo da classe | "produtos" = parâmetro recebido
        this.total = total;  // "this.total"    = atributo da classe | "total"    = parâmetro recebido
    }

    public void menu() {

        if (total == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum produto cadastrado!\n"
                    + "Cadastre produtos antes de reajustar.");
            return;
        }

        String opcao; // Declara a variável que vai guardar o que o usuário digitar

        // Inicia um loop que se repete indefinidamente até um "return" ou "break" interno
        do {
            // Abre uma janela com campo de texto e retorna o que o usuário digitou
            opcao = JOptionPane.showInputDialog(
                    "MENU REAJUSTE DE PREÇOS\n\n"
                    + "1 - Reajustar TODOS os produtos\n"
                    + "2 - Reajustar produto INDIVIDUAL\n"
                    + "3 - Consultar preços atuais\n"
                    + "4 - Retornar\n\n"
                    + "Opção: "
            );
            // Se o usuário clicou em "Cancelar" ou fechou a janela (X),
            // showInputDialog retorna null — então encerramos o método
            if (opcao == null) {
                return;
            }
            // opcao.trim() remove espaços em branco no início e no fim do texto digitado
            // O switch verifica qual opção foi digitada e chama o método correspondente

            switch (opcao.trim()) {
                case "1" ->
                    reajustarTodos();
                case "2" ->
                    reajustarIndividual();
                case "3" ->
                    consultarPrecos();
                case "4" -> {
                    return;
                }
                default ->
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
            }

        } while (true);
    }

    // =========================================================================
    // MÉTODO REAJUSTAR TODOS — aplica reajuste em todos os produtos
    // =========================================================================
    // "private" = só pode ser chamado dentro desta própria classe
    private void reajustarTodos() {
        // Chama o método auxiliar lerPercentual() para pedir e validar o percentual
        // A string passada é o texto que aparece na janela de entrada

        double percentual = lerPercentual("REAJUSTE GERAL\n\nPercentual de reajuste (%): ");
        // Double.MIN_VALUE é o código que lerPercentual() retorna quando o usuário cancela

        if (percentual == Double.MIN_VALUE) {
            return;
        }

        // Monta prévia de resjuste
        StringBuilder previa = new StringBuilder();
        previa.append("PRÉVIA DO REAJUSTE (").append(percentual).append("%)\n\n");
        previa.append(String.format("%-20s %10s  →  %10s%n", "PRODUTO", "ATUAL", "NOVO"));
        previa.append("─".repeat(46)).append("\n");

        for (int i = 0; i < total; i++) {
            double novoPreco = calcularNovoPreco(produtos[i].preco, percentual);
            previa.append(String.format("%-20s R$%8.2f  →  R$%8.2f%n",
                    produtos[i].nome, produtos[i].preco, novoPreco));
        }

        String confirma = JOptionPane.showInputDialog(previa + "\nCONFIRMA REAJUSTE? (S/N)");

        if (confirma != null && confirma.equalsIgnoreCase("S")) {
            for (int i = 0; i < total; i++) {
                produtos[i].preco = calcularNovoPreco(produtos[i].preco, percentual);
            }
            JOptionPane.showMessageDialog(null,
                    String.format("Reajuste de %.1f%% aplicado em %d produto(s)!", percentual, total));
        } else {
            JOptionPane.showMessageDialog(null, "Reajuste cancelado!");
        }
    }
    // reajuste individual 

    private void reajustarIndividual() {
        String novaAlteracao;

        do {
            String nome = JOptionPane.showInputDialog(
                    "REAJUSTE INDIVIDUAL\n\nDigite o nome do produto: "
            );

            if (nome == null) {
                return;
            }

            boolean encontrado = false;

            for (int i = 0; i < total; i++) {
                if (produtos[i].nome.equalsIgnoreCase(nome.trim())) {
                    Produto p = produtos[i];

                    JOptionPane.showMessageDialog(null,
                            "PRODUTO ENCONTRADO\n\n"
                            + "Nome:       " + p.nome + "\n"
                            + "Preço atual: R$ " + String.format("%.2f", p.preco) + "\n"
                            + "Unidade:    " + p.unidade + "\n"
                            + "Quantidade: " + p.quantidade
                    );

                    double percentual = lerPercentual("Percentual de reajuste para\n'" + p.nome + "' (%): ");
                    if (percentual == Double.MIN_VALUE) {
                        break;
                    }

                    double novoPreco = calcularNovoPreco(p.preco, percentual);

                    String confirma = JOptionPane.showInputDialog(
                            "Preço atual:  R$ " + String.format("%.2f", p.preco) + "\n"
                            + "Novo preço:   R$ " + String.format("%.2f", novoPreco) + "\n\n"
                            + "CONFIRMA REAJUSTE? (S/N)"
                    );

                    if (confirma != null && confirma.equalsIgnoreCase("S")) {
                        p.preco = novoPreco;
                        JOptionPane.showMessageDialog(null,
                                "Preço de '" + p.nome + "' atualizado para R$ "
                                + String.format("%.2f", p.preco) + "!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Reajuste cancelado!");
                    }

                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                JOptionPane.showMessageDialog(null, "Produto não encontrado!");
            }

            novaAlteracao = JOptionPane.showInputDialog("DESEJA REAJUSTAR OUTRO PRODUTO? (S/N)");

        } while (novaAlteracao != null && novaAlteracao.equalsIgnoreCase("S"));
    }
    // Consulta de preços

    private void consultarPrecos() {
        StringBuilder lista = new StringBuilder();
        lista.append("LISTA DE PREÇOS ATUAL\n\n");
        lista.append(String.format("%-4s %-20s %10s%n", "Nº", "PRODUTO", "PREÇO"));
        lista.append("─".repeat(38)).append("\n");

        for (int i = 0; i < total; i++) {
            lista.append(String.format("%-4d %-20s R$%7.2f%n",
                    i + 1, produtos[i].nome, produtos[i].preco));
        }

        JOptionPane.showMessageDialog(null, lista.toString());
    }

    private double lerPercentual(String mensagem) {
        while (true) {
            String entrada = JOptionPane.showInputDialog(mensagem);
            if (entrada == null) {
                return Double.MIN_VALUE;
            }

            try {
                double valor = Double.parseDouble(entrada.replace(",", ".").trim());
                if (valor == 0) {
                    JOptionPane.showMessageDialog(null, "O percentual não pode ser zero!");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Digite um número válido!");
            }
        }
    }

    private double calcularNovoPreco(double precoAtual, double percentual) {
        return precoAtual * (1 + percentual / 100.0);
    }
}
