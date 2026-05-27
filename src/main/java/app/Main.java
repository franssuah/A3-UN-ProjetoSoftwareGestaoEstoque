package app;

import javax.swing.*;

import telas.MenuMovimentacao;
import telas.MenuProduto;
import telas.MenuReajustePreco;
import telas.MenuRelatorio;

public class Main {

    public static void main(String[] args) {

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
                    + "0 - Sair\n"
                    + "Opção: "
            );

            // Bloco Condicional: Trava de Segurança para proteger contra NullPointerException.
            
            /* Resolve o problema no caso de o usuário clicar em "Cancelar", fechar a janela no "X"
            ou pressionar "ESC", o JOptionPane retorna "null". O "break" atuará para identificar esta
            ação e interromper o laço imediatamente, permitindo que a Máquina Virtual Java encerre o
            método main() de forma operacional e controlada. */

            if (opcao == null) {
                break;
            }
            
            switch (opcao) {
                case "1":
                    menuProduto.menu();
                    break;
                case "2":

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

    private static void MenuReajustePreco() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
