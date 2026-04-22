> ## Documentation Index
> Fetch the complete documentation index at: [https://mintlify.com/amitsaxena098/rlaas-users-service](https://mintlify.wiki/amitsaxena098/rlaas-users-service)

# Introduction to RLaaS Users Service and how it works

> RLaaS Users Service is a Spring Boot microservice that enforces per-user rate limits via Redis. Deploy it as a gateway in front of your APIs.

RLaaS Users Service is a lightweight Spring Boot microservice that sits in front of your APIs and enforces per-user HTTP rate limits. It solves the problem of runaway clients exhausting shared resources by tracking each user's request count in Redis and rejecting requests that exceed the configured threshold — returning a `429` response with the exact number of seconds the client must wait before retrying.

## What it does

Every inbound HTTP request passes through a servlet filter (`RateLimiterFilter`) before it reaches your application logic. The filter extracts the `userId` query parameter, then runs an atomic Lua script against Redis to increment and inspect a per-user counter. If the counter is within the allowed window, the request proceeds. If the user has exceeded the limit, the service immediately returns `429 Too Many Requests` with a human-readable retry message — no request ever reaches your upstream service.

Key behaviors:

* **Atomic counters**: Redis Lua scripts ensure increment-and-check is race-condition-free, even across multiple service instances.
* **Two algorithms**: choose between a fixed-window counter or a sliding-window counter depending on your tolerance for burst traffic at window boundaries.
* **Stateless service**: all state lives in Redis, so you can scale horizontally behind a load balancer without coordination.

## Request flow

<Steps>
  <Step title="Request arrives">
    A client sends `GET /check?userId={userId}` to the service.
  </Step>

  <Step title="Filter intercepts">
    `RateLimiterFilter` intercepts the request before it reaches the controller and calls `RateLimitingAlgorithm.allowRequest(userId)`.
  </Step>

  <Step title="Redis Lua script runs">
    An atomic Lua script increments the user's counter in Redis and reads back the current count and TTL.
  </Step>

  <Step title="Allow or deny">
    If `allowed` is `true`, the request continues and the service returns `200 User is allowed`. If `allowed` is `false`, the filter short-circuits and writes `User is not allowed...Try after X seconds.` with HTTP 429.
  </Step>
</Steps>

<Note>
  RLaaS Users Service is stateless except for Redis — you can run multiple instances behind a load balancer and share the same Redis instance.
</Note>

## Explore the docs

<CardGroup cols={2}>
  <Card title="Quickstart" icon="rocket" href="/quickstart">
    Deploy the service and make your first rate-limited request in under five minutes.
  </Card>

  <Card title="Configuration" icon="sliders" href="/configuration">
    Set the algorithm, window size, max requests, and Redis connection URL.
  </Card>

  <Card title="Rate limiting concepts" icon="book-open" href="/concepts/rate-limiting">
    Understand how per-user counters, windows, and TTLs work together.
  </Card>

  <Card title="API reference" icon="code" href="/api/check-endpoint">
    Full reference for the `GET /check` endpoint, parameters, and response codes.
  </Card>
</CardGroup>

## Load Testing Report

[View Locust Report](https://amitsaxena098.github.io/rlaas-users-service/Locust_2026-04-23-00h25_locustfile.py_https___rlaas-users-service-production.up.railway.app.html)
<img width="1533" height="442" alt="image" src="https://github.com/user-attachments/assets/b7d3360b-5cb4-443f-b2d0-919d9b9cd16a" />
<img width="1484" height="394" alt="image" src="https://github.com/user-attachments/assets/7cbe3063-8369-4ab2-aa30-18617d6253eb" />

## Metrics
<img width="1512" height="782" alt="image" src="https://github.com/user-attachments/assets/0b57b4e7-d425-4dee-b428-5bfd7dcb1b94" />

## Redis Metrics
<img width="1607" height="929" alt="image" src="https://github.com/user-attachments/assets/87c3f344-0692-4752-95c6-84ab623b0d8c" />


