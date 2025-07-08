#!/bin/bash

echo "=== Parando MySQL ==="

cd mysql-livros

docker-compose down

echo "✅ MySQL parado!"

