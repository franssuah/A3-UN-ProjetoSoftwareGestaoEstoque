package telas;

import modelo.Produto; // Importando os dados armazenados na memória.
import javax.swing.JOptionPane;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
// INCLUSÃO DOS IMPORTS TEMPORAIS DA API MODERN DO JAVA (JAVA 8+)
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MenuRelatorio {

    public Produto[] produtos = new Produto[100];
    public int total = 0;

    public MenuRelatorio(Produto[] produtos, int total) {
        this.produtos = produtos;
        this.total = total;
    }   
        
        /**
        SUB-ROTINA AUXILIAR PRIVADA (ENCAPSULAMENTO)
        Esta função se conecta com a API de tempo do sistema operacional via JVM, captura 
        a data corrente e a formata sob o padrão regulatório nacional (dd/MM/yyyy).
        Garante manutenibilidade centralizada: se o padrão mudar, altera-se apenas aqui.
        */
    private String obterDataFormatada() {
        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dataAtual.format(formatador);
    }
    
        /**
        Este método clona o ponteiro referencial do array original de produtos ativos e 
        processa a ordenação alfabética (A-Z) em memória temporária. Atende ao requisito
        sem bagunçar os índices físicos do estoque centralizado em MenuProduto e impede a
        ocorrência de erros em outras telas.
        */
    private Produto[] obterProdutosOrdenados() {
        Produto[] vetorOrdenado = new Produto[this.total];
        for (int i = 0; i < this.total; i++) {
            vetorOrdenado[i] = this.produtos[i];
        }

        for (int i = 0; i < this.total - 1; i++) {
            int indiceMenor = i;
            for (int j = i + 1; j < this.total; j++) {
                if (vetorOrdenado[j].nome.compareToIgnoreCase(vetorOrdenado[indiceMenor].nome) < 0) {
                    indiceMenor = j;
                }
            }
            Produto aux = vetorOrdenado[i];
            vetorOrdenado[i] = vetorOrdenado[indiceMenor];
            vetorOrdenado[indiceMenor] = aux;
        }
        return vetorOrdenado;
    }
    
    public void menu() {
        if (this.total == 0) {
            JOptionPane.showMessageDialog(null,
                    "SISTEMA DE ESTOQUE - RELATÓRIOS\n\n"
                    + "Aviso: Não existem produtos cadastrados no inventário para emissão.", 
                    "Relatório Vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String opcao;
        do {
            opcao = JOptionPane.showInputDialog(
                    "EMISSÃO DE RELATÓRIOS GERENCIAIS\n\n"
                    + "1 - Listagem de Preços (A-Z)\n"
                    + "2 - Balanço Físico de Estoque\n"
                    + "3 - Balanço Financeiro do Patrimônio\n"
                    + "0 - Retornar ao Menu Principal\n\n"
                    + "Selecione o relatório operacional:"
            );

            if (opcao == null) {
                return;
            }

            switch (opcao) {
                case "1" -> listaDePrecos();
                case "2" -> balancoFisico();
                case "3" -> balancoFinanceiro();
                case "0" -> { return; }
                default -> JOptionPane.showMessageDialog(null, "Opção inválida! Tente novamente.");
            }
        } while (true);
    }

    private void listaDePrecos() {
        Produto[] ordenados = obterProdutosOrdenados();
        StringBuilder sb = new StringBuilder();
        
        // SUBSTITUIÇÃO DO MARCADOR ESTÁTICO (99/99/99) PELA SUB-ROTINA AUXILIAR PRIVADA.
        sb.append("EMPRESA LTDA. - RELATÓRIO DE PREÇOS\n");
        sb.append("DATA DE EMISSÃO: ").append(obterDataFormatada()).append("\n");
        sb.append("====================================================\n");
        sb.append(String.format("%-25s %-15s %-15s\n", "NOME DO PRODUTO", "UNIDADE", "PREÇO UNITÁRIO"));
        sb.append("----------------------------------------------------\n");

        for (int i = 0; i < this.total; i++) {
            sb.append(String.format("%-25s %-15s %-15s\n",
                    ordenados[i].nome, 
                    ordenados[i].unidade.toUpperCase(), 
                    formatarParaMoedaReal(ordenados[i].preco)));
        }
        sb.append("====================================================\n");
        sb.append("Fim da listagem de preços.");

        JOptionPane.showMessageDialog(null, sb.toString(), "Relatório de Preços (A-Z)", JOptionPane.INFORMATION_MESSAGE);
    }

    private void balancoFisico() {
        Produto[] ordenados = obterProdutosOrdenados();
        StringBuilder sb = new StringBuilder();
        int totalItensEstoque = 0;
        
        // SUBSTITUIÇÃO DO MARCADOR ESTÁTICO (99/99/99) PELA SUB-ROTINA AUXILIAR PRIVADA.
        sb.append("EMPRESA LTDA. - BALANÇO FÍSICO CENTRAL\n");
        sb.append("DATA DE EMISSÃO: ").append(obterDataFormatada()).append("\n");
        sb.append("====================================================\n");
        sb.append(String.format("%-25s %-20s\n", "NOME DO PRODUTO", "QUANTIDADE EM ESTOQUE"));
        sb.append("----------------------------------------------------\n");
        
        for (int i = 0; i < this.total; i++) {
            sb.append(String.format("%-25s %-20s\n", 
                    ordenados[i].nome, 
                    formatarUnidade(ordenados[i].unidade, ordenados[i].quantidade)));
            totalItensEstoque += ordenados[i].quantidade;
        }
        
        sb.append("====================================================\n");
        sb.append("TOTAL DE ITENS NO ESTOQUE: ").append(totalItensEstoque).append(" unidades físicas.\n");
        sb.append("====================================================\n");

        JOptionPane.showMessageDialog(null, sb.toString(), "Balanço Físico de Estoque", JOptionPane.INFORMATION_MESSAGE);
    }

    private void balancoFinanceiro() {
        Produto[] ordenados = obterProdutosOrdenados();
        StringBuilder sb = new StringBuilder();
        double valorTotalPatrimonio = 0.0;
        
        // SUBSTITUIÇÃO DO MARCADOR ESTÁTICO (99/99/99) PELA SUB-ROTINA AUXILIAR PRIVADA.
        sb.append("EMPRESA LTDA. - BALANÇO FINANCEIRO DE ATIVOS\n");
        sb.append("DATA DE EMISSÃO: ").append(obterDataFormatada()).append("\n");
        sb.append("====================================================\n");
        sb.append(String.format("%-25s %-12s %-15s\n", "NOME DO PRODUTO", "SALDO FÍSICO", "VALOR TOTAL (R$)"));
        sb.append("----------------------------------------------------\n");
        
        for (int i = 0; i < this.total; i++) {
            double custoTotalProduto = ordenados[i].quantidade * ordenados[i].preco;
            sb.append(String.format("%-25s %-12d %-15s\n", 
                    ordenados[i].nome, 
                    ordenados[i].quantidade, 
                    formatarParaMoedaReal(custoTotalProduto)));
            valorTotalPatrimonio += custoTotalProduto;
        }
        
        sb.append("====================================================\n");
        sb.append("VALOR TOTAL DO ESTOQUE EM MOVIMENTO: ").append(formatarParaMoedaReal(valorTotalPatrimonio)).append("\n");
        sb.append("====================================================\n");

        JOptionPane.showMessageDialog(null, sb.toString(), "Balanço Financeiro - Patrimônio", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
    Sub-rotina utilitária e privada para centralizar a formatação de valores monetários.
    Utiliza a infraestrutura DecimalFormat e força os padrões comerciais brasileiros (PT-BR),
    garantindo que pontos separem milhares e vírgulas separem centavos independentemente do
    Local ou Sistema Operacional. Posicionada corretamente como membro isolado da classe.
    */
    private String formatarParaMoedaReal(double valor) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator('.');
        simbolos.setDecimalSeparator(',');
        
        // Garante ao menos um zero na unidade e duas casas nos centavos
        DecimalFormat formatador = new DecimalFormat("#,##0.00", simbolos);
        return "R$ " + formatador.format(valor);
    }
    
    private String formatarUnidade(String unidade, int quantidade) {
        // Blindagem contra NullPointerException caso a unidade não tenha sido informada.
        if (unidade == null) {
            return quantidade + " UN (Unidades)";
        }       
        switch (unidade.toUpperCase()) {
            case "KG" -> { return quantidade + " KG (Quilogramas)"; }
            case "G"  -> { return quantidade + " G (Gramas)"; }
            case "L"  -> { return quantidade + " L (Litros)"; }
            case "ML" -> { return quantidade + " ML (Mililitros)"; }
            case "UN" -> { return quantidade + " UN (Unidades)"; }
            case "CX" -> { return quantidade + " CX (Caixas)"; }
            case "PC" -> { return quantidade + " PC (Pacotes)"; }
            default   -> { return quantityFormatada(unidade, quantidade); }
        }
    }
    
    private String quantityFormatada(String unidade, int quantidade) {
        return quantidade + " " + unidade;
    }
}