package modelo;

/**
 * Representa a entidade de um Produto dentro do Sistema de Controle de Estoque.
 * Esta classe atua como o modelo de dados estrutural para o armazenamento
 * e manipulação dos itens em inventário.
 * * @author Franssuah (Responsável Final - Versão Fork Individual)
 */
public class Produto {
    
    /**
     * O nome ou descrição identificadora do produto.
     */
    public String nome;
    
    /**
     * A unidade de medida do produto (ex: UN, KG, LT).
     */
    public String unidade;
    
    /**
     * O preço unitário de venda ou avaliação do produto.
     */
    public double preco;
    
    /**
     * A quantidade total do produto atualmente disponível em estoque.
     */
    public int quantidade;
}