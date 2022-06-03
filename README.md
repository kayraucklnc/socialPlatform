# [Segmentation Fault] - Social Platform App

![Home]([http://url/to/img.png](https://github.com/kayraucklnc/socialPlatform/blob/main/imgs/home.png?raw=true))
![Home]([http://url/to/img.png](https://github.com/kayraucklnc/socialPlatform/blob/main/imgs/feed.png?raw=true))


### Package Structure

Given that all usecases contain almost all CRUD operations, it would make sense to divide packages based on use cases.
So, we will have global packages that apply generally and packages that are contained in usecase packages.

***Global Packages:***

* **config:** All `@Configuraion` classes. These usually apply some changes to the default settings of the Spring modules(Think Jackson Serializer etc.)
* **entity(domain):** Entity model classes for the database. Think User, Post etc.
* **security:** Handles all Spring Security related code. Things such as Configurations and endpoint filters(access related).
* **util:** Contains utilities such as Date/Time helper classes and specialized Request/Response related classes.

***Per Usecase Packages:***

* **dto:** Data Transfer Object classes. These are usually just data classes. Sometimes we don't want to send the whole entity in the response, maybe we want to add some additional fields or remove some from the entity. These serve that purpose.

* **mappers:** Classes that contain `static` functions to convert DTO->Entity and Entity->DTO.

* **repository:** Repository classes that implement JPA. Here we expose functions that will fetch the data from the database. Each repo is related to an entity. Ex. UserRepo

* **controller:** Here our endpoints are exposed and supplied to Spring. This class will implement services to and forward calls to them so that business logic can be applied. Methods in this class can be annotated to respond to GET/POST/DELETE/PUT requests. Given that we need all of these for each use case, we will basically have a controller for each usecase. This will give a clear structure and keep LoC relatively small per class.

* **service:** Interfaces that describe what a service will do.
  Example: 
  
  ```java
  public interface UserService {
  
    User saveUser(User user);
  
    Role saveRole(Role role);
  
    void addRoleToUser(String username, String roleName);
  
    User getUser(String username);
  
    List<User> getUsers(); // usually return a Page instead of a collection, more efficient
  
  }
  ```

All business logic will be in the implementation of these methods and the interface allows us to swap implementations without causing distress to other pieces of the codebase.

---

### Dependencies

These are Maven project dependencies. Most of the time we won't have to worry about manual management of these since [Spring Initializer](https://start.spring.io/) will be used to generate the backend project. Think of this like the `create-react-app`.

* Spring Web
* Spring Data JPA
* Spring Security
* PostgreSQL Driver
* Lombok
* Auth0 JWT

---

## First startup

After opening the project, in order to run it a few steps have to be completed.

1. Install PostgreSQL - [PostgreSQL: Downloads](https://www.postgresql.org/download/)

   2. Create `huceng` database using the postgres CLI

        ***Make sure the postgres service is running*** 

```sql
-- create database, duh
CREATE DATABASE huceng;
-- grant access privilages to the postgres user
GRANT ALL PRIVILEGES ON DATABASE "huceng" TO postgres;    
```

    3.  Configure `application.properties` in the java project

```
spring.datasource.url=jdbc:postgresql://localhost:5432/ //database name goes here
spring.datasource.username= //username for postgres db user
spring.datasource.password= //password for the above
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true
```

    4. Run the Spring app.

----

## Notes

* Relationships are realized with annotations so some entities from the graph are not represented as classes - specifically weak entities.

* Spring uses Jackson serializer so most request/response objects will be mapped using simple POJO classes. These will have request/response in their name eg. `FooRequest`.

----

## Contributions:

NİKOLA DRLJACA

KAYRA ÜÇKILINÇ

SAFA LEVENTOGLU

DAVUT KULAKSIZ

ÖZGÜR OKUMUŞ
