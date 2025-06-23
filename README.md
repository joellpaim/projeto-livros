# README - Projeto de Banco de Dados de Livros

## Pré-requisitos

- Docker e Docker Compose instalados
- Java JDK (versão 8 ou superior)
- PostgreSQL JDBC driver (já incluído como `postgresql-42.2.4.jar`)

## Como executar o projeto

### 1. Iniciar o banco de dados PostgreSQL

```bash
# Navegue até a pasta do banco de dados
cd postgres-livros

# Inicie o container do PostgreSQL
docker-compose up -d

# Verifique se o container está rodando
docker ps

# (Opcional) Verifique os logs para acompanhar a inicialização
docker logs postgres_livros
```

O banco de dados estará acessível em:

- Host: `localhost`
- Porta: `5432`
- Banco: `livros`
- Usuário: `livros_user`
- Senha: `livros_pass`

### 2. Compilar e executar a aplicação Java

```bash
# Volte para a pasta raiz do projeto
cd ..

# Compile o projeto
javac -cp ".:postgresql-42.2.4.jar" src/combo/**/*.java

# Execute a aplicação
java -cp ".:postgresql-42.2.4.jar:src" combo.principal.Principal
```

### 3. Parar o banco de dados (quando necessário)

```bash
# Navegue até a pasta do banco de dados
cd postgres-livros

# Pare e remova o container
docker-compose down

# Para remover completamente os dados (cuidado: isso apagará todos os dados)
docker-compose down -v
```

## Estrutura do Projeto

- `postgres-livros/`: Contém a configuração do Docker para o banco de dados
  - `initdb/`: Scripts SQL que são executados na inicialização do banco
- `src/`: Código fonte Java da aplicação
- `postgresql-42.2.4.jar`: Driver JDBC para conexão com PostgreSQL

## Verificando o banco de dados

Para verificar se os dados foram carregados corretamente, você pode conectar diretamente ao banco:

```bash
docker exec -it postgres_livros psql -U livros_user -d livros
```

Dentro do PostgreSQL, execute consultas de teste:

```sql
SELECT count(*) FROM livros;     -- Deve retornar 9999
SELECT count(*) FROM autor;      -- Deve retornar 9999
SELECT count(*) FROM edicao;     -- Deve retornar 11511
SELECT count(*) FROM livroautor; -- Deve retornar 10011
SELECT count(*) FROM livrostemp; -- Deve retornar 999
```

## Solução de Problemas

- Se a aplicação não conseguir conectar ao banco:

  - Verifique se o container está rodando (`docker ps`)
  - Confira os logs do container (`docker logs postgres_livros`)
  - Verifique se as credenciais no código Java correspondem às do docker-compose.yaml

- Se os scripts SQL não forem executados:
  - Verifique se os arquivos na pasta `initdb/` têm extensão `.sql`
  - Confira os logs do container para erros de sintaxe SQL
