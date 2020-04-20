# COVID19_Backend
Repositorio para el desarrollo del backend

Antes que nada [date una Vuelta por la WIKI](https://github.com/TACSGrupo3/COVID19_Backend/wiki)

## Arquitectura de la Api: 
Lenguaje: Java 8

Frameworks Usados:

-SpringBoot

-Maven

# Como correr la Api con Docker
Prerequisitos:
-Maven
-Docker Desktop

1) Descargar el Repo de COVID19_Backend
2) Abrir una consola de comandos y (parado en el proyecto) compilar con maven: mvn clean install
3) Ejectuar los siguientes comandos:
> docker build -t covid19_grupo3/api .

> docker run -p 8080:8080 -t covid19_grupo3/api


## Endpoints de los servicios:
Publicados en : https://documenter.getpostman.com/view/11044257/Szf6Z9Z9
