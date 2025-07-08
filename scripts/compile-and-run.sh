#!/bin/bash

# Script para compilar e executar a aplicação Java

DB_TYPE=${1:-postgresql}

echo "=== Compilando e Executando Aplicação ==="
echo "Banco de dados: $DB_TYPE"

# Verifica se o tipo de banco é válido
if [[ "$DB_TYPE" != "postgresql" && "$DB_TYPE" != "mysql" ]]; then
    echo "❌ Tipo de banco inválido: $DB_TYPE"
    echo "Use: postgresql ou mysql"
    exit 1
fi

# Navega para o diretório raiz do projeto
cd "$(dirname "$0")/.."

# Cria diretório de classes se não existir
mkdir -p classes

echo "📦 Compilando código Java..."

# Compila o código
if [[ "$DB_TYPE" == "mysql" ]]; then
    # Para MySQL, precisa do driver MySQL
    if [[ ! -f "mysql-connector-j-9.3.0.jar" ]]; then
        echo "⬇️ Baixando driver MySQL..."
        wget -q https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-j-9.3.0.tar.gz
        tar -xzf mysql-connector-j-9.3.0.tar.gz
        cp mysql-connector-j-9.3.0/mysql-connector-j-9.3.0.jar .
        rm -rf mysql-connector-j-9.3.0*
    fi
    
    javac -cp ".:mysql-connector-j-9.3.0.jar" -d classes src/combo/**/*.java
    CLASSPATH=".:mysql-connector-j-9.3.0.jar:classes"
else
    # Para PostgreSQL
    javac -cp ".:postgresql-42.2.4.jar" -d classes src/combo/**/*.java
    CLASSPATH=".:postgresql-42.2.4.jar:classes"
fi

if [[ $? -eq 0 ]]; then
    echo "✅ Compilação concluída com sucesso!"
    echo ""
    echo "🚀 Executando aplicação..."
    echo "Tipo de banco: $DB_TYPE"
    echo ""
    
    # Executa a aplicação
    java -cp "$CLASSPATH" combo.principal.Principal "$DB_TYPE"
else
    echo "❌ Erro na compilação!"
    exit 1
fi

