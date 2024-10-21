# Connecting Docker and PostgreSQL
These are the steps I took to Dockerize the Spring Boot Application & Postgres Database. First I started the Docker
Engine by opening the Docker Desktop for Windows. 

## Step 1: Creating the PostgreSQL container
If I did not already have the postgres image, I could pull it from the Docker Hub using the command `docker pull postgres`

After I created an empty folder called `feedapp` inside `C:/data`, I ran `postgreSQL` by issuing the command

```
docker run -p 5432:5432 \
           -e POSTGRES_PASSWORD=postgres \
           -e POSTGRES_USER=postgres \
           -e POSTGRES_DB=postgres_db \
           -v C:/data/postgres_data:/var/lib/postgresql/data \
           --name=postgres_container \
           -d \
           postgres
```
or the one-liner 

```
docker run -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=postgres_db -v C:/data/postgres_data:/var/lib/postgresql/data --name=postgres_container -d postgres``` 
```
The arguments in this command are:
``` 
- p:                    Specify hostport:containerport
- e POSTGRES_PASSWORD:  Specifies a password for admin user
- e POSTGRES_USER:      Specifies a user with superuser privileges and a database with the same name
- e POSTGRES_DB:        Specifies a name for the database
- v:                    Mount a local folder to the container
-- name:                Specify container name
- d:                    Specify that the contaiener should be ran in detatched mode 
                        (keep running in the background and not exit right away)
```
To look at the newly created container, I ran the command 
```
$ docker ps
CONTAINER ID   IMAGE      COMMAND                  CREATED          STATUS          PORTS                    NAMES
739e3814c85b   postgres   "docker-entrypoint.s…"   14 seconds ago   Up 13 seconds   0.0.0.0:5432->5432/tcp   postgres_container
```
## Step 2: Creating the Spring Boot Application Image
I modified `applicatoin.properties` to set up the containers for the application- and postgreSQL container. Instead of
using the url `jdbc:postgresql://localhost:5432/postgres_db` for the database, I had to use the exact IP-address, and to 
find IP-address for the host, I ran `docker inspect container_name/container_id`, i.e. 

`docker inspect 739e3814c85b`