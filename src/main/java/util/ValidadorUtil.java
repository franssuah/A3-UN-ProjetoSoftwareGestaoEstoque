package util;

/* Classe dedicada a interpretar e realizar a conversão de dados (String -> Número) atuando
paralelamente para checar as regras de negócio do sistema. */

public class ValidadorUtil {

	/* Processar a entrada do usuário, adaptar a formatação, converter para Double e validar
    se o valor é maior que zero. */
	
	public static double parseEValidarPreco(String entrada) {
        // Trava Nº 1: Se o usuário clicar em cancelar na janela ou enviar caractere vazio.
        if (entrada == null || entrada.trim().isEmpty()) {
            return -1.0;
        }

        try {
            
            // Ajustar a entrada do usuário para substituir vírgula por ponto.

            /* Nosso usuário típico será brasileiro, cujo hábito majoritário consiste em usar
            vírgulas para separar decimais em valores monetários e a pontuação exigida pelo
            Java é o padrão americano que utiliza ponto. */

            String entradaCorrigida = entrada.replace(",", ".").trim();
            double preco = Double.parseDouble(entradaCorrigida);

            // Trava Nº 2: Regra de Negócio = O preço DEVE ser maior que zero.
            if (preco > 0) {
                return preco;
            } else {
                return -1.0; // Preço negativo ou zero é automaticamente rejeitado.
            }

        } catch (NumberFormatException e) {
            /* Trava Nº 3: Se o usuário digitar letras ("vinte reais") em vez de "20,00" ou
            "20.00", a exceção é capturada e o método retorna -1.0. */
            return -1.0;
        }
    }

    /* Tratar a entrada do usuário, converter para Inteiro e validar se a quantidade é maior
    ou igual a zero. */
       
    public static int parseEValidarQuantidade(String entrada) {
        // Trava Nº 1: Prevenção contra NullPointerException e NumberFormatException.
        if (entrada == null || entrada.trim().isEmpty()) {
            return -1;
        }

        try {
            int quantidade = Integer.parseInt(entrada.trim());

            // Trava Nº 2: Regra de Negócio = A quantidade deve ser maior ou igual a zero.
            if (quantidade >= 0) {
                return quantidade;
            } else {
                return -1; // Quantidade negativa (estoque negativo) é rejeitada.
            }

        } catch (NumberFormatException e) {
            /* Trava Nº 3: Interceptar falhas com letras ou números decimais, já que a
            quantidade de peças de estoque deve ser apenas um número inteiro. */
            return -1;
        }
    }
}