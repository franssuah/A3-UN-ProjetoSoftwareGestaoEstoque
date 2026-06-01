package app;

import javax.swing.*;
import telas.MenuMovimentacao;
import telas.MenuProduto;
import telas.MenuReajustePreco;
import telas.MenuRelatorio;

/**
 * Classe principal do Sistema de Controle de Estoque.
 * Atua como o ponto de entrada da aplicação, gerenciando o laço do menu superior
 * e coordenando o compartilhamento de dados de inventário entre as telas.
 * * @author Franssuah (Responsável Final - Versão Fork Individual)
 */
public class Main {
    
    /**
     * Método de inicialização da aplicação (Ponto de Entrada).
     * Configura as instâncias centrais de produtos e gerencia o fluxo operacional
     * baseado nas escolhas do operador no JOptionPane.
     * * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        
        // Inicialização dos módulos base e compartilhamento de referências de memória.
        MenuProduto menuProduto = new MenuProduto();
        MenuMovimentacao menuMovimentacao = new MenuMovimentacao();
        menuMovimentacao.produtos = menuProduto.produtos;
        menuMovimentacao.total = menuProduto.total;

        String opcao;

        do {
            opcao = JOptionPane.showInputDialog(
                    "SISTEMA DE ESTOQUE\n\n"
                    + "1 - Administrar produtos\n"
                    + "2 - Movimentação de estoque\n"
                    + "3 - Reajuste de preços\n"
                    + "4 - Relatórios\n"
                    + "0 - Sair\n\n"
                    + "Opção: "
            );

            // Bloco Condicional: Trava de Segurança para proteger contra NullPointerException.
            /** Resolve o problema no caso de o usuário clicar em "Cancelar", fechar a janela no "X"
            ou pressionar "ESC", o JOptionPane retorna "null". O "break" atuará para identificar esta
            ação e interromper o laço imediatamente, permitindo que a Máquina Virtual Java encerre o
            método main() de forma operacional e controlada. 
            */
            if (opcao == null) {
                break;
            }
            
            switch (opcao) {
                case "1":
                    menuProduto.menu();
                    break;
                case "2":
                    // Sincronização e atualização dos ponteiros antes da execução da movimentação.
                    menuMovimentacao.produtos = menuProduto.produtos;
                    menuMovimentacao.total = menuProduto.total;
                    menuMovimentacao.menu();
                    break;
                case "3":
                    new MenuReajustePreco(menuProduto.produtos, menuProduto.total).menu();
                    break;
                case "4":
                    new MenuRelatorio(menuProduto.produtos, menuProduto.total).menu();
                    break;
                case "0":
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        } while (!opcao.equals("0"));
    }
}