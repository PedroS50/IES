# Pedro Santos
# 93221


#3.1
## a)
@Entity na classe Emplyee é uma anotação do JPA para que o objeto esteja pronto para ser armazenado numa JPA-based data store.
@Id, @GeneratedValue no atributo Id indica que é a primary key e deve ser automáticamente populado pelo JPA provider.

Após correr a PayloadApplication:
2020-11-05 10:20:47.584  INFO 9961 --- [main] payroll.LoadDatabase: Preloading Employee{id=1, name='Bilbo Baggins', role='burglar'}
2020-11-05 10:20:47.586  INFO 9961 --- [main] payroll.LoadDatabase: Preloading Employee{id=2, name='Frodo Baggins', role='thief'}

### ----- Nota Importante ----- ### 
* **Warning no VsCode**
The compiler compliance specified is 1.7 but a JRE 11 is used vscode

* **Solução**
Adicionar...
<properties>
	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	<maven.compiler.release>11</maven.compiler.release>
</properties>
... a todos os Pom.xml, right click no Pom.xml e "Update Project Configuration".
### --------------------------- ###

## REST vs RPC
* RPC (Remote Procedure Call)
É uma forma de descrever um mecanismo que permite fazer uma chamada noutro processo atrás de troca de mensagens.
Envolve gerar métodos que fazem a chamada parecer local mas o pedido é enviado ao processo servidor.
O servidor processa o pedido e envia a resposta ao cliente.
HTTP é um protocolo usado para troca de mensagens, RPC não está restringido a HTTP.

* REST (Representational State Transfer)
É uma arquitetura que impõe certas restrições na interface para atingir determinados objetivos.
Obriga a um modelo cliente/ servidor, em que o cliente pretende obter informação e agir sobre determinados recursos geridos pelo servidor.
O servidor informa o cliente dos recursos ao fornecer representações de recursos e disponibilizando ações para obter novas representações dos recursos ou para os manipular.
Toda a comunicação entre o cliente e o servidor deve ser 'stateless' e 'cachable'.

1. Stateless: Os requests devem conter toda a informação necessária para serem percebidos pelo servidor. Não devem ser dependentes de requests anteriores. Armazenar estados de sessões no servidor quebra este atributo. Os estados deve ser geridos unicamente pelo cliente.
2. Cacheable: Constrições de cache requerem que a informação dentro de uma resposta a um pedido seja marcada como cacheable ou non-cacheable. Se uma resposta for cacheable, a cache do cliente tem a possibilidade de reutilizar a informação para futuros pedidos semelhantes.

### Spring HATEOAS
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-hateoas</artifactId>
</dependency>

* Fornece ferramentas para definir um serviço RESTful e renderizar este num formato aceitável para ser consumido pelo cliente;
* Adicionar links para realizar operações relevantes é fundamental para serviços RESTful. (ex.: /employees/{id} para aceder a um employee pelo id);
* O return type é **EntityModel<Employee>**, que parte de um contentor genérico do Spring HATEOAS;
* **linkTo((...).one(id)).withSelfRel()** pede ao Spring HATEOAS que  crie um link para o método one() do EmployeeController e marca-o como um self link;
* **linkTo((...).all()).withSelfRel("employees")** pede ao Sping HATEOAS que crie um link para o aggregate root, all(), e dar-lhe o nome "employees";
* **CollectionModel<Employee>** é outro contentor permite encapsular coleções. Permite incluir links;
* Encapsular coleções = Encapsular coleções de recursos de employees, por isso é que vamos buscar todos os employees e os transformamos numa lista de objetos EntityModel<Exployee>;
* No final, cada membro individual tem informação sobre si mesmo e links relacionados.

### ----- NOTA ----- ###

Para o resultado do curl não ser ilegível, adicionar '**| json_pp**' ao comando curl. 

### ---------------- ###

