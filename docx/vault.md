### documentation - https://www.vaultproject.io/docs/configuration/storage

#### Всю информацию, которую нужно сохранить в тайне, называем секретом. Это создает проблему с хранением: в репозитории хранить плохо, в зашифрованном виде — нужно где-то держать ключи шифрования.

#### HashiCorp Vault — одно из неплохих решений проблемы.

* Безопасно хранит и управляет ключами.
* Заточен на мир микросервисов, так как сам по себе микросервис
* В HashiCorp Vault много сделано для аутентификации и авторизации доступа к секретам, например, ACL и принцип минимальных привилегий
* REST интерфейс с JSON.
* Безопасность не идеальна, но на достаточно высоком уровне.


## Использование учетных данных динамической базы данных
После установки vault :
* vault secrets enable database (включение секретного механизма для бд)
* создание ресурса конфигурации бд:
~~~
vault write database/config/psql-blog -<<EOF
{
  "plugin_name":"postgresql-database-plugin",
  "connection_url":"postgresql://{{username}}:{{password}}@localhost:5432/",
  "allowed_roles":"*", 
  "username":"username",
  "password":"password"
}
EOF
~~~
* настройка роли базы данных vault: (!!! db_name - Имя существующей конфигурации базы данных,не путать с названием бд!!!)
~~~
vault write database/roles/blog-accounts-ro \
    db_name=psql-blog \
    creation_statements="CREATE ROLE \"{{name}}\" WITH LOGIN PASSWORD '{{password}}' VALID UNTIL '{{expiration}}'; \
        GRANT SELECT ON ALL TABLES IN SCHEMA public TO \"{{name}}\";" \
    default_ttl="1h" \
    max_ttl="24h"
~~~
* Создание динамических учетных данных:
~~~
vault read database/creds/blog-accounts-ro
~~~

Префикс database/creds используется для создания учетных данных для доступных ролей. Поскольку мы использовали  роль blog-accounts-ro  , возвращаемые имя пользователя/пароль будут ограничены для выбора операций.

###  Тестирование:
~~~
sudo -i -u postgres
~~~
~~~
psql -h 127.0.0.1 --u usertoken -d myblog
~~~
#### Для удобства стоит написать bash-script для конфигурации

### Настройка постоянного хранилища HTTPS
~~~
storage "file" {
  path = "./vault-data"
}
listener "tcp" {
  address = "127.0.0.1:8200"
  tls_cert_file = "./src/test/vault-config/localhost.cert"
  tls_key_file = "./src/test/vault-config/localhost.key"
}
~~~

~~~
export VAULT_ADDR=https://localhost:8200
export VAULT_CACERT=/home/nikita/cert/localhost/localhost.crt
vault operator init
~~~

Здесь мы определили несколько переменных среды, поэтому нам не нужно каждый раз передавать их в Vault в качестве параметров.

* VAULT_ADDR :  базовый URI, по которому наш сервер API будет обслуживать запросы.

* VAULT_CACERT : Путь к открытому ключу сертификата нашего сервера.

В нашем случае мы используем VAULT_CACERT  , поэтому мы можем использовать HTTPS для доступа к API Vault. Нам это нужно, потому что мы используем самоподписанные сертификаты. В этом нет необходимости для прода, где у нас обычно есть доступ к сертификатам, подписанным CA.

После выполнения вышеуказанной команды мы должны увидеть такое сообщение:
~~~
Unseal Key 1: <key share 1 value>
Unseal Key 2: <key share 2 value>
Unseal Key 3: <key share 3 value>
Unseal Key 4: <key share 4 value>
Unseal Key 5: <key share 5 value>

Initial Root Token: <root token value>

... more messages omitted
~~~
#### !Пять первых строк — это общие общие ключи, которые мы позже будем использовать для вскрытия хранилища Vault. Обратите внимание, что Vault отображает общий доступ к мастер-ключу только во время инициализации — и никогда больше. Примите к сведению и сохраните их в безопасности, иначе мы потеряем доступ к нашим секретам после перезапуска сервера!

