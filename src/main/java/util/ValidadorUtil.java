package util;

/**
 * Classe dedicada a interpretar e realizar a conversão de dados (String -> Número)
 * atuando paralelamente para checar as regras de negócio do sistema de estoque.
 * * @author Franssuah (Responsável Final - Versão Fork Individual)
 */
public class ValidadorUtil {

	/**
         * Processa a entrada do usuário, adapta a formatação para o padrão americano,
         * converte para Double e valida se o valor do preço é estritamente maior que zero.
         * * @param entrada String capturada da interface com o usuário (ex: JOptionPane).
         * @return double O preço validado e convertido em caso de sucesso, ou -1.0 se 
         * ocorrer falha de conversão ou violação da regra de negócio.
         */
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
            /** Trava Nº 3: Se o usuário digitar letras ("vinte reais") em vez de "20,00" ou
            "20.00", a exceção é capturada e o método retorna -1.0.
            */
            return -1.0;
        }
    }

    /**
     * Trata a entrada do usuário, remove espaços sobressalentes, converte para 
     * Inteiro e valida se a quantidade informada é maior ou igual a zero.
     * * @param entrada String capturada da interface com o usuário (ex: JOptionPane).
     * @return int A quantidade validada e convertida em caso de sucesso, ou -1 se
     * ocorrer falha de conversão ou se o valor for negativo.
     */ 
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
            /** Trava Nº 3: Interceptar falhas com letras ou números decimais, já que a
            quantidade de peças de estoque deve ser apenas um número inteiro.
            */
            return -1;
        }
    }
}