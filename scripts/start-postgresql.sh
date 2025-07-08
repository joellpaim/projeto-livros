#!/bin/bash

echo "=== Iniciando PostgreSQL ==="

# Navega para o diretório do PostgreSQL
cd postgres-livros

# Para containers existentes (se houver)
echo "Parando containers existentes..."
docker-compose down

# Libera redes antigas não utilizadas
echo "Limpando redes não utilizadas..."
docker network prune -f

# Inicia o PostgreSQL
echo "Iniciando PostgreSQL..."
docker-compose up -d

# Aguarda o banco ficar disponível
echo "Aguardando PostgreSQL ficar disponível..."
sleep 10

# Verifica se está rodando
if docker ps | grep -q postgres_livros; then
    echo "✅ PostgreSQL iniciado com sucesso!"
    echo "📊 Banco de dados: livros"
    echo "🔗 Host: localhost:5432"
    echo "👤 Usuário: livros_user"
    echo "🔑 Senha: livros_pass"
    echo "🌐 pgAdmin: http://localhost:8081 (admin@livros.com / admin123)"
    
    # Testa a conexão
    echo ""
    echo "Testando conexão..."
    docker exec postgres_livros psql -U livros_user -d livros -c "SELECT 'Conexão OK!' as status;"
    
    echo ""
    echo "Para parar: ./scripts/stop-postgresql.sh"
else
    echo "❌ Erro ao iniciar PostgreSQL"
    echo "Verificando logs..."
    docker-compose logs
fi

