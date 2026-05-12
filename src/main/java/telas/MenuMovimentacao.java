
package telas;

import javax.swing.JOptionPane;

public class MenuMovimentacao {

    public void menu() {

        String opcao;

        do {

            opcao = JOptionPane.showInputDialog(
                    "MOVIMENTAÇÃO DE ESTOQUE\n\n"
                    + "1 - Entrada\n"
                    + "2 - Saída\n"
                    + "0 - Retornar\n\n"
                    + "Opção:"
            );

            if (opcao == null) {
                return;
            }

            switch (opcao) {

                case "1":
                    JOptionPane.showMessageDialog(null,
                            "MENU DE ENTRADA EM DESENVOLVIMENTO");
                    break;

                case "2":
                    JOptionPane.showMessageDialog(null,
                            "MENU DE SAÍDA EM DESENVOLVIMENTO");
                    break;

                case "0":
                    return;

                default:
                    JOptionPane.showMessageDialog(null,
                            "Opção inválida!");
            }

        } while (true);
    }
}
    

