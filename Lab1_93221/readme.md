# Pedro Santos - 93221

# 1.1
## mvn archetype:generate -DgroupId=ua.ies.pedro93221 -DartifactId=myweatherradar -Dversion=1.0-SNAPSHOT
->	Gera os ficheiros necessários para o projeto.

## mvn package
->	É uma phase, executa todos os goals até chegar ao package. Gera um pacote a partir do código compilado

## mvn exec:java -Dexec.mainClass="ua.ies.pedro93221.MyWeatherRadar"
-> 	Executa o programa Java.
	-Dexec.mainClass é utilizado para definir a classe main a ser executada.
	-Dexec.args é utilizado para adicionar argumentos na execução do programa.

## Archetype
Um archetype é um projeto template (modelo) a partir do qual outros projetos podem ser desenvolvidos.

## GroupId
Nome que identifica o desenvolvedor do projeto.

## ArtifactId
Nome que identifica o projeto.

## Maven Goal
No Maven, uma phase é uma sequência de goals, quando corremos uma phase, todos os goals pertencentes a esta são executados. 
Cada goal representa uma tarefa, estes podem estar associados a phases ou não, se não estiverem têm de ser invocados diretamente.

Ex.: A phase package (mvn package) executa os goals: validate, generate-sources, ..., test, package. 

## Principais Goals:
->	Validate, Generate-Sources, Process-Sources, Generate-Resources, Process-Resources, Compile, Process-Classes, generate-test-sources, process-test-sources, generate-test-resources, process-test-resources, test-compile, test, package, pre-integration-test, integration-test, post-integration-test, verify, install, deploy

# 1.2
## git clone https://github.com/PedroS50/IESGitLab93221.git
->	Clonar repositório.

## git add .
->	Adiciona todos os ficheiros locais na working tree para o próximo commit.
	Devido ao .gitignore incluir "/target", essa pasta não será adicionada.
## git commit -m "Initial Commit"
->	Cria um novo commit que contém os ficheiros adicionados.
	-m define a descrição do commit.
## git push
->	Atualiza o repositório com o novo commit.

## git checkout -b search-by-name
->	Cria o branch "search-by-name";

## git pull origin main --rebase 
->	Pull e merge dos ficheiros no branch main (que incluem as alterações do ex 1), com os ficheiros do branch search-by-name local.
### git add .
### git commit -m "Merge search-by-name to main"
### git push
### git checkout main
### git merge search-by-name

->	Ao realizar o rebase do main com o search-by-name, possíveis conflitos podem ser resolvidos localmente, no branch, sem comprometer o código no main. Após resolvidos, é executado o merge para adicionar as alterações ao main-

# 1.3
## c) Passo 2 do tutorial do Docker:
## docker build --tag bulletinboard:1.0 .
-> 	O comando build contrói uma imagem através do processamento do Dockerfile, correndo todos os comandos em sequência. 
	O parâmetro --tag é usado para atribuir um nome e uma tag à imagem no formato nome:tag. 

## docker run --publish 8000:8080 --detach --name bb bulletinboard:1.0 
->	Corre a imagem. 
	--publish é utilizado para publicar a porta do container na máquina hóspede. 
	--detach faz o container correr em segundo plano.
	--name atribui um nome ao contentor.

## docker rm --force bb
-> 	Elimina o container. 
	O parâmetro --force força a paragem do contentor para que este possa ser eliminado.

## docker ps
->	Lista os containers.

## d) Instalar Portainer:
## docker volume create portainer_data
->	Cria um volume no qual os containers podem armazenar dados.

## docker run -d -p 8000:8000 -p 9000:9000 --name=portainer --restart=always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer-ce
->	Corre a imagem.
	-d = --detach = container corre em segundo plano
	-p = --publish = 

## e) Instalar PostgreSQL:
## docker build -t eg_postgresql .
->	Contruir a imagem a partir do Dockerfile que contém os comandos necessários

## docker run --rm -P --name pg_test eg_postgresql
->	Corre a imagem.
	Ao utilizar o parâmetro --rm, o container é automaticamente removido quando este é fechado.
	-P publica todas as portas expostas.

## psql -h localhost -p 32768 -d docker -U docker --password
->	Conectar à base de dados PostreSQL.
	-h, host do servidor da base de dados.
	-p, port do do servidor da base de dados.
	-d, nome da base de dados à qual nos vamos conectar.
	-u, nome de utilizador da bade de dados.
	--password força o prompt que requisita a password.
	Após inserir a password temos acesso à bd.

## $ docker container ls --all
CONTAINER ID        IMAGE                    COMMAND                  CREATED              STATUS                     PORTS                     NAMES
2cfb8871f170        hello-world              "/hello"                 11 minutes ago       Exited (0) 2 seconds ago                             unruffled_bhabha
478d231de7bd        redis:alpine             "docker-entrypoint.s…"   About a minute ago   Up About a minute          6379/tcp                  composetest_redis_1
4d7c28d331c6        composetest_web          "flask run"              About a minute ago   Up About a minute          0.0.0.0:5000->5000/tcp    composetest_web_1
6140ff541f40        eg_postgresql            "/usr/lib/postgresql…"   4 minutes ago  	   Up About a minute          0.0.0.0:32768->5432/tcp   pg_test
a3564f7fa08f        portainer/portainer-ce   "/portainer"             6 minutes ago       Created                                              portainer
3476d5c8884b        bulletinboard:1.0        "docker-entrypoint.s…"   9 minutes ago       Created                                              bb

## Relevância de configurar "volumes":
->	Os volumes são criados para os containers possam retirar e armazenar informação nestes. Estes são importantes para a base de dados fornecendo persistência de dados. Caso o container seja eliminado ou os dados sejam perdidos, os volumes que estão guardados de forma local podem ser utilizados para repôr os dados. Também são ideais para partilhar dados entre containers.