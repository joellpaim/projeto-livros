#!/bin/bash

echo "=== Parando PostgreSQL ==="

cd postgres-livros

docker-compose down

echo "✅ PostgreSQL parado!"

