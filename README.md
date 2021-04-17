REST service, that gives content sorted by UCB1 algorithm. (Multi-Armed Bandit algorithm)  
Spring Boot, Kotlin

*TODO:* Assumptions and restrictions ...

#### Environment preparation

TODO: wrap it into docker compose ?

1) Run MongoDB

```
docker run --rm --name mongo-server -p 27017-27019:27017-27019 -v /opt/mongodb_volume:/data/db -d mongo
```  

where `/opt/mongodb_volume` - your local data volume for mongo

2) Run service
   ```./gradlew clean bootRun```

3) Endpoints
   - Get list of content, using UCB1 `/play/{userId}`
   - Add new content `/content/add?id={id}&createdOn={unix_timestamp}`
   - Get content info `/content/{id}`
   - Add view to content `/content/{id}/views/add?userId={userId}&watchedOn={unix_timestamp}`
   - Add like to content `/content/{id}/likes/add?userId={userId}&likedOn={unix_timestamp}`

#### TODO

- be able to register events not only via REST, but via other messaging system (Kafka) ?
- unit test for algorithm (verify probability distribution)
- performance test for entire service

