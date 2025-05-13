# 🏦 WRDD Bank System

<div align="center">

![WRDD Bank System](client/src/main/resources/Images/icon.png)

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-17%2B-8A2BE2?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.7%2B-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![JPA](https://img.shields.io/badge/JPA-Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)](https://hibernate.org/)

</div>

A modern banking system built with JavaFX frontend and Spring Boot backend that enables account management, transactions, and administrative operations through a secure client-server architecture.

## ✨ Features

- **🔐 Secure Authentication**: Login system for clients and administrators
- **💼 Account Management**: Create and manage checking and savings accounts
- **💸 Transaction Processing**: Deposit, withdraw, and transfer funds between accounts
- **📊 Financial Dashboard**: View account balances and transaction history
- **👤 User Management**: Admin interface for managing client accounts
- **🏗️ Robust Architecture**: Clean separation between client UI and server business logic

## 🏛️ Architecture

The project is divided into two main components:

### Client

- JavaFX-based desktop application
- MVC architecture separating UI from business logic
- Communicates with server via REST API
- Handles user interface and interactions
- No direct database access

### Server

- Spring Boot backend application
- Exposes REST API endpoints
- Handles database operations using Hibernate/JPA
- Implements business logic and security
- Follows standard Spring architecture:
  - Controller Layer: REST endpoints
  - Service Layer: Business logic
  - Repository Layer: Data access
  - Model Layer: Entity classes

## 🚀 Getting Started

### Prerequisites

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/joewrdd/WrddBank.git
   cd wrddbanksystem
   ```

2. Set up the database:

   Create a MySQL database and set the connection details in environment variables.

3. Configure environment variables:

   **Server**

   ```
   DB_URL=jdbc:mysql://localhost:3306/wrddbank
   DB_USER=your_db_username
   DB_PASSWORD=your_db_password
   ```

4. Start the server:

   ```bash
   cd server
   mvn spring-boot:run
   ```

5. Start the client:

   ```bash
   cd client
   mvn javafx:run
   ```

## 📊 Database Schema

### Client Entity

```java
{
  id: Long,                // Primary key
  firstName: String,       // Client's first name
  lastName: String,        // Client's last name
  address: String,         // Client's address
  email: String,           // Client's email (unique)
  password: String,        // Hashed password
  accounts: List<Account>, // Client's accounts
  dateCreated: Date        // Registration date
}
```

### Account Entity

```java
{
  id: Long,              // Primary key
  accountNumber: String, // Unique account number
  type: AccountType,     // CHECKING or SAVINGS
  balance: BigDecimal,   // Current balance
  client: Client,        // Account owner
  dateCreated: Date,     // Account creation date
  transactions: List<Transaction> // Transaction history
}
```

### Transaction Entity

```java
{
  id: Long,               // Primary key
  amount: BigDecimal,     // Transaction amount
  type: TransactionType,  // DEPOSIT, WITHDRAWAL, TRANSFER
  date: Date,             // Transaction date
  account: Account,       // Associated account
  description: String     // Transaction description
}
```

### Admin Entity

```java
{
  id: Long,           // Primary key
  username: String,   // Admin username
  password: String,   // Hashed password
  email: String,      // Admin email
  role: String        // Admin role
}
```

## 🔌 API Endpoints

### Authentication

- `POST /api/auth/login` - User login

  - Parameters: `{ email, password }`
  - Returns: `{ token, clientId, role }`

- `POST /api/auth/admin/login` - Admin login
  - Parameters: `{ username, password }`
  - Returns: `{ token, adminId }`

### Client Management

- `GET /api/clients` - Get all clients (admin only)

  - Returns: `[ { id, firstName, lastName, email, ... } ]`

- `GET /api/clients/{id}` - Get client by ID

  - Returns: `{ id, firstName, lastName, email, ... }`

- `POST /api/clients` - Create new client

  - Parameters: `{ firstName, lastName, address, email, password }`
  - Returns: `{ id, firstName, lastName, email, ... }`

- `PUT /api/clients/{id}` - Update client
  - Parameters: `{ firstName, lastName, address, email }`
  - Returns: `{ id, firstName, lastName, email, ... }`

### Account Management

- `GET /api/clients/{id}/accounts` - Get client accounts

  - Returns: `[ { id, accountNumber, type, balance, ... } ]`

- `POST /api/clients/{id}/accounts` - Create new account

  - Parameters: `{ type, initialDeposit }`
  - Returns: `{ id, accountNumber, type, balance, ... }`

- `GET /api/accounts/{id}` - Get account details

  - Returns: `{ id, accountNumber, type, balance, ... }`

- `DELETE /api/accounts/{id}` - Close account
  - Returns: `{ message }`

### Transactions

- `GET /api/accounts/{id}/transactions` - Get account transactions

  - Returns: `[ { id, amount, type, date, description, ... } ]`

- `POST /api/accounts/{id}/deposit` - Deposit funds

  - Parameters: `{ amount, description }`
  - Returns: `{ id, amount, type, date, newBalance }`

- `POST /api/accounts/{id}/withdraw` - Withdraw funds

  - Parameters: `{ amount, description }`
  - Returns: `{ id, amount, type, date, newBalance }`

- `POST /api/accounts/{id}/transfer` - Transfer funds
  - Parameters: `{ targetAccountId, amount, description }`
  - Returns: `{ id, amount, type, date, newBalance }`

## 💻 Running and Testing

### Development Environment

1. Start both server and client as described in Installation section
2. The application will be available at:
   - Server API: `http://localhost:8080`
   - Client: JavaFX desktop application

### Testing the Application

1. **Authentication Testing**:

   - Launch the client application
   - Register a new account or login with existing credentials
   - Verify you can access the dashboard

2. **Account Management Testing**:

   - Create new checking or savings account
   - View account details and balances
   - Close an existing account

3. **Transaction Testing**:

   - Deposit funds to an account
   - Withdraw funds from an account
   - Transfer funds between accounts
   - View transaction history

4. **Admin Testing**:
   - Login as an administrator
   - View list of all clients
   - View client details and accounts
   - Modify client information

## 🛠️ Technical Implementation

### Client-Side Components

- **Controllers**: Handle UI events and API communication
- **Models**: Data models for client-side operations
- **Views**: FXML-based user interface components
- **ApiClient**: Handles HTTP communications with the server

### Server-Side Components

- **Controllers**: REST endpoint implementations
- **Services**: Business logic implementation
- **Repositories**: Data access interfaces
- **Models**: JPA entity classes
- **Configuration**: Security and application setup

## 🧪 Third-Party Libraries

### Client

- **JavaFX**: UI framework
- **Jackson**: JSON processing
- **Java HTTP Client**: API communication
- **Maven**: Build and dependency management

### Server

- **Spring Boot**: Backend framework
- **Spring Data JPA**: Data access abstraction
- **Spring Security**: Authentication and authorization
- **Hibernate**: ORM framework
- **MySQL Connector/J**: Database driver
- **Lombok**: Boilerplate code reduction
- **Maven**: Build and dependency management

## 🙏 Acknowledgements

- [JavaFX](https://openjfx.io/) for providing a robust UI framework
- [Spring Boot](https://spring.io/projects/spring-boot) for simplifying backend development
- [Hibernate](https://hibernate.org/) for object-relational mapping
- [MySQL](https://www.mysql.com/) for reliable database services

## 📄 License

This project is available under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">
  <p>Created With ❤️ By Joe Ward</p>
</div>
