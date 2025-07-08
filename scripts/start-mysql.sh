#!/bin/bash

echo "=== Iniciando MySQL ==="

# Navega para o diretório do MySQL
cd mysql-livros

# Para containers existentes (se houver)
echo "Parando containers existentes..."
docker-compose down

# Libera redes antigas não utilizadas
echo "Limpando redes não utilizadas..."
docker network prune -f

# Inicia o MySQL
echo "Iniciando MySQL..."
docker-compose up -d

# Aguarda o banco ficar disponível
echo "Aguardando MySQL ficar disponível..."
sleep 15

# Verifica se está rodando
if docker ps | grep -q mysql_livros; then
    echo "✅ MySQL iniciado com sucesso!"
    echo "📊 Banco de dados: livros"
    echo "🔗 Host: localhost:3306"
    echo "👤 Usuário: livros_user"
    echo "🔑 Senha: livros_pass"
    echo "🌐 phpMyAdmin: http://localhost:8080"
    
    # Testa a conexão
    echo ""
    echo "Testando conexão..."
    docker exec mysql_livros mysql -u livros_user -plivros_pass -e "SELECT 'Conexão OK!' as status;"
    
    echo ""
    echo "Para parar: ./scripts/stop-mysql.sh"
else
    echo "❌ Erro ao iniciar MySQL"
    echo "Verificando logs..."
    docker-compose logs
fi

