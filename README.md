# Property Management System
 
This project implements a simple CRUD (Create, Read, Update, Delete) system for managing real estate properties using a Spring Boot backend and MySQL database and a frontend built with HTML, CSS, and JavaScript. It allows users to create, update, delete properties. Using HTTPS and TLS for secure communication between the backend and frontend.

![Demo](images/demo.gif)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You need to install the following tools to run the project:
1. Java
    ```
    java -version
    ```
    It should appear something like this:
    ```
    java version "17.0.10" 2024-01-16 LTS
    Java(TM) SE Runtime Environment (build 17.0.10+11-LTS-240)
    Java HotSpot(TM) 64-Bit Server VM (build 17.0.10+11-LTS-240, mixed mode, sharing)
    ```
    ```
    javac -version
    ```
    It should appear something like this:
    ```
    javac 17.0.10
    ```
2. Maven
    ```
    mvn -version
    ```
    It should appear something like this:
    ```
    Apache Maven 3.9.6 (bc0240f3c744dd6b6ec2920b3cd08dcc295161ae)
    Maven home: C:\workspace\apache-maven-3.9.6-bin\apache-maven-3.9.6
    Java version: 17.0.10, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-17
    Default locale: es_CO, platform encoding: Cp1252
    OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
    ```
3. Git
    ```
    git --version
    ```
    It should appear something like this:
    ```
    git version 2.44.0
    ```
4. Docker
    ```
    docker --version
    ```
    It should appear something like this:
    ```
    Docker version 27.3.1, build ce12230
    ```

### Installing locally

1. Clone this repository and go to project directory:
    ```
    git clone https://github.com/oscar0617/Lab06-AREP-SecureApplication

    cd Lab06-AREP-SecureApplication
    ```
2. Build the project:
    ```
    mvn package
    ```
    Should appear something like this:
    ```
    
    ```
3. Run the container with MySQL
    ```
    docker-compose up -d
    ```
4. Run the project:
    ```
    mvn spring-boot:run
    ```
    Should appear something like this:
    ```
    
    ```
After this, you will be able to access into your browser with https://localhost:5000 and try the CRUD System with a data base locally, to do it with a MySQL data base follow the next section to deploy it on AWS. Check this video of the project working locally:

![Demo](images/LocalHostPruebaLab6.gif)


### Installing on AWS

Keep in mind that you will need an active AWS account to run this project on cloud.

#### MySQL Machine

1. Create an EC2 instance on AWS to host MySQL databse using Amazon Linux as the OS.

2. Connect to the EC2 instance:

```
ssh -i your-key.pem ec2-user@<mysql-ec2-instance-ip>
```

3. Install MySQL
```
wget https://dev.mysql.com/get/mysql80-community-release-el9-1.noarch.rpm
sudo rpm -Uvh mysql80-community-release-el9-1.noarch.rpm
sudo yum update -y
sudo yum install -y mysql-community-server
```
You should see something like this:
![MySQL Installation](images/instalacionMySQL.png)

4. Start MySQL
```
sudo systemctl enable --now mysqld
sudo systemctl status mysqld
```
5. Get the temporal password of MySQL
```
sudo grep 'temporary password' /var/log/mysqld.log
```
6. Connect into MySQL using this password
```
mysql -u root -p
```
![Password example](images/psswd.png)
7. Create the database and user:
```
CREATE DATABASE crudsystem;
CREATE USER 'myuser'@'%' IDENTIFIED BY 'Password123!';
GRANT ALL PRIVILEGES ON crudsystem.* TO 'myuser'@'%';
FLUSH PRIVILEGES;
```
8. Allow external connections to MySQL by editing the MySQL config file:
```
sudo nano /etc/my.cnf
```
9. Add the following line at the bottom of the file:
```
bind-address = 0.0.0.0
```
Follow this example:
![Connection example](images/connections.png)
10. Restart MySQL:
```
sudo systemctl restart mysqld
```
11. Verify the MySQL status
![Status example](images/statusMySQL.png)

12. Check the listening port
![Listening example](images/listening.png)

And that's it, we finished setting up our MySQL instance on EC2. Now let's set up the Backend instance.

#### Domain (Duck DNS)