Также нужно обратить внимание на корневой токен , так как он понадобится позже. В отличие от незапечатанных ключей, корневой токен можно легко сгенерировать позднее , поэтому его можно безопасно уничтожить после завершения всех задач настройки. Поскольку нужно вводить команды, для которых требуется токен аутентификации, сохраним корневой токен в переменной среды:
~~~
export VAULT_TOKEN=<root token value> (Unix/Linux)
~~~
можно посмотреть состояние сервера:
~~~
vault status
~~~
Мы увидим:
~~~
Key                Value
---                -----
Seal Type          shamir
Sealed             true
Total Shares       5
Threshold          3
Unseal Progress    0/3
Unseal Nonce       n/a
Version            0.10.4
HA Enabled         false
~~~
Можно увидеть, что Vault все еще запечатан. Мы также можем следить за ходом распечатывания: «0/3».
~~~
 vault operator unseal <key share 1 value>
 vault operator unseal <key share 2 value>
 vault operator unseal <key share 3 value>
~~~
после введения всех трех ключей статус Sealed изменится на false.

Далее введем:
~~~
vault secrets enable -path=secret/ kv
~~~

### Создание сертификатов localhost.crt и localhost.key
~~~
$ mkdir cert
$ cd cert
$ mkdir CA
$ cd CA
$ openssl genrsa -out CA.key -des3 2048
~~~
Затем мы создадим корневой сертификат CA, используя сгенерированный ключ, который в нашем случае будет действовать в течение десяти лет. Будет запрошена парольная фраза для ключа и информация о сертификате. Нужно ввести желаемую информацию о сертификате или оставить ее по умолчанию.
~~~
openssl req -x509 -sha256 -new -nodes -days 3650 -key CA.key -out CA.pem
~~~
На данный момент в нашем cert/CA folder, у нас есть два файла, CA.keyи CA.pem.
#### Генерация сертификата
Далее в cert/CA каталоге создайте новый каталог, localhost. Внутри localhostсоздайте новый файл localhost.ext.
~~~
mkdir localhost
cd localhost
touch localhost.ext
~~~
Информация, которую необходимо записать в подписанный SSL-сертификат, будет содержаться в этом localhost.extфайле.
Файл будет выглядеть следующим образом:
~~~
authorityKeyIdentifier = keyid,issuer
basicConstraints = CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
subjectAltName = @alt_names

[alt_names]
DNS.1 = localhost
IP.1 = 127.0.0.1
~~~
#### !Сертификат будет работать для локального хоста, а также для 127.0.0.1. Можно добавить в файл больше доменов или IP-адресов, но обязательно отредактировать файл /etc/hosts, чтобы эти домены указывали на локальный компьютер (127.0.0.1)!

Далее нужно сгенерировать ключ и использовать его для создания CSR (запроса на подпись сертификата) с помощью приведенной ниже команды.
~~~
openssl genrsa -out localhost.key -des3 2048
~~~
Создание CSR:
~~~
openssl req -new -key localhost.key -out localhost.csr
~~~
Теперь с этим CSR можно запросить CA подписать сертификат, как показано ниже. Пути к файлам CA.key и CA.pem зависят от того, откуда пользователь запускает команды. В этом случае приведенные ниже команды запускаются из /cert/CA/localhost.
~~~
openssl x509 -req -in localhost.csr -CA ../CA.pem -CAkey ../CA.key -CAcreateserial -days 3650 -sha256 -extfile localhost.ext -out localhost.crt
~~~
Серверу потребуется файл сертификата localhost.crt и расшифрованный ключ, поскольку наш localhost.key находится в зашифрованном виде.
Расшифровываем localhost.key и сохраняем:
~~~
openssl rsa -in localhost.key -out localhost.decrypted.key
~~~

### Resolving javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: PKIX path building failed Error
* Необходимо добавить сертификат в файл хранилища доверенных сертификатов используемой JVM , расположенный по адресу $JAVA_HOME\lib\security\cacerts.

* Сначала можно проверить, находится ли сертификат в хранилище доверенных сертификатов, выполнив следующую команду:
~~~ 
keytool -list -keystore "$JAVA_HOME/jre/lib/security/cacerts"
~~~
* Если сертификат отсутствует, можно получить его, загрузив его с помощью браузера и добавив в хранилище доверенных сертификатов с помощью следующей команды:
~~~
keytool -import -noprompt -trustcacerts -alias <AliasName> -file   <certificate> -keystore <KeystoreFile> -storepass <Password>
~~~
Пример:
~~~
keytool -import -noprompt -trustcacerts -alias myFancyAlias -file /path/to/my/cert/myCert.cer -keystore /path/to/my/jdk/jre/lib/security/cacerts/keystore.jks -storepass changeit
~~~