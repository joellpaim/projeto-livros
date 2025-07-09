#!/bin/bash
shopt -s globstar

echo "=== Parando MySQL ==="

cd mysql-livros

docker-compose down

echo "✅ MySQL parado!"

