### Запуск Spring Boot с PostgreSQL в Docker Compose

* Прежде чем мы сможем запустить PostgreSQL с Docker Compose, нужно превратить наше приложение Spring Boot в образ Docker . Первый шаг — упаковать приложение в виде файла JAR
~~~
./mvnw clean package -DskipTests
~~~
Очищаем наши предыдущие сборки перед упаковкой приложения. Пропускаем тесты, потому что они не работают без PostgreSQL

* Создадать новый каталог src/main/docker
* После этого копировать туда JAR-файл приложения
~~~
cp target/docker-spring-boot-postgres-0.0.1-SNAPSHOT.jar src/main/docker
~~~

* Cоздать этот Dockerfile в том же каталоге
~~~
FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
~~~
* Создать файл Docker Compose, docker-compose.yml , и сохранить его в src/main/docker
~~~
version: '2'

services:
  app:
    image: 'docker-spring-boot-postgres:latest'
    build:
      context: .
    container_name: app
    depends_on:
      - db
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/compose-postgres
      - SPRING_DATASOURCE_USERNAME=compose-postgres
      - SPRING_DATASOURCE_PASSWORD=compose-postgres
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
          
  db:
    image: 'postgres:13.1-alpine'
    container_name: db
    environment:
      - POSTGRES_USER=compose-postgres
      - POSTGRES_PASSWORD=compose-postgres
~~~

#### Приложение называется  app. Это первая из двух служб
* Образ Spring Boot Docker имеет имя  docker-spring-boot-postgres:latest (строка 5). Docker создает этот образ из файла Dockerfile в текущем каталоге
* Имя контейнера — app . Это зависит от службы БД . Вот почему он начинается после контейнера db
* Приложение использует контейнер db PostgreSQL в качестве источника данных . Имя базы данных, имя пользователя и пароль — все compose-postgres
* Hibernate автоматически создаст или обновит все необходимые таблицы базы данных

#### База данных PostgreSQL имеет имя db и является вторым сервисом
* Используем PostgreSQL 13.1
* Имя контейнера — db
* Имя пользователя и пароль —  compose-postgres

#### Запуск Spring Boot & PostgreSql
~~~
docker-compose up
~~~

#### Остановка приложения
~~~
docker-compose down
~~~