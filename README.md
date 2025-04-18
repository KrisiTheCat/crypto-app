## 🧪 How to Run the Project

### 📦 Prerequisites

- **Java 17+**
- **Node.js + npm** 
- **MySQL** 
- **Maven**

---

### 🔧 Backend Setup

1. **Clone the repository** and navigate to the backend folder:

``` bash
git clone https://github.com/KrisiTheCat/crypto.git
cd crypto/backend
```

2. **Configure your database** settings in _src/main/resources/application.properties_:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. **Import** the database schema:

You can directly import the provided crypto_ms.sql file via phpMyAdmin:
- Open phpMyAdmin and select or create a new database.
- Go to the Import tab.
- Upload and run the crypto_ms.sql file.

4. **Start** the backend server:
You can run the code directly from the IDE or use this command: 
```bash
mvn spring-boot:run
```

---

### 🌐 Frontend Setup

1. **Navigate** to the frontend folder:

```bash
cd ../frontend
```

2. **Run** the React.js application

```bash
npm install
npm run start 
```
 
 ---

 ## 📷 Screenshots

 You can find some screenshots of the app in the /screenshots folder
 ![alternative](https://github.com/KrisiTheCat/crypto-app/main/screenshots/homepage.png)
