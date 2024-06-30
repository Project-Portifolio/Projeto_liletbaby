# Projeto, LiletBaby.

## Notas:
Versão do Spring Security utilizada: _6.3.0_<br>
Utilizamos dois sistemas de autenticação, JWT `auth0`

```properties
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>4.4.0</version>
</dependency>
```

E estamos usando `OAuth2` da Google, limitando sua resposta (Página de login) a um `único` endpoint.<br>

```properties
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
<!-- && google API client -->
<dependency>
    <groupId>com.google.api-client</groupId>
    <artifactId>google-api-client</artifactId>
    <version>2.6.0</version>
</dependency>
```

Para maiores detalhes, [confira aqui](https://github.com/Project-Portifolio/Projeto_liletbaby/blob/main/back-end/src/main/java/br/com/liletbaby/back_end/security/WebSecurityConfig.java)
