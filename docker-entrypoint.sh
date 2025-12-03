#!/bin/bash
set -e

# Converte os certificados PEM para PKCS12 se existirem
if [ -f /opt/tomcat/ssl/fullchain.pem ] && [ -f /opt/tomcat/ssl/wildcard.key ]; then
    echo "Convertendo certificados SSL para PKCS12..."
    openssl pkcs12 -export \
        -in /opt/tomcat/ssl/fullchain.pem \
        -inkey /opt/tomcat/ssl/wildcard.key \
        -out /opt/tomcat/ssl/keystore.p12 \
        -name tomcat \
        -passout pass:changeit
    echo "Certificados convertidos com sucesso!"
else
    echo "AVISO: Certificados SSL não encontrados em /opt/tomcat/ssl/"
    echo "Gerando certificado autoassinado para desenvolvimento..."
    keytool -genkey -noprompt \
        -alias tomcat \
        -dname "CN=localhost, OU=Dev, O=Company, L=City, S=State, C=BR" \
        -keystore /opt/tomcat/ssl/keystore.p12 \
        -storepass changeit \
        -keypass changeit \
        -keyalg RSA \
        -storetype PKCS12 \
        -validity 365
fi

# Executa o comando passado como argumento
exec "$@"