# Programming Challenge: Secret Santa

### Part one
Imagine that every year your extended family does a "Secret Santa" gift exchange. 
For this gift exchange, each person draws another person at random and then gets a gift for them. 
Write a program that will choose a Secret Santa for everyone given a list of all the members of your extended family. 
Obviously, a person cannot be their own Secret Santa.

### Part two
After the third year of having the Secret Santa gift exchange, 
you’ve heard complaints of having the same Secret Santa year after year. 
Modify your program so that a family member can only have the same Secret Santa once every 3 years.

### Part three
As your extended family has grown, members have gotten married and/or had children. 
Families usually get gifts for members of their immediate family, 
so it doesn’t make a lot of sense for anyone to be 
a Secret Santa for a member of their immediate family (spouse, parents, or children). 
Modify your program to take this constraint into consideration when choosing Secret Santas.

## Followup

### Q1. Describe your algorithm for selecting Secret Santas

#### Part one 

I have introduced random shift index for pair family member candidate. 
That algorithm allows building a Secret Santa pair in O(n) in runtime and O(n) for memory complexity  
<img width="547" alt="Part One Algorithm" src="https://user-images.githubusercontent.com/4140597/54382056-10ece280-4687-11e9-86c2-f7b6d134dbe7.png">

#### Part two 

I added a sliding window algorithm that store history for the last three years in a pair year/family member. 

<img width="618" alt="sliding window algorithm" src="https://user-images.githubusercontent.com/4140597/54473875-8f579a80-47d5-11e9-8b80-8c42897e5a51.png">

#### Part three

To store family relations, I used a weighted bi-directed graph.

<img width="834" alt="weighted directed graph representation" src="https://user-images.githubusercontent.com/4140597/54473703-3b4bb680-47d3-11e9-993f-23b5bebfab23.png">


### Q2. What are the run​time characteristics of your final solution?

* best case O(n): 
* worst case O(n^2)

### Q3. What are the memory characteristics of your final solution?

* best case O(n)  
* worst case O(n)  

### Q4. How can/did you structure your application such that adding additional constraints would require minimal code changes?

SecretSanta interface provides two methods with/without filter interface. 

```java
public interface SecretSanta<T extends RelationEntity> {

    Map<T, T> getPairs();

    Map<T, T> getPairs(Predicate<T> filter);

}
``` 
A client can define a custom filter by specifying filter predicate. 
For example, filter all family members older 18 years

 ```java
secretSanta.getPairs(p -> p.getAge() > 18);
 ``` 

### Q5. How can/did you structure your application such that a transition from an in​-memory datastore to a persistent datastore would require minimal code changes?

We should implement FamilyRepository interface for persistent database and wire it for wire calculation algorithm. 

```java
public interface FamilyRepository<T extends RelationEntity> {

    void addMemberPair(T source, T destination, RelationType type);

    List<T> getAllMembers();

    List<T> getMembers(Predicate<Member> filter);

    boolean isFamilyMembers(T source, T destination);
}
```

### Q6. If you had to choose a persistent datastore to use for this application, what would you use and why?

#### Capacity Estimation and Constraints

Let’s assume we have 350M total users

* Storage Estimates

    * Member Id: (16 bytes)
    * Name (256 bytes)
    * Age (1 byte)
    * Gift history for the 3 years (68 bytes)
    * User family relation, 1 average user has 10 immediate family (160 bytes)

Total size: (16 + 256 + 1 + 68 + 160) * 350M ~ 195 GB
  
#### Database 

Before we choose storage, let's have a look at available options, and picked the best suitable.

##### MySQL

MySQL is an open source relation database
 
* Pros
    * Widely used 
    * Available tooling
    * One of the best general purpose database
    * Easy to use
    * Powerful enough
    * SQL <3 
* Cons
    * Does not allow data partitions
    * Scaling 

Summary

We will have a hard time to tune performance for a large dataset, where 
user table: 350 M records 
relation table: 3,500 M records
We can store relations between in the same table in JSON format, but still, 350 M records is a huge amount for MySQL

##### MongoDB

MongodB is a document-oriented database 

* Pros
    * Easy to use 
    * Schema-less might be a good
    * Good scalability as you can easily share the data and have quick availability of data
    * Easy replication of the data  
 
* Cons
    * Less flexible than traditional SQL (i.e.: no joins)
    * Not fully transactional (ACID compliant)

Summary

At first glance, we might think this suitable for us, and we can store family in one document.
But when two families establish a new connection (e.g., get married), we should  merge two documents in a single document, that might cause the problem when our document will grow significantly (MongoDB has a restriction for 16MB per document)

##### Cassandra

Cassandra is a wide column database

* Pros
    * Build in fault tolerance
    * Can grow horizontally
    * Write speed. Cassandra is very fast while writing data
    * Tunable Consistency
    * Cassandra Query Language is a subset of SQL query, but very limited compare to traditional SQL
* Cons
    * Aggregation functions are not very efficient.
    * Read operations is slow
    * High minimum hardware requirements (min 8 GB per node)

Summary

The main focus of cassandra database in fast writes and horizontal scalability, unfortunately, it comes with a price, 
cassandra reads is considerably slow and minimum system requirements are quite high compared to other databases.
For the system, we build where we should keep the system on low specs 
for 11 months where no activity happened on our application might be a problem for the price perspective. 
To build an HA cluster, we should have at least three nodes.
 

##### Neo4J  

Neo4j is a graph database

* Pros
    * Easy way to insert and store relationships
    * Easy to visualize data in Neo4j browser
    * Mature Query language 
    * The best fit when joins are a common in a relational store
* Cons
    * Horizontal scaling
    * Slow writes

Summary

Secret Santa application looks like a perfect use case for graph databases where you have a lot of relations
between entities and powerful query language  (cypher) will cover most of your filter needs. 
The main problem with the graph database is scalability and performance,  but according to Neo4J documentation, 
community edition limit for relations/node is 34 Bn. A slow reads performance we can fix by introducing cache mechanism. 

### Q7. Let’s imagine the entire population of the United States is your extended family, how do you scale your application to generate Secret Santas?

#### High Level Design

The detailed workflow would look like this:

1. A user sends a request to the HAProxy LB
2. The server receives the request and transfers it to one of the Application servers. 
3. For a read operation cache use Read-through pattern
4. For a write operation cache use Write-through pattern
5. At a given time X day to the Christmas job trigger recalculation to find a random Santa for the uses  

Note: we might consider implementing a user notification system by email/SMS in a future

#### Load Balancing

We can add the Load balancing layer between Clients and Application servers.
Initially, we could use a simple Round Robin approach that distributes incoming requests equally among backend servers.

#### Cache

We should cache users that are frequently accessed. 
We can use some off-the-shelf solution like Redis, which can store full user information. 
The application servers, before hitting the database, should check if the cache has the desired user.
Least Recently Used (LRU) can be a reasonable eviction policy for our system.

To further increase the efficiency, we can replicate our caching servers to distribute load between them.

#### Monitoring 

Logging and monitoring is a significant part of the system. 
While monitoring helps to gauge the health/state of the system at any instance or interval of time,
logging give you details about the event such as a resource that was accessed, who accessed it, and the time.

#### Fault Tolerance

* Neo4J master-slave replication 
* HAProxy 2 instances with VRRP
* Application Server 2 active-active instances

## Summary 

* Build project on Circle CI
* Add google error-prone static analysis tool
* Add mutation test report 

<img width="858" alt="Mutation test report" src="https://user-images.githubusercontent.com/4140597/54474177-abf5d180-47d9-11e9-81b7-f4716bed96c5.png">

## License

-------
    MIT License
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
