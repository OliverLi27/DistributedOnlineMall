# DistributedOnlineMall
Back-end project of distributed online mall project

The project adopts the idea of micro-service to divide the system into gateway service, public service, order
service, payment system, comment system, user system, shopping system, commodity service, etc.
The whole backend system is built by Springboot as the framework, with Dubbo as the RPC framework,
through the interface exposed by each service to realize the communication between each service, using
Mybatis as the Dao layer framework, with Druid database connection pool, and MySQL database
connection,
At the same time, in order to ensure the transaction consistency of distributed system, Redis is used as the
data cache and RocketMQ as the message queue, and the try-confirm -Cancel transaction model is
adopted to build the solution of distributed transaction, so as to ensure the transaction consistency when
multiple services operate the database at the same time.
In the face of large volume of traffic, the token bucket flow limiting scheme is realized by using Redis and
RateLimiter's time slice algorithm idea of Google Guava.
At the same time, in order to establish the order business process convenient for transaction management,
the producer consumer model is adopted to deal with various types of order problems flexibly through the
exception capture of different handlers.