## b)
1. List all:
curl -v localhost:8080/employees
2. List one:
curl -v localhost:8080/employees/{id}
3. Insert:
curl -v -X POST localhost:8080/employees -H 'Content-Type:application/json' -d '{"name": "Gandalf the Pink", "role": "stylist"}'
4. Update:
curl -v -X PUT localhost:8080/employees/3 -H 'Content-Type:application/json' -d '{"name": "Samwise Gamgee", "role": "ring bearer"}
Atualiza os campos do employee com id 3.
5. Delete:
curl -v -X DELETE localhost:8080/employees/3
Elimina o employee com id 3.

## c) What happens to your data when the application is stopped and restarted? How could you change that behavior?
Quando a aplicação é reiniciada os dados até então inseridos são perdidos. Isto acontece porque, a base de dados está diretamente ligada à aplicação. Para corrigir isto, teríamos de separar os dois componentes, utilizar uma base de dados a correr separadamente e inserir lá os dados. Outro método seria armazenar os dados json num ficheiro e carregar estes ao iniciar a aplicação porém esta solução não tem escalabilidade, o ato de carregar e armazenar os dados seria muito demorado com uma quantidade significativa de dados.

## Perguntas/ Respostas
### What would be the proper HTTP Status code to get when searching an API for non-existent http://localhost:8080/employees/987987 ?
Código HTTP de estado apropriado: 404

### Be sure to provide evidence (e.g.: screenshots, JSON results view) that you have successfully used the API to insert new entries.
Após usar os comandos de inserção/ atualização acima:
{"_embedded":{"employeeList":[{"id":1,"name":"Bilbo Baggins","role":"burglar","_links":{"self":{"href":"http://localhost:8080/employees/1"},"employees":{"href":"http://localhost:8080/employees"}}},{"id":2,"name":"Frodo Baggins","role":"thief","_links":{"self":{"href":"http://localhost:8080/employees/2"},"employees":{"href":"http://localhost:8080/employees"}}},{"id":3,"name":"Samwise Gamgee","role":"ring bearer","_links":{"self":{"href":"http://localhost:8080/employees/3"},"employees":{"href":"http://localhost:8080/employees"}}},{"id":4,"name":"Gandalf the Pink","role":"stylist","_links":{"self":{"href":"http://localhost:8080/employees/4"},"employees":{"href":"http://localhost:8080/employees"}}}]},"_links":{"self":{"href":"http://localhost:8080/employees"}}}

### Create a layered architecture view (UML diagram), displaying the key abstractions in the solution, in particular: entities, repositories and REST controllers.
Ver UMLEx1.png

### Describe the role of the elements modeled in the previous point

1. Employee (Entidade) - Classe usada para representar um employee. Contém anotações como **@Data** que cria métodos de get, set, equals, hash e toString, @Entity que torna o objeto pronto para ser armazenado em JPA e @Id que informa a base ded dados que o respetivo atributo é a primary key.
2. EmployeeRepository (Repositório) - Adiciona suporte para adição, atualização, eliminação e procura de employees. Extende o JpaRepository e especifica o domínio como Employee e o id do tipo Long.
3. EmployeeController (Controlador) - Mapeia os pedidos HTTP aos respetivos métodos de gestão de dados do repositório.
4. PayrollApplication (Entidade) - Contém a anotação @SpringBootApplication que chama a autoconfiguração e scan de componentes do SpringBoot. 
5. LoadDatabase (Entidade) - Cria duas entidades e armazena-as na base de dados. É também criado um objeto Logger que permite fazer o log das instâncias do tipo Employee.
6. EmployeeNotFoundAdvice (Entidade) - Utilizada para tratar da geração de erros ao não encontrar um employee após um pedido falhado. Retorna uma página de erro 404 com informação sobre o porquê da falha do pedido.
7. EmployeeNotFoundException (Entidade) - Extensão da classe 6. Retorna informação sobre a falha no pedido.
 
# 3.2

