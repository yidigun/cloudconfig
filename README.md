# Spring Cloud Config Server

## Build Jar

```shell
mvn package -Dmaven.test.skip=true
```

## Run Jar

```shell
java -jar cloudconfig.jar \
  --backend=git \
  --port=8888 \
  --git.uri=http://github.com/path-to/my-config.git \
  --git.username={username or private token} \
  --git.password={password or private token} \
  --git.branch=master \
  --git.basedir=file:./my-config \
  --git.search-paths={profile}/{application}
```

### Command line options

See [application.yaml](main/src/resources/application.yaml) property configuration file
and [Spring Cloud Config Documentation](https://cloud.spring.io/spring-cloud-config/reference/html/).

| Option        | Spring Property                                    | Default/Required           |
|---------------|----------------------------------------------------|----------------------------|
| --backend     | spring.profiles.active                             | Required                   |
| --port        | server.port                                        | 8080                       |
| --git.uri     | spring.cloud.config.server.git.uri                 | Required if backend=git    |
| --git.*       | spring.cloud.config.server.git.*                   |                            |
| --git.url     | alias for (--git.uri)                              |                            |
| --native.path | spring.cloud.config.server.native.search-locations | Required if backend=native |
| --native.*    | spring.cloud.config.server.native.*                |                            |

## Build Docker Image

## Run Docker Container

