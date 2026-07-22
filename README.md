# CLI Banking Application

A console-based banking system simulating core bank operations: account management, secure deposits, and atomic transfers.
This project demonstrates backend Java fundamentals, database integration, and object-oriented design.

## Features
* **Account Management:** Automatically generates 16-digit card numbers and 4-digit PINs.
* **Authentication:** Secure login validation.
* **Transactions:** Check balance, deposit funds, and transfer money to other accounts.
* **ACID Compliance:** Uses SQLite transactions (`setAutoCommit(false)`, `commit()`, `rollback()`) to ensure no money is lost during transfers if an error occurs.

## Tech Stack
* **Language:** Java 17+ (utilizing `record` classes and enhanced `switch` statements)
* **Database:** SQLite
* **API:** JDBC (Java Database Connectivity)
* **Build Tool:** Maven

## How to Run

1. Clone the repository:
   ```bash
   git clone [https://github.com/Matrixioo/cli-banking-app.git](https://github.com/Matrixioo/cli-banking-app.git)

2. Navigate to the project directory:
    ```bash
   cd cli-banking-app
   
3. Compile and run using Maven:
    ```bash
   mvn clean compile exec:java -Dexec.mainClass="com.bank.Main"
   
## Architecture & OOP Principles

* `Account.java`: Implemented as a Java `record` to act as an immutable data carrier, reducing boilerplate code.
* `DatabaseManager.java`: Acts as a Data Access Object (DAO). Separates database logic (SQL queries, JDBC connections) from the business logic.
* `Main.java`: Handles the UI logic and user inputs.
