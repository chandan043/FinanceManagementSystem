# 💼 Finance Management System (FMS)

The Finance Management System is a Spring Boot–based REST API that allows users to manage their finances efficiently. The backend application is designed to support CRUD operations for users, incomes, expenses, and financial goals, with the ability to track progress, view dashboard statistics, and analyze spending behavior by category.

---
## 🚀 Key Features

- ✅ **User Signup** with auto-created income & goal structure
- 💰 **Income Management** (multiple sources per user)
- 💸 **Expense Tracking** with categorized spending
- 🎯 **Goal Tracking** including creation, deletion, and status updates
- 📈 **Dashboard** summaries for income, expenses, and goal progress
- ✅ **User Account Lifecycle**: create and delete user + all related data
- 📆 **Date-Based Reporting**: view income/expenses within a date range
- 🗂️ **Expense Analytics**: category-wise spending percentages

---

## 🛠️ Tech Stack

| Layer       | Technology           |
|-------------|----------------------|
| Backend     | Java, Spring Boot    |
| Frontend    | React (with Tailwind or MUI) |
| Database    | H2 / PostgreSQL      |
| Build Tool  | Maven                |
| Validation  | Jakarta Validation   |

---

## 🧩 Core Features

### 👤 User APIs
- `POST /fms/add-user-and-associated-records`
- `DELETE /fms/delete-user-and-all-associated-data/{email}`

### 🎯 Goal APIs
- `POST /fms/create-goal/{userEmail}`
- `PUT /fms/update-goal-status/{userEmail}/{goalName}/{status}`
- `GET /fms/get-goals-progress-of-a-user/{userEmail}`
- `DELETE /fms/delete-goal-of-a-user/{userEmail}/{goalName}`

### 💰 Income APIs
- `POST /fms/add-income-source-to-user/{userEmail}`
- `PUT /fms/update-income-by-email/{userEmail}`
- `DELETE /fms/delete-income-by-email/{userEmail}/{source}`

### 💸 Expense APIs
- `POST /fms/add-expense-to-user/{userEmail}`
- `PUT /fms/update-expense-by-user-email/{userEmail}`
- `DELETE /fms/delete-expense-by-name-and-date/{userEmail}/{expenseName}/{targetDate}`

### 📆 Date-Based Reports
- `GET /fms/get-income-and-expenses-by-date-range/{userEmail}/{startDate}/{endDate}`

### 📊 Expense Analysis
- `GET /fms/get-Expenses-percentage-By-Category/{userEmail}`

### 📈 Dashboard
- `GET /fms/dashboard/{userEmail}`

---

## 🏗️ Database Tables & Relationships

### 🔗 Entity Relationships

1️⃣ **Users (users)**  
Primary Key: `email`  
Attributes: `firstName`, `lastName`, `password`, etc.  
Relations:  
- One-to-Many with: `goals`, `incomes`, `expenses`

2️⃣ **Goals (goals)**  
Primary Key: `goalId`  
Foreign Key: `userEmail → users(email)`  
Attributes: `goalName`, `targetAmount`, `status`, etc.

3️⃣ **Incomes (incomes)**  
Primary Key: `incomeId`  
Foreign Key: `userEmail → users(email)`  
Attributes: `source`, `amount`, `incomeDate`, etc.

4️⃣ **Expenses (expenses)**  
Primary Key: `expenseId`  
Foreign Key: `userEmail → users(email)`  
Attributes: `expenseName`, `amount`, `category`, `expenseDate`, etc.

5️⃣ **ExpenseCategory (enum)**  
Used in: `Expense`  
Represents categories like FOOD, TRANSPORT, ENTERTAINMENT, etc.

---

## ⚙️ Setup Instructions

**1️⃣ Clone the Repository**

```
git clone https://github.com/your-username/fms-backend.git
cd fms-backend
```
**2️⃣ Build and Run the Application**
```
mvn clean install
mvn spring-boot:run
```
**3️⃣ Access Endpoints via Postman or Swagger (if integrated)**

---

## 📊 Example Use Case
- A user signs up.
- The system auto-creates a default income/goal structure.
- The user adds income sources (job, freelance).
- They log expenses (rent, groceries, gym) with categories.
- The user defines goals (save for vacation).
- Dashboard updates show financial health and progress.
- Expenses are analyzed by category to help with budgeting.
  
## 🔮 Future Enhancements
- 🔐 JWT authentication
- 👨‍💻 Frontend UI in React.js (optional Tailwind/MUI)
- 📨 Monthly report emails
- 📅 PDF export for financial summaries
- 📊 Charts via Chart.js or Recharts
  
### 🙌 Contribution

Feel free to fork the repo, create pull requests, or open issues for feature requests and bugs.

### 📝 License

Licensed under the MIT License.


