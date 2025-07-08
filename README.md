# Sistema de Gerenciamento de Livros

## 📚 Visão Geral

Este projeto é uma versão melhorada e refatorada do sistema de gerenciamento de livros original. O sistema foi completamente reestruturado com melhorias significativas em arquitetura, interface, paginação e suporte a múltiplos bancos de dados.

## ✨ Principais Melhorias

### 🏗️ Arquitetura

- **Separação de responsabilidades**: Criação de pacotes específicos para configuração, serviços, utilitários e exceções
- **Factory Pattern**: Implementação do padrão Factory para gerenciamento de conexões
- **Service Layer**: Camada de serviços para lógica de negócio
- **Exception Handling**: Sistema robusto de tratamento de exceções

### 🎨 Interface do Usuário

- **Look and Feel**: Configuração automática do tema do sistema operacional
- **Interface moderna**: Nova interface principal com melhor usabilidade
- **Componentes reutilizáveis**: Criação de componentes GUI modulares
- **Feedback visual**: Melhor feedback para o usuário em operações

### 📄 Sistema de Paginação

- **Paginação avançada**: Controle completo de navegação entre páginas
- **Configuração flexível**: Número de itens por página configurável
- **Performance otimizada**: Carregamento eficiente de dados
- **Interface intuitiva**: Controles de navegação user-friendly

### 🗄️ Suporte a Múltiplos Bancos

- **PostgreSQL**: Suporte completo (banco padrão)
- **MySQL**: Suporte completo (banco alternativo)
- **Configuração dinâmica**: Troca de banco sem recompilação
- **Scripts automatizados**: Scripts para inicialização de ambos os bancos

## 📁 Estrutura do Projeto

```
projeto-livros/
├── src/
│   └── combo/
│       ├── config/          # Configurações do sistema
│       │   └── DatabaseConfig.java
│       ├── exception/       # Exceções customizadas
│       │   └── DatabaseException.java
│       ├── factory/         # Factories para criação de objetos
│       │   └── ConnectionFactory.java
│       ├── service/         # Camada de serviços
│       │   └── LivroService.java
│       ├── util/           # Utilitários e helpers
│       │   ├── PaginationResult.java
│       │   └── PaginationController.java
│       ├── gui/            # Interface gráfica
│       │   ├── GuiPrincipal.java
│       │   └── components/
│       │       └── PaginationPanel.java
│       ├── principal/      # Classe principal
│       │   └── Principal.java
│       ├── dao/            # Data Access Objects (originais)
│       ├── bd/             # Conexões de banco (originais)
│       └── controller/     # Controladores (originais)
├── src/resources/          # Recursos e configurações
│   └── database.properties
├── scripts/                # Scripts de automação
│   ├── start-postgresql.sh
│   ├── start-mysql.sh
│   ├── stop-postgresql.sh
│   ├── stop-mysql.sh
│   └── compile-and-run.sh
├── postgres-livros/        # Configuração PostgreSQL
│   └── docker-compose.yaml
├── mysql-livros/          # Configuração MySQL
│   └── docker-compose.yaml
└── README.md
```

## 🚀 Como Executar

### Pré-requisitos

- Java 11 ou superior
- Docker e Docker Compose
- Git

### 1. Configuração do Banco de Dados

#### PostgreSQL (Padrão)

```bash
cd projeto-livros
./scripts/start-postgresql.sh
```

#### MySQL (Alternativo)

```bash
cd projeto-livros
./scripts/start-mysql.sh
```

### 2. Compilação e Execução

```bash
./scripts/compile-and-run.sh
```

### 3. Troca de Banco de Dados

Para trocar entre PostgreSQL e MySQL, edite o arquivo `src/resources/database.properties` ou use o método `DatabaseConfig.setCurrentDatabaseType("mysql")` no código.

## 🔧 Configurações

### Arquivo database.properties

```properties
# PostgreSQL (padrão)
postgresql.driver=org.postgresql.Driver
postgresql.url=jdbc:postgresql://localhost:5432/livros
postgresql.username=livros_user
postgresql.password=livros_pass

# MySQL (alternativo)
mysql.driver=com.mysql.cj.jdbc.Driver
mysql.url=jdbc:mysql://localhost:3306/livros
mysql.username=livros_user
mysql.password=livros_pass
```

## 📊 Funcionalidades da Paginação

### Características

- **Navegação**: Primeira, anterior, próxima, última página
- **Salto direto**: Ir para página específica
- **Configuração**: Itens por página (10, 25, 50, 100)
- **Informações**: Total de registros e páginas
- **Performance**: Carregamento sob demanda

### Uso Programático

```java
PaginationController controller = new PaginationController(10); // 10 itens por página
PaginationResult<Livro> result = controller.getPage(1, livroService.getAllLivros());

// Navegação
controller.nextPage();
controller.previousPage();
controller.goToPage(5);
```

## 🛠️ Melhorias Técnicas

### Tratamento de Exceções

```java
try {
    // Operação de banco
} catch (DatabaseException e) {
    // Tratamento específico para erros de banco
    logger.error("Erro de banco: " + e.getMessage());
}
```

### Factory Pattern para Conexões

```java
Connection conn = ConnectionFactory.getConnection(DatabaseType.POSTGRESQL);
```

### Service Layer

```java
LivroService service = new LivroService();
List<Livro> livros = service.buscarPorAutor("Machado de Assis");
```

## 🐳 Docker

### PostgreSQL

```yaml
version: "3.8"
services:
  postgres:
    image: postgres:13
    environment:
      POSTGRES_DB: livros
      POSTGRES_USER: livros_user
      POSTGRES_PASSWORD: livros_pass
    ports:
      - "5432:5432"
```

### MySQL

```yaml
version: "3.8"
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: livros
      MYSQL_USER: livros_user
      MYSQL_PASSWORD: livros_pass
      MYSQL_ROOT_PASSWORD: root_pass
    ports:
      - "3306:3306"
```

## 📝 Scripts Disponíveis

- `start-postgresql.sh`: Inicia PostgreSQL via Docker
- `start-mysql.sh`: Inicia MySQL via Docker
- `stop-postgresql.sh`: Para PostgreSQL
- `stop-mysql.sh`: Para MySQL
- `compile-and-run.sh`: Compila e executa a aplicação

## 🔍 Testes

O sistema inclui verificações automáticas de:

- Conexão com banco de dados
- Validação de configurações
- Testes de paginação
- Verificação de interface

## 📈 Performance

### Otimizações Implementadas

- **Lazy Loading**: Carregamento sob demanda de dados
- **Connection Pooling**: Reutilização de conexões
- **Paginação eficiente**: Queries otimizadas com LIMIT/OFFSET
- **Cache de configurações**: Configurações carregadas uma vez

## 🤝 Contribuição

Para contribuir com o projeto:

1. Faça um fork do repositório
2. Crie uma branch para sua feature
3. Implemente as melhorias
4. Teste thoroughly
5. Submeta um pull request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

## 🆘 Suporte

Para suporte e dúvidas:

- Abra uma issue no GitHub
- Consulte a documentação técnica
- Verifique os logs de erro

---

**Versão**: 2.0  
**Data**: 2025  
**Autor**: Sistema Melhorado  
**Status**: Produção Ready ✅