To use a valid certificate we need a domain, otherwise this will not work. So to solve this, we are going to use duckdns.org.

1. Go to duckdns.org and use your GitHub account or create a new one.

2. Create a subdomain for each EC2 instances (front end and backend)

3. Update the DNS with the IP address of each of your EC2 instances.

![DuckDNS](images/duck.png)

#### Apache server
In a different EC2 instance:

1. Install Apache:
    ```
    sudo yum install httpd -y
    sudo systemctl start httpd
    sudo systemctl enable httpd
    ```

2. Set up HTTPS:
    Install Certbot and configure Apache to serve content over HTTPS:
    ```
    sudo yum install certbot python3-certbot-apache -y
    sudo certbot certonly --standalone -d <DOMAIN>
    ```
    This will automatically configure Apache to use the certificate.

3. Edit the Apache VirtualHost configuration:
    ```
    sudo nano /etc/httpd/conf.d/serverfront.conf
    ```

4. Add the following configuration:
    ```
      <VirtualHost *:80>
        ServerName labarep6oscarl.duckdns.org
        DocumentRoot /var/www/html

        ErrorLog /var/log/httpd/labarep6oscarl_error.log
        CustomLog /var/log/httpd/labarep6oscarl_access.log combined
      </VirtualHost>

      <VirtualHost *:443>
          ServerName labarep6oscarl.duckdns.org
          DocumentRoot /var/www/html

          SSLEngine on
          SSLCertificateFile /etc/letsencrypt/live/labarep6oscarl.duckdns.org/fullchain.pem
          SSLCertificateKeyFile /etc/letsencrypt/live/labarep6oscarl.duckdns.org/privkey.pem

          ErrorLog /var/log/httpd/labarep6oscarl_error_ssl.log
          CustomLog /var/log/httpd/labarep6oscarl_access_ssl.log combined
      </VirtualHost>
    ```

5. Restart Apache to apply changes:
    ```
    sudo systemctl restart httpd
    ```

6. Deploy frontend files:
    Copy your frontend files (HTML, CSS, JS) to `/var/www/html/`:
    ```
    sudo cp -r /path/to/your/frontend/* /var/www/html/
    ```

7. Ensure the firewall allows HTTPS traffic:
    Ensure that port `443` is open for the Apache server in the AWS Security Group.

8. Apply SSL on Apache:

  ```
  sudo certbot certonly --manual --preferred-challenges dns -d <DOMAIN>
  ```
  And renew the certificate.
9. 


#### Prerrequisites on the backend for AWS

Before we proceed, we have to make some changes on our settings and code in our backend, to run the application in AWS.

1. In application properties you must add this code:
```
spring.datasource.url=jdbc:mysql://<MySQL IPAddress>:3306/crudsystem
spring.datasource.username=myuser
spring.datasource.password=<passwordOfTheUser>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
This code connects into the MySQL Instance to use it as database

2. In `script.js` you need to change this line
```
let IPADDRESS = " public ip address of the backend machine";
```

With this steps you will be able to deploy the backend on AWS

#### Backend on AWS

1. Create another EC2 instance for the backend and connect to it via SSH
```
ssh -i your-key.pem ec2-user@<backend-ec2-instance-ip>
```

2. Install Java and Maven:

```
sudo yum install java-17-amazon-corretto -y
sudo yum install maven -y
```

3. Transfer the JAR file to the EC2 instance using SFTP (keep in mind that you must to compile and package the jar file before this step in you local machine):
```
sftp -i your-key.pem ec2-user@<backend-ec2-instance-ip>

