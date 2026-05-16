package telas;       // Define que esta classe pertence ao pacote "telas"

import modelo.Produto;                 // Importa a classe Produto do pacote modelo
import javax.swing.JOptionPane;        // Importa o JOptionPane para criar janelas de diálogo

public class MenuReajustePreco {

    public Produto[] produtos = new Produto[100];
    public int total = 0;

    public MenuReajustePreco(Produto[] produtos, int total) {
        this.produtos = produtos;
        this.total = total;
    }

    public void menu() {

        if (total == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum produto cadastrado!\n"
                    + "Cadastre produtos antes de reajustar.");
            return;
        }

        String opcao;

        do {
            opcao = JOptionPane.showInputDialog(
                    "MENU REAJUSTE DE PREÇOS\n\n"
                    + "1 - Reajustar TODOS os produtos\n"
                    + "2 - Reajustar produto INDIVIDUAL\n"
                    + "3 - Consultar preços atuais\n"
                    + "4 - Retornar\n\n"
                    + "Opção: "
            );

            if (opcao == null) {
                return;
            }

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

    private void reajustarTodos() {

        double percentual = lerPercentual("REAJUSTE GERAL\n\nPercentual de reajuste (%): ");
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
