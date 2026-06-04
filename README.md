# Projeto de Software - Gestão de Estoque

<img src="https://capsule-render.vercel.app/api?type=soft&color=0666e5&height=20&section=header" width="100%">

| **Aluno:** | **Franssuah Amorim Francisco** | **RA: 10725218227** | <a href="https://github.com/franssuah"><img src="https://img.shields.io/badge/Perfil-GitHub-181717?style=flat-square&logo=github&logoColor=white" alt="GitHub Profile" width="135" align="center"></a> |
| :--- | :--- | :---: | :---: |

> **A3 — Semestre 2026/1**  
> **Unidade Curricular:** Algoritmos e Programação  
> **Instituição:** Universidade do Sul de Santa Catarina (UNISUL)  
> **Professor:** Ricardo Ribeiro Assink

### Relatório Técnico

A versão completa e detalhada do relatório acadêmico está disponível para consulta:

<a href="https://drive.google.com/file/d/1advSxyIrl3S8O2MV86wSc6_2p3VVnUwi/view?usp=sharing">
  <img src="https://img.shields.io/badge/Google Drive-PDF-red?style=flat&logo=googledrive&logoColor=white&labelColor=555555" height="90">
</a>

### Objetivo A3

Projetar um software funcional dedicado a atender necessidades cotidianas e logísticas de empresas voltadas para o comércio utilizando estritamente a linguagem `Java`.

### Histórico de Versões

Este repositório apresenta a continuação e evolução do projeto A3, concebida a partir de um *fork* da base prévia do código elaborado inicialmente em colaboração no repositório [ppedroal/GestorEstoque](https://github.com/ppedroal/GestorEstoque). A atual versão dedica-se à refatoração estrutural e à implementação de refinamentos sobre a base de código anterior a data 28/05/2026.

### Otimizações Implementadas
**As atualizações nesta versão focam em 5 pilares principais:**
1. **Tratamento de exceções:** Adição de camadas responsáveis pela prevenção, mitigação e correção de erros em tempo de execução, eliminando falhas de interrupção;
2. **Validação de fluxos:** Implementação de parâmetros que atuam na validação de dados e das ações realizadas pelo usuário, garantindo a previsibilidade do sistema;
3. **Consistência de dados:** Aplicação das regras de negócio, impedindo o cadastro de produtos com preço igual ou menor que zero;
4. **Blindagem de estoque:** Inclusão de travas de segurança contra a ocorrência de estoque físico negativo em movimentações de saída;
5. **Experiência do Usuário (UX):** Reestruturação da interface para garantir conformidade visual, clareza e uma navegação fluida.

## Arquitetura do Código

O código-fonte segue o padrão de separação de responsabilidades para garantir coesão superior e baixo acoplamento. 

Foi organizado na seguinte árvore de pacotes em `Java`:

* **`app`**
    * `Main.java`: Ponto de entrada operacional do sistema; gerencia o ciclo de vida do menu principal e intercepta retornos nulos.
* **`modelo`**
    * `Produto.java`: Entidade pura (POJO) que encapsula os atributos logísticos do item: Nome, Preço, Unidade e Quantidade.
* **`telas`**
    * `MenuProduto.java`: Sub-rotinas para o CRUD completo (Inclusão com verificação de duplicidade, Alteração, Consulta e Exclusão).
    * `MenuMovimentacao.java`: Módulo de transações de fluxo de estoque (Entradas e Saídas com exibição prévia de Saldo Final).
    * `MenuReajustePreco.java`: Motor matemático para aplicação de correções percentuais em lote ou individuais.
    * `MenuRelatorio.java`: Camada de saída estruturada para emissão da Lista de Preços e do Balanço Físico-Financeiro.
* **`util`**
    * `ValidadorUtil.java`: Infraestrutura central de segurança lógica. Componente isolado focado no parsing amigável e validação estrita de dados de entrada de interfaces visuais.

## Como Iniciar o Sistema

### Pré-Requisitos

* **Java Development Kit:** JDK 17 LTS ou superior (Ambiente de desenvolvimento homologado em JDK 25).
* **Controle de Versão:** Git.
* Uma das IDEs homologadas listadas abaixo.

### Primeiros Passos (Execute em seu terminal):

1º Clonar Repositório
```text
git clone https://github.com/franssuah/A3-UN-ProjetoSoftwareGestaoEstoque.git
```
2º Alterar para o Diretório da Pasta Gerada
```text
cd A3-UN-ProjetoSoftwareGestaoEstoque
```

## Instruções de Inicialização por IDEs

### ➔ NetBeans IDE
1. Acesse o menu superior: `File` ➔ `Open Project...`.
2. Navegue até o diretório raiz deste repositório e selecione a pasta do projeto.
3. Clique com o botão direito sobre a raiz do projeto na aba lateral e selecione `Executar` (ou pressione `F6`).

### ➔ IntelliJ IDEA
1. Acesse: `File` ➔ `Open` e selecione o diretório raiz do projeto.
2. Se necessário, configure o SDK do projeto em: `File` ➔ `Project Structure` ➔ `Project` ➔ `SDK` (Selecione a versão correspondente ao seu JDK instalado).
3. Localize a classe `Main.java` no pacote `app` e clique no botão verde *Run*.

### ➔ Visual Studio Code
1. Certifique-se de que a extensão oficial *Extension Pack for Java* está instalada e ativa.
2. Abra a pasta raiz do projeto no VS Code.
3. Navegue até `src/app/Main.java` e acione o comando `Run` posicionado acima da assinatura do método `main`.
