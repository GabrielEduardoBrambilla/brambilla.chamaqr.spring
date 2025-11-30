#!/bin/bash
# ═════════════════════════════════════════════════════════════
# Docker Entrypoint - ChamadaQR Backend
# ═════════════════════════════════════════════════════════════
#
# Responsabilidades:
# 1. Validar certificados SSL
# 2. Configurar variáveis de ambiente
# 3. Verificar conectividade MySQL
# 4. Verificar conectividade Keycloak
# 5. Iniciar Tomcat
#
# ═════════════════════════════════════════════════════════════

set -e

echo "═════════════════════════════════════════════════════════════"
echo " ChamadaQR Backend - Docker Container Initialization"
echo "═════════════════════════════════════════════════════════════"
echo ""

# ═════════════════════════════════════════════════════════════
# 1. VALIDAÇÃO DE CERTIFICADOS SSL
# ═════════════════════════════════════════════════════════════

echo "🔐 [1/5] Validando certificados SSL..."

SSL_DIR="/usr/local/tomcat/conf/ssl"

# Verificar se certificados existem
if [ ! -f "$SSL_DIR/fullchain.pem" ]; then
    echo "❌ ERRO: fullchain.pem não encontrado em $SSL_DIR"
    exit 1
fi

if [ ! -f "$SSL_DIR/wildcard.key" ]; then
    echo "❌ ERRO: wildcard.key não encontrado em $SSL_DIR"
    exit 1
fi

# if [ ! -f "$SSL_DIR/mysql-truststore.jks" ]; then
#     echo "❌ ERRO: mysql-truststore.jks não encontrado em $SSL_DIR"
#     exit 1
# fi

# if [ ! -f "$SSL_DIR/client-keystore.p12" ]; then
#     echo "❌ ERRO: client-keystore.p12 não encontrado em $SSL_DIR"
#     exit 1
# fi

echo "   ✅ fullchain.pem encontrado"
echo "   ✅ wildcard.key encontrado"
# echo "   ✅ mysql-truststore.jks encontrado"
# echo "   ✅ client-keystore.p12 encontrado"
echo ""

# ═════════════════════════════════════════════════════════════
# 2. CONFIGURAÇÃO DE VARIÁVEIS DE AMBIENTE
# ═════════════════════════════════════════════════════════════

echo "⚙️  [2/5] Configurando variáveis de ambiente..."

# Exibir configurações (sem senhas)
echo "   MySQL Host:       $MYSQL_HOST:$MYSQL_PORT"
echo "   MySQL Database:   $MYSQL_DATABASE"
echo "   MySQL User:       $MYSQL_USER"
echo "   Keycloak URL:     $KEYCLOAK_URL"
echo "   Keycloak Realm:   $KEYCLOAK_REALM"
echo "   Keycloak Client:  $KEYCLOAK_CLIENT_ID"
echo "   Server Port:      $SERVER_PORT"
echo ""

# Construir JAVA_OPTS com todas as propriedades
export JAVA_OPTS="$JAVA_OPTS -Dspring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DATABASE}?useSSL=false\u0026allowPublicKeyRetrieval=true -Dspring.datasource.username=${MYSQL_USER} -Dspring.datasource.password=${MYSQL_PASSWORD} -Dkeycloak.auth-server-url=${KEYCLOAK_URL} -Dkeycloak.realm=${KEYCLOAK_REALM} -Dkeycloak.client-id=${KEYCLOAK_CLIENT_ID} -Dkeycloak.client-secret=${KEYCLOAK_CLIENT_SECRET} -Dserver.port=${SERVER_PORT}"

# ═════════════════════════════════════════════════════════════
# 3. VERIFICAR CONECTIVIDADE MYSQL
# ═════════════════════════════════════════════════════════════

echo "🗄️  [3/5] Verificando conectividade com MySQL..."

# Tentar conexão TCP (timeout 5 segundos)
if timeout 5 bash -c "cat < /dev/null > /dev/tcp/${MYSQL_HOST}/${MYSQL_PORT}" 2>/dev/null; then
    echo "   ✅ MySQL acessível em ${MYSQL_HOST}:${MYSQL_PORT}"
else
    echo "   ⚠️  AVISO: MySQL não está acessível"
    echo "   ⚠️  Container continuará, mas pode falhar ao conectar"
fi
echo ""

# ═════════════════════════════════════════════════════════════
# 4. VERIFICAR CONECTIVIDADE KEYCLOAK (VIA HAPROXY)
# ═════════════════════════════════════════════════════════════

echo "🔑 [4/5] Verificando conectividade com Keycloak..."

# Extrair host e porta do KEYCLOAK_URL
KEYCLOAK_HOST=$(echo "$KEYCLOAK_URL" | sed -E 's|https?://([^:/]+).*|\1|')
KEYCLOAK_PORT=443  # HAProxy HTTPS

# Tentar conexão TCP
if timeout 5 bash -c "cat < /dev/null > /dev/tcp/${KEYCLOAK_HOST}/${KEYCLOAK_PORT}" 2>/dev/null; then
    echo "   ✅ HAProxy/Keycloak acessível em ${KEYCLOAK_HOST}:${KEYCLOAK_PORT}"
else
    echo "   ⚠️  AVISO: HAProxy/Keycloak não está acessível"
    echo "   ⚠️  Autenticação pode falhar"
fi
echo ""

# ═════════════════════════════════════════════════════════════
# 5. INFORMAÇÕES FINAIS E INICIALIZAÇÃO
# ═════════════════════════════════════════════════════════════

echo "🚀 [5/5] Iniciando Tomcat..."
echo ""
echo "═════════════════════════════════════════════════════════════"
echo " Container pronto para inicializar!"
echo "═════════════════════════════════════════════════════════════"
echo ""
echo "📌 Health Check Endpoint:"
echo "   curl --insecure https://localhost:8443/chamadaqr/api/message"
echo ""
echo "📌 Logs:"
echo "   docker logs -f <container_name>"
echo ""
echo "═════════════════════════════════════════════════════════════"
echo ""

# ═════════════════════════════════════════════════════════════
# EXECUTAR COMANDO PASSADO (catalina.sh run)
# ═════════════════════════════════════════════════════════════

exec "$@"