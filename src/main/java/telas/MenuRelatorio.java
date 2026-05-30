package telas;

import modelo.Produto; // Importando os dados armazenados na memória.
import javax.swing.JOptionPane;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class MenuRelatorio {

    public Produto[] produtos = new Produto[100];
    public int total = 0;

    public MenuRelatorio(Produto[] produtos, int total) {
        this.produtos = produtos;
        this.total = total;
    }
        /**
        Este método clona o ponteiro referencial do array original de produtos ativos e 
        processa a ordenação alfabética (A-Z) em memória temporária. Atende ao requisito
        sem bagunçar os índices físicos do estoque centralizado em MenuProduto, impedindo
        a ocorrência de erros em outras telas.
        */
    private Produto[] obterProdutosOrdenados() {
        // Passo Nº 1: Inicializar um vetor de tamanho idêntico ao total de cadastros válidos atuais.
        Produto[] vetorOrdenado = new Produto[this.total];
        
        // Passo Nº 2: Copiar as referências de objetos em um laço linear.
        for (int i = 0; i < this.total; i++) {
            vetorOrdenado[i] = this.produtos[i];
        }

        // Passo Nº 3: Aplicação do Algoritmo Selection Sort.
        for (int i = 0; i < this.total - 1; i++) {
            int indiceMenor = i;
            
            // Laço secundário interno para varredura do restante do array em memória.
            for (int j = i + 1; j < this.total; j++) {
                // Comparação de strings ignorando diferenças entre letras maiúsculas e minúsculas.
                if (vetorOrdenado[j].nome.compareToIgnoreCase(vetorOrdenado[indiceMenor].nome) < 0) {
                    indiceMenor = j; // Armazena o índice do menor elemento alfabético encontrado.
                }
            }
            
            // Mecanismo de Troca utilizando uma variável auxiliar de tipo de dado abstrato.
            Produto temporario = vetorOrdenado[indiceMenor];
            vetorOrdenado[indiceMenor] = vetorOrdenado[i];
            vetorOrdenado[i] = temporario;
        }

        return vetorOrdenado; // Retorna o array perfeitamente organizado de A a Z.
    }
    
    public void menu() {

        if (total == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum produto cadastrado!\n"
                    + "Cadastre produtos antes de gerar relatórios."); // Ou seja, caso não tenha produto cadastrado, vai aparecer essas mensagens.
            return; // Aqui o sistema vai direcionar para a tela 1.0.
        }

        String opcao;

        do { // Caso existir um produto ou mais, surgirá o menu de relatórios.
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

                // Nas cases optei por usar -> (setinha) para não ter que utilziar o break, na minha opnião ficou mais bonito.
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

    // SUB ROTINAS
    private void listaDePrecos() {
        Produto[] produtosOrdenados = obterProdutosOrdenados();
        StringBuilder lista = new StringBuilder(); // StringBuilder é a classe que permite a manipulação do valor da variável. / Aqui estamos pegando a variável lista e transformando em um objeto.
        lista.append("RELATÓRIO: LISTA DE PREÇOS\n\n"); // .append é um método que adiciona texto no final da string que está sendo construido.
        lista.append(String.format("%-4s %-20s %-10s %10s%n", "Nº", "PRODUTO", "UNIDADE", "PREÇO")); // formatação utilizada para que a janela se pareça com uma tabela.
        lista.append("─".repeat(55)).append("\n"); // Ajustado para 55 repetições para alinhar com o novo espaçamento.

        for (int i = 0; i < total; i++) {
            // Formatação monetária profissional antes de exibir. Corrigido para dar append em "lista".
            String precoFormatado = formatarParaMoedaReal(produtosOrdenados[i].preco);
            
            lista.append(String.format("%-4d %-25s %-12s %-15s%n",
                    i + 1, 
                    produtosOrdenados[i].nome, 
                    produtosOrdenados[i].unidade != null ? produtosOrdenados[i].unidade.toUpperCase() : "UN",
                    precoFormatado));
        }

        lista.append("─".repeat(60)).append("\n");
        lista.append("Total de produtos: ").append(total);

        JOptionPane.showMessageDialog(null, lista.toString());
    }

    private void balancoFisico() {
        Produto[] produtosOrdenados = obterProdutosOrdenados();
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("RELATÓRIO: BALANÇO FÍSICO\n\n");
        relatorio.append(String.format("%-4s %-20s %-14s %-14s %-14s%n", "Nº", "PRODUTO", "PREÇO UN.", "QUANTIDADE", "TOTAL ITEM"));
        relatorio.append("─".repeat(70)).append("\n");
        
        double totalGeral = 0; // Correção: Inicialização da variável local que estava faltando
        
        for (int i = 0; i < total; i++) {
            double valorTotalItem = produtosOrdenados[i].preco * produtosOrdenados[i].quantidade;
            totalGeral += valorTotalItem;

        // Formatar o preço unitário e o total acumulado por item utilizando a sub-rotina.
            String precoUnidadeFormatado = formatarParaMoedaReal(produtosOrdenados[i].preco);
            String totalItemFormatado = formatarParaMoedaReal(valorTotalItem);
            
            String qtdComUnidade = formatarUnidade(produtosOrdenados[i].unidade, produtosOrdenados[i].quantidade);

            relatorio.append(String.format("%-4d %-20s %-14s %-14s %-14s%n",
                    i + 1, 
                    produtosOrdenados[i].nome, 
                    precoUnidadeFormatado,
                    qtdComUnidade, 
                    totalItemFormatado));
        }
        
        String totalGeralFormatado = formatarParaMoedaReal(totalGeral);

        relatorio.append("─".repeat(70)).append("\n");
        relatorio.append(String.format("%-51s %-15s%n", "VALOR PATRIMONIAL TOTAL DO ESTOQUE:", totalGeralFormatado));
        relatorio.append("Total de produtos cadastrados: ").append(total);

        JOptionPane.showMessageDialog(null, relatorio.toString());
    }

    private void balancoFinanceiro() {
        Produto[] produtosOrdenados = obterProdutosOrdenados(); // Mantendo a padronização de exibição ordenada.
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("RELATÓRIO: BALANÇO FINANCEIRO COMERCIAL\n\n");
        relatorio.append(String.format("%-4s %-25s %-15s %-10s %-15s%n",
                "Nº", "PRODUTO", "PREÇO UNIT.", "QTD", "VALOR TOTAL"));
        relatorio.append("─".repeat(70)).append("\n");

        double totalGeral = 0;

        for (int i = 0; i < total; i++) {
            double valorTotal = produtosOrdenados[i].preco * produtosOrdenados[i].quantidade;
            totalGeral += valorTotal;
            
            // Injeção do DecimalFormat para extinguir saídas cruas de ponto flutuante americano.
            String precoUnidadeFormatado = formatarParaMoedaReal(produtosOrdenados[i].preco);
            String totalItemFormatado = formatarParaMoedaReal(valorTotal);
            
            relatorio.append(String.format("%-4d %-25s %-15s %-10d %-15s%n",
                    i + 1, 
                    produtosOrdenados[i].nome, 
                    precoUnidadeFormatado,
                    produtosOrdenados[i].quantidade, 
                    totalItemFormatado));
        }
        
        String totalGeralFormatado = formatarParaMoedaReal(totalGeral);
        relatorio.append("─".repeat(70)).append("\n");
        relatorio.append(String.format("%-51s %-15s%n", "TOTAL FINANCEIRO EM MOVIMENTO:", totalGeralFormatado));
        relatorio.append("\nTotal de produtos: ").append(total);

        JOptionPane.showMessageDialog(null, relatorio.toString());
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
            default   -> { return quantidade + " " + unidade; }
        }
    }
}