put target/arep-0.0.1-SNAPSHOT.jar
```

4. Run the application using Spring-boot
```
java -jar arep-0.0.1-SNAPSHOT.jar
```

To access to the application don't forget to open the ports on the IAM of AWS, otherwise the connection will be rejected, same for MySQL instance.

Let's see the application running in AWS:

![AWS](images/demo3.gif)

## Architecture

![Architecture Diagram](images/Architecture.png)

#### Overview

This architecture diagram illustrates the request flow in the **Real Estate CRUD System**. The system is divided into three main components:

1. **Browser (Client)**
2. **Backend (Spring Boot Application)**
3. **Database (MySQL)**

### Components and Workflow

#### **1. Browser (Client)**
The user interacts with the system through a web browser, which:
- Sends **HTTP requests** to the backend on **port 8080**.
- Requests **static files** such as:
  - **HTML** (for page structure).
  - **JavaScript** (for dynamic interactions).
  - **CSS** (for styling).
- Uses JavaScript to send API requests for **CRUD operations** on property listings.

#### **2. Backend (Spring Boot Application)**
The backend is a **Spring Boot** application that:
- Processes requests from the browser.
- Manages property data through the **Property Management Service**.
- Exposes **RESTful API endpoints** for CRUD operations.
- Communicates with the **MySQL database** to retrieve and store property information.

#### **3. Database (MySQL)**
The database is a **MySQL instance** responsible for:
- Storing property listings.
- Handling queries related to **creating, reading, updating, and deleting** property records.
- Sending requested data back to the backend.

### Request Flow

1. The **user** sends an HTTP request from the **browser** (client) to **port 8080**.
2. If the request is for a **static file** (HTML, CSS, JavaScript), the backend serves the file directly.
3. If the request is for a **CRUD operation**, the backend processes it through the **Property Management Service**.
4. The backend queries or updates the **MySQL database** as needed.
5. The database sends the requested information back to the backend.
6. The backend responds to the browser with the processed data.
7. The browser dynamically updates the UI to reflect the changes.

This architecture ensures a structured, efficient, and **scalable** real estate management system.

## Class Diagram
![Class Diagram](images/ClassDiagram.png)

#### Overview

The class diagram represents the **Real Estate CRUD System**, showing its main components, relationships, and responsibilities. The system follows a **Model-View-Controller (MVC)** architecture and is divided into three key layers:

1. **`Model`** - Defines the `Property` entity and its attributes.
2. **`Repository & Service`** - Handles database operations and business logic.
3. **`Controller`** - Manages HTTP requests and responses.

###### **Model Layer**
The `Property` class represents a real estate entity with attributes:

- **Attributes**:
  - `id: Long` → Unique identifier of the property.
  - `address: String` → Property location.
  - `price: Integer` → Property price.
  - `size: String` → Property size.
  - `description: String` → Additional property details.

- **Methods**:
  - `getId() : Long` → Returns the property ID.
  - `getAddress() : String` → Returns the address.
  - `getPrice() : Integer` → Returns the price.
  - `getSize() : String` → Returns the size.
  - `getDescription() : String` → Returns the description.
  - `setAddress(address: String) : void` → Sets a new address.
  - `setPrice(price: Integer) : void` → Updates the price.
  - `setSize(size: String) : void` → Updates the size.
  - `setDescription(description: String) : void` → Updates the description.

This layer ensures **data encapsulation** and provides access to real estate properties.

###### **Repository Layer**
The `PropertyRepository` interface extends `JpaRepository` to provide database access.

- **Methods**:
  - `findById(id: Long) : Optional<Property>` → Retrieves a property by its ID.
  - `findAll() : List<Property>` → Fetches all properties.

This layer abstracts **data persistence** and allows interaction with the database.

###### **Service Layer**
The `PropertyService` class implements the business logic for managing properties.

- **Methods**:
  - `createProperty(property: Property) : Property` → Saves a new property.
  - `findAllProperties() : List<Property>` → Returns all properties.
  - `findById(id: Long) : Optional<Property>` → Fetches a property by ID.
  - `deletePropertyById(id: Long) : void` → Deletes a property.
  - `updateProperty(property: Property) : Property` → Updates an existing property.

This layer ensures **data validation, processing, and interaction** with the repository.

###### **Controller Layer**
The `PropertyController` class handles **HTTP requests** and delegates operations to the service layer.

- **Methods**:
  - `@GetMapping("/{id}")`
    - **Description**: Fetches a property by ID.
    - **Returns**: `ResponseEntity<Property>`.
  
  - `@PostMapping("/create")`
    - **Description**: Creates a new property.
    - **Returns**: `ResponseEntity<Property>` with status `201 CREATED`.

  - `@GetMapping`
    - **Description**: Retrieves all properties.
    - **Returns**: `ResponseEntity<List<Property>>`.

  - `@DeleteMapping("/{id}")`
    - **Description**: Deletes a property by ID.
    - **Returns**: `ResponseEntity<Void>` with `204 NO CONTENT` or `404 NOT FOUND`.

  - `@PutMapping("/update")`
    - **Description**: Updates an existing property.
    - **Returns**: `ResponseEntity<Property>` with `200 OK` or `404 NOT FOUND`.

This layer acts as the **REST API interface**, exposing endpoints for client interactions.

###### **Relationships and Interactions**
- **Model → Repository**
  - The `Property` class is mapped to the database using JPA.
  
- **Repository → Service**
  - The `PropertyRepository` is used by `PropertyService` to fetch and manage data.
  
- **Service → Controller**
  - The `PropertyService` handles business logic before sending data to `PropertyController`.

- **Controller → API**
  - The `PropertyController` exposes RESTful endpoints for external applications to interact with the system.

## Running the tests

The following unit tests were created to validate the functionality of the `PropertyController` class. These tests ensure that each endpoint of the REST API behaves as expected.

#### **1. `testGetPropertyById_WhenPropertyExists`**
- **Purpose**: Validates that the `/v1/property/{id}` endpoint returns a property when it exists.
- **What it tests**:
  - Given an existing property ID, the controller should return a `200 OK` response.
  - The returned property should match the requested ID and have the correct address.


#### **2. `testGetPropertyById_WhenPropertyDoesNotExist`**
- **Purpose**: Ensures that requesting a non-existent property ID returns `404 NOT FOUND`.
- **What it tests**:
  - When a property is not found, the response should have no body and return `404 NOT FOUND`.


#### **3. `testCreateProperty`**
- **Purpose**: Validates the `/v1/property/create` endpoint to ensure that new properties can be created successfully.
- **What it tests**:
  - Given a valid property object, the system should return `201 CREATED` and persist the property.
  - The returned property should match the provided data.


#### **4. `testGetAllProperties`**
- **Purpose**: Ensures that the `/v1/property` endpoint retrieves all stored properties.
- **What it tests**:
  - The API should return `200 OK` with a list of properties.
  - The number of returned properties should match the expected count.

#### **5. `testDeleteProperty_WhenExists`**
- **Purpose**: Validates that an existing property can be deleted using the `/v1/property/{id}` endpoint.
- **What it tests**:
  - Given an existing property ID, the API should return `204 NO CONTENT` after deletion.
  - The property should no longer exist in the system.


#### **6. `testDeleteProperty_WhenNotExists`**
- **Purpose**: Ensures that trying to delete a non-existent property results in a `404 NOT FOUND` response.
- **What it tests**:
  - When a property with the given ID does not exist, the API should return `404 NOT FOUND`.

#### **7. `testUpdateProperty_WhenExists`**
- **Purpose**: Validates that an existing property can be updated using the `/v1/property/update` endpoint.
- **What it tests**:
  - Given a valid property ID and updated data, the API should return `200 OK`.
  - The updated property should reflect the changes.

#### **8. `testUpdateProperty_WhenNotExists`**
- **Purpose**: Ensures that trying to update a non-existent property results in a `404 NOT FOUND` response.
- **What it tests**:
  - When a property does not exist, the API should return `404 NOT FOUND`.
  - No changes should be made in the database.

### **Test Execution**
Each of the tests was executed using **JUnit 5** and **Mockito** to mock dependencies and isolate the `PropertyController`. The expected outcomes were met in all cases, validating the correctness of the CRUD operations.


![Test cases](images/test.png)

## Conclusion

The Real Estate CRUD System provides a well-structured and scalable solution for managing property listings. Built with Spring Boot, it follows a layered architecture ensuring separation of concerns between the controller, service, and repository layers.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [GIT](https://git-scm.com) - Version control
* [Spring-boot](https://spring.io/projects/spring-boot) - Backend framework
* [MySQL](https://www.mysql.com) - Database
* [Docker](https://www.docker.com) - Virtualization

## Versioning

I use [GitHub](http://github.com) for versioning.

## Authors

* **Oscar Santiago Lesmes Parra** - [oscar0617](https://github.com/oscar0617)

Date: 03/03/2025
## License

This project is licensed under the GNU.