## Depois de criados os ficheiros e configurado o projeto, resultado de ./mvnw spring-boot:run :
2020-11-07 13:07:19.673  INFO 7368 --- [  restartedMain] DeferredRepositoryInitializationListener : Spring Data repositories initialized!
2020-11-07 13:07:19.683  INFO 7368 --- [  restartedMain] s.s.Springboot2JpaCrudExampleApplication : Started Springboot2JpaCrudExampleApplication in 3.457 seconds (JVM running for 3.985)

## Inserção de dados:
curl -v -X POST localhost:8080/api/v1/employees -H 'Content-Type:application/json' -d '{"firstName": "Charles", "lastName": "Xavier", "emailId": "mindreader@gmail.com"}'
curl -v -X POST localhost:8080/api/v1/employees -H 'Content-Type:application/json' -d '{"firstName": "Hugh", "lastName": "Jackman", "emailId": "kittyclaws@gmail.com"}'
curl -v -X POST localhost:8080/api/v1/employees -H 'Content-Type:application/json' -d '{"firstName": "James", "lastName": "Bond", "emailId": "doubleohseven@gmail.com"}'

## Resultado de curl -v localhost:8080/api/v1/employees?email=mindreader@gmail.com :

[{"id" : 17,"lastName" : "Xavier","firstName" : "Charles","emailId" : "mindreader@gmail.com"}]

## Anotações e significados:
* @Column: Especifica o mapeamento entre o atributo de uma entidade e uma coluna de uma tabela da BD;
* @Table: Especifica a tabela de uma certa entidade;
* @Id: Especifica o identificador de uma entidade. Todas as entidades devem ter Ids.
* @AutoWired: 
   1. Em métodos set para não ser necessário definir elementos <property> no ficheiro de configuração XML. Ao se deparar com esta anotação, o Spring tenta realizar autowiring byType (especifica autowiring por tipo de propriedade) no método.
   2. Em propriedades é utilizado em vez de métodos set. Ao passar valores de propriedades autowired com <property>, os valores são atribuídos à propriedade.
   3. Em construtores indica que o construtor deve ser autowired ao criar um bean, mesmo que nenhum elemento <constructor-arg> seja usado ao configurar o bean no ficheiro XML.
 
# 3.3
## Mais anotaçoes e significados:
* @PostMapping: Sinaliza que o método gere pedidos POST. Quando chega um pedido POST, o método é chamado
* @GetMapping: Sinaliza que o método gere pedidos GET.
(Anotações presentes no exercicio 3.2, também existem 2 especificas para os pedidos PUT e DELETE.)

## No Controller:
O objeto Model é fornecido pelo Spring e é passado ao controlador. Este objeto pode ser configurado através do método addAttribute(key, value), key é a chave pela qual o value pode ser acedido.

* model.addAttribute("submitted", true) -> Atributo definido para dar feedback ao receber dados.

## Nos templates html Thymeleaf:
* Se ' xmlns:th="http://www.thymeleaf.org" ' não for usado, a template não funciona. 
* th:action define a ação que deve acontecer quando o form é submetido
* *{} é utlizado para se referir a objetos passados à template. ${} é utilizado para fazer referência a campos do objeto do form.
* class="result_message" é utilizado para mostrar uma mensagem de confirmação. Apenas é mostrada ao submeter devido ao th:if.

## Configuração da BD:
* @Query permite definir queries JPQL que são executados quando um método é chamado.

## p)
Adicionar método getId() e adicionar a coluna no template em html.

## Perguntas/ Respostas
### Explain the differences between the RestController and Controller components used in this sample.
O Controller marcado pela anotação @Controller permite mapear os urls à respetiva view, pelo programa implementado, /issuereport -> issuereport_form, /issue -> issuereport_list. O componente RestController por outro lado, retorna objetos JSON que são utilizados como resposta ao pedido HTTP.

### Both the RestController and the Controller need to access the database, using a Repository. How do they get a valid instance of the Repository to work with?
Ambos recebem, como argumento de entrada no construtor, um objeto IssueRepository. A classe IssueRepository extende JpaRepository dando assim acesso à BD.
