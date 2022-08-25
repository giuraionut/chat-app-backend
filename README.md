# Chat application


# Docker

* You can find the docker configuration in `/backend/docker/`. You just need to compose it and everything should be ready including `postgresql`, `redis`, `rabbitmq`, etc.

# Gateway

## Registering the services to the gateway

* Registration of services to the gateway can be done in a `Java` file or in a `Yaml` file.
* Yaml file configuration is faster and more elegant so we are going to use an `application.yml` file

`application.yaml:`
```yaml
server:
  port: 9191 #port of our gateway

spring:
  application:
    name: API-GATEWAY #name for eureka server

  cloud:
    gateway:

        - id: MESSAGE-SERVICE
          uri: lb://MESSAGE-SERVICE
          predicates:
            - Path=/api/v1/message/**

eureka: #this is a client for our eureka server
  client:
    register-with-eureka: true
    fetch-registry: true
    service_url:
      defaultZone: http://localhost:8761/eureka/
  instance: localhost
```

* This is the basic setup to be able to access a service from a gateway.
* Now any request meant for a service made to our `gateway` will be redirected to the specific service.
* `/**` is a wildcard notation.

## CircuitBreaker

* A circuitbreaker is a pattern used in case of a failed service.
* In a microservice architecture a circuitbreaker is esential because the services are interdependent.
* If a service fails we will have a domino effect that will break our entire application.

Implementation
* For the circuitbreaker we are going to use `resilience4j`

`application.yml:`
```yml
resilience4j:
  circuitbreaker:
    instances:

      messageService:
        slidingWindowSize: 10
        permittedNumberOfCallsInHalfOpenState: 5
        failureRateThreshold: 50
        waitDurationInOpenState: 15000
        registerHealthIndicator: true

  timelimiter:
    instances:
      userService:
        timeoutDuration: 2s
```

* We can implement the circuitbreaker in our `application.yml` as a filter in our routes:

```yml
routes:
  - id: MESSAGE-SERVICE
    uri: lb://MESSAGE-SERVICE
    predicates:
      - Path=/api/v1/message/**
    filters:
      - name: CircuitBreaker 
        args:
          name: messageService
          fallbackUri: forward:/messageServiceFallback # fallbackmethod defined in a RestController
```

`Rest Controller for fallback methods:`
```java
@GetMapping("/messageServiceFallback")
public String messageServiceFallback() {
    return "There is a problem with MESSAGE-SERVICE. Please try again later";
}
```
* The same pattern can be applied to other services.

## Rate Limiter with Redis

* Rate limiting can be applied for a number of reasons. Some of them may be to protect your application for attacks, to limit different categories of users based on their type like premium user, basic user, etc.
* To implement this feature we are going to use a Redis server to keep track of the clients.
* We also need `Docker` to start our Redis server.
* Redis is used to store the tokens so we can keep track of our clients.

* Now you can add another filter to each service or you can add a `default-filter` for all services:
```yml
cloud:
    gateway:

      default-filters:
        - name: RequestRateLimiter
          args:
            redis-rate-limiter.replenishRate: 10 
            redis-rate-limiter.burstCapacity: 20
            redis-rate-limiter.requestedTokens: 1
```

## Monitor module with Prometheus and Grafana

* To be able to watch our application we need `Prometheus` and `Grafana` which can be obtained easily with `Docker` as containers.
