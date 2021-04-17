REST service, that provides content to user by sorting it via UCB1 algorithm. (Multi-Armed Bandit algorithm)  
Spring Boot, Kotlin

Assumptions and restrictions ...

#### Environment preparation

TODO: wrap it into docker compose ?

Start a PostgreSQL instance:

```
docker run -d \
    --name postgres-content \
    -e POSTGRES_USER=ADMIN \
    -e POSTGRES_PASSWORD=SECRET \
    -e POSTGRES_DB=spring_boot_content \
    -e PGDATA=/var/lib/postgresql/data/pgdata \
    -v /opt/postgresql_volume/:/var/lib/postgresql/data \
    -p 5432:5432 \
    postgres:13.2
```

#### TODO

- be able to register events not only via REST, but via other messaging system (Kafka) ?
- unit test for algorithm (verify probability distribution)
- performance test for entire service

