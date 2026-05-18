
package telas;

import javax.swing.JOptionPane;

import modelo.Produto;

public class MenuMovimentacao {
      Produto[] produtos;
    int total;

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
                    entrada();
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
    public void entrada() {
         /*
  * Método responsável pela entrada
  * de produtos no estoque.
  */


    String novaEntrada;

    do {

        String nome = JOptionPane.showInputDialog(
                "ENTRADA DE PRODUTO\n\n"
                + "DIGITE O NOME DO PRODUTO:"
        );

        if (nome == null) {
            break;
        }

        boolean existe = false;

        // PROCURA O PRODUTO NO VETOR
        for (int i = 0; i < total; i++) {

            if (produtos[i].nome.equalsIgnoreCase(nome.trim())) {

                Produto produto = produtos[i];

                JOptionPane.showMessageDialog(null,
                        "PRODUTO ENCONTRADO\n\n"
                        + "Produto: " + produto.nome + "\n"
                        + "Preço: R$ " + String.format("%.2f", produto.preco) + "\n"
                        + "Unidade: " + produto.unidade + "\n"
                        + "Quantidade Atual: " + produto.quantidade
                );

                int quantidadeEntrada;

                // VALIDA A QUANTIDADE
                while (true) {

                    try {

                        quantidadeEntrada = Integer.parseInt(
                                JOptionPane.showInputDialog(
                                        "QUANTIDADE DE ENTRADA:"
                                )
                        );

                        if (quantidadeEntrada > 0) {
                            break;
                        }

                        JOptionPane.showMessageDialog(null,
                                "Digite uma quantidade válida!");

                    } catch (NumberFormatException e) {

                        JOptionPane.showMessageDialog(null,
                                "Digite apenas números!");
                    }
                }

                String confirma = JOptionPane.showInputDialog(
                        "CONFIRMA ENTRADA? (S/N)"
                );

                if (confirma != null
                        && confirma.equalsIgnoreCase("S")) {

                    // SOMA A NOVA QUANTIDADE AO ESTOQUE
                    produto.quantidade += quantidadeEntrada;

                    JOptionPane.showMessageDialog(null,
                            "ENTRADA REALIZADA COM SUCESSO!\n\n"
                            + "NOVO ESTOQUE: "
                            + produto.quantidade
                    );

                } else {

                    JOptionPane.showMessageDialog(null,
                            "ENTRADA CANCELADA!");
                }

                existe = true;
                break;
            }
        }

        if (!existe) {

            JOptionPane.showMessageDialog(null,
                    "Produto não encontrado!");
        }

        novaEntrada = JOptionPane.showInputDialog(
                "DESEJA FAZER UMA NOVA ENTRADA? (S/N)"
        );

    } while (novaEntrada != null
            && novaEntrada.equalsIgnoreCase("S"));
}
}
    

