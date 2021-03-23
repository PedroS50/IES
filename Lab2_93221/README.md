# 2.1
### Inicializar Tomcat:
cd apache-tomcat-9.0.39/bin 
./startup.sh

### Depois de inicializado, fica disponível no endereço:
http://localhost:8080

### Para aceder ao sistema de gestão de aplicações (deploy, undeploy) ir a:
http://localhost:8080/manager

### Para criar uma aplicação aplicação web default em maven é usado o comando:
mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-webapp -DarchetypeVersion=1.4

Após o build do projeto é criado um ficheiro maven-archetype-webapp.war.
Os ficheiros .war (Web ARchive) contém toda a informação necessária da aplicação para que esta possa ser deployed
através do tomcat, damos upload do ficheiro e deploy através do Tomcat manager.
Também existe a possibilidade de dar deploy através do vscode, ficheiro .war -> Run on Tomcat Server.

Ao entrar no endereço http://localhost:8080/maven-archetype-webapp acedemos à aplicação.

### Comando utilizado para ver o log do servidor:
tail logs/catalina.out

### Notas sobre adicionar a aplicação java:
Colocar ficheiros com código java no path src/main/java
Para aceder à aplicação ir a http://localhost:8080/maven-archetype-webapp/CalendarServlet, sendo que o ficheiro CalendarServlet.java, classe CalendarServlet.

### Para receber dados passados como parametro no http request:
String username = request.getParameter("username");
Adicionar ?username=XXXX ao url.

### !! Excerto do server log que anuncia o sucesso no lançamento da aplicação !!
29-Oct-2020 20:55:33.916 INFO [http-nio-8080-exec-1] org.apache.catalina.startup.HostConfig.deployWAR Deploying web application archive [/home/pedro/apache-tomcat-9.0.39/webapps/maven-archetype-webapp.war]
29-Oct-2020 20:55:33.958 INFO [http-nio-8080-exec-1] org.apache.catalina.startup.HostConfig.deployWAR Deployment of web application archive [/home/pedro/apache-tomcat-9.0.39/webapps/maven-archetype-webapp.war] has finished in [25] ms

## k)
### Clonar imagem do Tomcat:
docker image pull tomcat:latest

### Criar e inicializar o container com tomcat:
docker container create --publish 8082:8080 --name my-tomcat-container tomcat:latest
docker container ls -a # it will list all the containers
docker container start my-tomcat-container

### Para entrar no diretório do container:
docker container exec -it my-tomcat-container bash

### Construir a imagem da aplicação através do Dockerfile:
docker image build -t pedrosantos/maven-archetype-webapp ./

### Correr app através de:
docker container run -it --publish 8081:8080 pedrosantos/maven-archetype-webapp

# 2.2
Adicionar classe CalendarServlet a EmbeddedJettyExample e adicionar ao servletHandler o nome da classe.
## Ao compilar o programa criava um ficheiro .jar. Solução:
Adicionar '<packaging>war</packaging>' ao pom.xml.

# 2.3
## n)
Aceder a https://start.spring.io/ para criar o projeto base no maven.
Depois de correr o comando ./mvnw spring-boot:run :
 --> 2020-11-02 22:59:49.683  INFO 11231 --- [           main] c.e.s.springdemo.SpringdemoApplication   : Started SpringdemoApplication in 1.569 seconds (JVM running for 1.838)


## o)
Adicionei a dependência do Thymeleaf no pom.xml.
Alterei porta de 8080 para 8090 ao inserir a linha 'server.port=8090' no ficheiro src/main/resources/application.properties.
Se o index.html existir, é utilizado como welcome page a.k.a. página root.

### Tags importantes:
#### No controlador:
--> @Controller antes da declaração da classe GreetingController:
	Lida com os pedidos HTTP
--> @GetMapping("/greeting") antes do método greeting:
	Mapeia os pedidos GET que chegam a /greeting ao método greeting()
--> @RequestParam nos parâmetros de entrada:
	Adiciona parâmetros de entrada ao url, é definido o nome, se é necessário ou não e o valor default
	São definidos da seguinte forma: /greeting?name=WhateverNameYouWant
#### No programa principal:
--> @SpringBootApplication antes da definição da classe
#
# Perguntas/ Respostas
### What are the responsibilities/services of a “servlet container”? 
	Um servlet container deve:
	- Fazer a gestão do LifeCycle, ou seja, carrega as classes, trata da inicialização e gere as instâncias;
	- Gere a comunicação entre o servlet e o servidor web;
	- Gere os pedidos recebidos no servlet criando uma nova thread por pedido e terminando esta quando concluido;
	- Liberta os recursos quando o objeto servlet deve ser destruído.

### Web applications in Java can be deployed to stand-alone applications servers or embedded servers. Elaborate on when to choose one over the other (and relate with Spring Boot).

1. Stand-alone Application Servers - Servidor http corre separadamente da applicação, o servidor realiza o encaminhamento dos pedidos para a aplicação e pode ser responsável por carregar a aplicação.
#### Vantagens:
	-> 	Maior facilidade durante a implantação.
	-> 	Melhor segurança por causa das tarefas executadas e dependências serem mais específicas.
	->	Maior nivel de controlo sobre o servidor e o código da aplicação, o que aumenta a facilidade de adição de novas funcionalidades.

#### Desvantagens:
	->	É necessário fazer manutenção de ambos o servidor e a aplicação.

2. Embedded Application Servers - Servidor http corre no mesmo processo que a aplicação, a aplicação é responsável por iniciar o servidor e por configurar o servidor.
#### Vantagens:
	->	Arquitetura da aplicação mais flexivel.
	->	Maior facilidade em trocar servidores.
	->	Maior facilidade em adicionar funcionalidade sem reiniciar o servidor.
#### Desvantagens:
	->	Maior complexidade durante a implantação visto que é necessário manter o servidor web e a aplicação.

### Explain, in brief, the “dynamics” of Model-View-Controller approach used in Spring Boot to serve web content. (You may exemplify with the context of the previous exercises.)
	O MVC é projetado à volta de um DispatchServlet que expede os pedidos para os handlers, com os mapeamentos configuráveis. O default handler é baseado nas anotações @Controller e @RequestMapping, oferecendo vários métodos de tratamento. O mecanismo @Controller também permite criar Web Apps Restful (como podemos observar na alínea p). Na aplicação desenvolvida conseguimos observar estas propriedades de mapeamento, sendo que o @Controller implementado mapeia o caminho /greeting para a função greeting(). Também foi utilizado o @RequestParam para definir parâmetros de entrada.

### Inspect the POM.xml for the previous SpringBoot projects. What is the role of the “starters” dependencies?
	As dependências starter são utilizadas para adicionar várias depências a partir de uma só, sem ter que as listar individualmente. AAo utilizar starters não é necessário definir versões a utilizar visto que o Spring Boot faz a gestão por si, escolhendo a versão mais apropriada.

### Which annotations are transitively included in the @SpringBootApplication?
	@Configuration: Marca a classe como fonte de definições de beans;
	@EnableAutoConfiguration: Informa o Sping Boot para começar a adicionar beans baseados nas definições da classpath, outros beans e outras propriedades.
	@ComponentScan: Diz ao Spring Boot para procurar outros componentes, configurações e serviços no com/example, permitindo que este encontre os controladores.

## Referências:
1.	https://stephencoakley.com/2017/07/06/embedded-vs-external-web-server
2.	https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/mvc.html
3.	https://www.baeldung.com/spring-boot-starters
