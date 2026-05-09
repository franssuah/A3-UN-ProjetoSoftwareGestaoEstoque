package app;

import javax.swing.*;
import telas.MenuProduto;
import telas.MenuReajustePreco;
import telas.MenuRelatorio;

public class Main {

    public static void main(String[] args) {

        MenuProduto menuProduto = new MenuProduto();

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

            switch (opcao) {
                case "1":
                    menuProduto.menu();
                    break;
                case "2":
                    JOptionPane.showMessageDialog(null, "Em desenvolvimento...");
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
