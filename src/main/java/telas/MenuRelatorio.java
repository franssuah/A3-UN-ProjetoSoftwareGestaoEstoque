package telas;

import modelo.Produto;
import javax.swing.JOptionPane;

public class MenuRelatorio {

    public Produto[] produtos = new Produto[100];
    public int total = 0;

    public MenuRelatorio(Produto[] produtos, int total) {
        this.produtos = produtos;
        this.total = total;
    }

    public void menu() {

        if (total == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum produto cadastrado!\n"
                    + "Cadastre produtos antes de gerar relatórios."); // Ou seja, caso não tenha produto cadastrado, vai aparecer essas mensagens.
            return; // Aqui o sistema vai me direcionar para a page home.
        }

        String opcao;

        do { // Caso exisitir um produto ou mais, surguirá o menu de relatórios.
            opcao = JOptionPane.showInputDialog(
                    "MENU RELATÓRIOS\n\n"
                    + "1 - Lista de Preços\n"
                    + "2 - Balanço Físico\n"
                    + "3 - Balanço Financeiro\n"
                    + "4 - Retornar\n\n"
                    + "Opção: "
            );

            if (opcao == null) { // Caso não coloque valor, retorna ao menu.
                return;
            }

            switch (opcao.trim()) { // Já que opcao é uma string, o .trim remove espaços em branco do início e do fim, evitando a quebra do sistema.

                //Nas cases optei por usar -> (setinha) para não ter que utilziar o break, na minha opnião ficou mais bonito.
                case "1" ->
                    listaDePrecos();
                case "2" ->
                    balancoFisico();
                case "3" ->
                    balancoFinanceiro();
                case "4" -> {
                    return;
                }
                default ->
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
            }

        } while (true);
    }

    private void listaDePrecos() {
        StringBuilder lista = new StringBuilder(); // StringBuilder é a classe que permite a manipulação do valor da variável.
        lista.append("RELATÓRIO: LISTA DE PREÇOS\n\n");
        lista.append(String.format("%-4s %-20s %-10s %10s%n", "Nº", "PRODUTO", "UNIDADE", "PREÇO")); //formatação utilizada para que a janela se pareça com uma tabela.
        lista.append("─".repeat(48)).append("\n"); // Aqui ele repete 48x o ─ (alt + 192)

        for (int i = 0; i < total; i++) {
            lista.append(String.format("%-4d %-20s %-10s R$%8.2f%n",
                    i + 1, produtos[i].nome, produtos[i].unidade, produtos[i].preco));
        }

        lista.append("─".repeat(48)).append("\n");
        lista.append("Total de produtos: ").append(total);

        JOptionPane.showMessageDialog(null, lista.toString());
    }

    private void balancoFisico() {
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("RELATÓRIO: BALANÇO FÍSICO\n\n");
        relatorio.append(String.format("%-4s %-20s %s%n", "Nº", "PRODUTO", "QUANTIDADE"));
        relatorio.append("─".repeat(44)).append("\n");

        for (int i = 0; i < total; i++) {
            relatorio.append(String.format("%-4d %-20s %s%n",
                    i + 1, produtos[i].nome,
                    formatarUnidade(produtos[i].unidade, produtos[i].quantidade)));
        }

        relatorio.append("─".repeat(44)).append("\n");
        relatorio.append("Total de produtos: ").append(total);

        JOptionPane.showMessageDialog(null, relatorio.toString());
    }

    private void balancoFinanceiro() {
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("RELATÓRIO: BALANÇO FINANCEIRO\n\n");
        relatorio.append(String.format("%-4s %-20s %12s %6s %14s%n",
                "Nº", "PRODUTO", "PREÇO UNIT.", "QTD", "VALOR TOTAL"));
        relatorio.append("─".repeat(60)).append("\n");

        double totalGeral = 0;

        for (int i = 0; i < total; i++) {
            double valorTotal = produtos[i].preco * produtos[i].quantidade;
            totalGeral += valorTotal;
            relatorio.append(String.format("%-4d %-20s R$%8.2f %6d    R$%9.2f%n",
                    i + 1, produtos[i].nome, produtos[i].preco,
                    produtos[i].quantidade, valorTotal));
        }

        relatorio.append("─".repeat(60)).append("\n");
        relatorio.append(String.format("%44s R$%9.2f%n", "TOTAL:", totalGeral));
        relatorio.append("\nTotal de produtos: ").append(total);

        JOptionPane.showMessageDialog(null, relatorio.toString());
    }

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
}
