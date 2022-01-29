REST service, that returns content sorted by UCB1 algorithm. (Multi-Armed Bandit algorithm)  
Spring Boot, Kotlin

Task description [here](FunCorp_Task.pdf)  
Restrictions:  
Caching is used in service.  
In case of deployment many instances of the service - take a look at the external cache provider (Redis).

#### Environment

1) Run MongoDB

```
docker run --rm --name mongo-server -p 27017-27019:27017-27019 -v /opt/mongodb_volume:/data/db -d mongo
```

where `/opt/mongodb_volume` - your local data volume for mongo

2) Run service ```./gradlew clean bootRun```

#### Endpoints
   - Get list of content, using UCB1 `/play/{userId}`
   - Add new content `/content/add?id={id}&createdOn={unix_timestamp}`
   - Get content info `/content/{id}`
   - Add view to content `/content/{id}/views/add?userId={userId}&watchedOn={unix_timestamp}`
   - Add like to content `/content/{id}/likes/add?userId={userId}&likedOn={unix_timestamp}`

#### Tests

- Test coverage is not complete. Only examples of test are provided
- Example of performance test [here](src/test/kotlin/com/funcorp/bandit/loadtests/BanditLoadTest.kt)
