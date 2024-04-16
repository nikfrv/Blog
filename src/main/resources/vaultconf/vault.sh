#!/bin/bash

RED="\e[31m"
GREEN="\e[32m"
ENDCOLOR="\e[0m"

vault server -config ./vaultcfg.hcl &	

sleep 1

 
echo -e "${GREEN}Vault started with config${ENDCOLOR}"

export VAULT_ADDR=https://localhost:8200
export VAULT_CACERT=/home/nikita/cert/CA/localhost/localhost.crt

sleep 1

echo -e "${GREEN}Vault address and cacert was exported${ENDCOLOR}"



vault operator init | awk '{split($0,a,":"); print a[2]}' | sed 's/.//' >> file.txt	

sleep 1

echo -e "${GREEN}Keys was writed in file.txt${ENDCOLOR}"


token=$(sed -n '7p' file.txt | sed '/^$/d') 	


export VAULT_TOKEN=$token

sleep 1

echo -e "${GREEN}TOKEN WAS INSTALLED${ENDCOLOR}"
 
vault operator unseal ` sed -n '1p' file.txt ` 

vault operator unseal ` sed -n '2p' file.txt ` 
  
vault operator unseal ` sed -n '3p' file.txt ` 

sleep 1						

echo -e "${RED}VAULT UNSEALED${ENDCOLOR}"


vault secrets enable database

sleep 1

echo -e "${RED}DATABASE ENABLED${ENDCOLOR}"

vault secrets enable -path=secret/ kv

sleep 1

echo -e "${RED}SECRETS ENABLED${ENDCOLOR}"


vault write database/config/psql-blog -<<EOF
{
  "plugin_name":"postgresql-database-plugin",
  "connection_url":"postgresql://{{username}}:{{password}}@localhost:5432/",
  "allowed_roles":"*", 
  "username":"postgres",
  "password":"sdfgtb123"
}
EOF

vault write database/roles/blog-accounts-ro \
    db_name=psql-blog \
    creation_statements="CREATE ROLE \"{{name}}\" WITH LOGIN PASSWORD '{{password}}' VALID UNTIL '{{expiration}}'; \
        GRANT SELECT ON ALL TABLES IN SCHEMA public TO \"{{name}}\";" \
    default_ttl="5h" \
    max_ttl="24h"

sleep 1

echo -e "${GREEN}DATABASE WAS CONFIGURED${ENDCOLOR}"

vault kv put secret/dbi secret=kv0s mailname=bbotov770@gmail.com mailpass=ugpdgkfdtbxplpve code=hwx12z

sleep 1

echo -e "${GREEN}SECRETS PUT IN DB${ENDCOLOR}"

sleep 1

read -r -p "Do you want to get database data? [Y/n] " input

case $input in
    [yY][eE][sS]|[yY])
	vault read database/creds/blog-accounts-ro
		;;

    [nN][oO]|[nN])
		echo "OK!"
       	;;

    *)
		echo "Invalid input..."
		exit 1
		;;
esac


