# Sports Equipment Management System

A JavaFX-based desktop application for managing sports equipment inventory, requests, and transactions.

## Project Overview

This system is designed to manage sports equipment across educational institutions, allowing students, teachers, and sports heads to manage equipment borrowing, returning, and inventory tracking.

## Group Members
- Ramsha Khalid (24P-0522)
- Ayesha Zulfiqar (24P-0663)
- Tayyaba Asif (24P-0579)
- Marwa Mushtaq (24P-0556)

**Section:** CS-4B

## Technologies Used

- **Language:** Java 11+
- **GUI Framework:** JavaFX 21.0.1
- **Data Persistence:** JSON (Gson)
- **Build Tool:** Maven

## Features Implemented

### Core Use Cases 

1. **User Registration & Authentication**
   - Register new users (Student, Teacher, Sports Head)
   - Secure login system
   - Role-based access control

2. **Search Equipment**
   - Search by name, description, or category
   - Filter by category and availability
   - View equipment details

3. **Request Equipment**
   - Students/Teachers can request available equipment
   - Specify quantity and duration
   - Track request status

4. **Approve/Reject Requests**
   - Sports heads review pending requests
   - Approve with specified loan duration
   - Reject with reason
   - Automatic equipment issuance on approval

5. **Return Equipment**
   - Users can return borrowed equipment
   - Report damaged equipment
   - Automatic penalty calculation for late returns

### Additional Features

6. **Add Equipment**
   - Sports heads can add new equipment to inventory
   - Specify category, quantity, and description

7. **View Inventory**
   - Complete equipment catalog
   - Edit equipment details
   - Delete unused equipment
   - Filter by category and availability

8. **View Transactions**
   - Complete transaction history
   - Track issued vs returned items
   - Identify overdue equipment
   - Calculate penalties

9. **View All Requests**
   - Sports heads can see all requests (any status)
   - Filter by request status

## Project Structure

```
SportsEquipmentSystem/
├── src/main/java/com/sports/equipment/
│   ├── SportsEquipmentApp.java (Main application entry)
│   ├── model/
│   │   ├── User.java
│   │   ├── Equipment.java
│   │   ├── EquipmentRequest.java
│   │   ├── Transaction.java
│   │   └── MaintenanceRecord.java
│   ├── service/
│   │   ├── UserService.java
│   │   ├── EquipmentService.java
│   │   ├── RequestService.java
│   │   └── TransactionService.java
│   ├── ui/
│   │   ├── LoginWindow.java
│   │   ├── DashboardWindow.java
│   │   ├── SearchEquipmentWindow.java
│   │   ├── MyRequestsWindow.java
│   │   ├── ReturnEquipmentWindow.java
│   │   ├── AddEquipmentWindow.java
│   │   ├── ApproveRequestsWindow.java
│   │   ├── AllRequestsWindow.java
│   │   ├── InventoryWindow.java
│   │   └── TransactionsWindow.java
│   └── util/
│       ├── DataPersistence.java
│       └── IDGenerator.java
├── data/
│   ├── users.json
│   ├── equipment.json
│   ├── requests.json
│   ├── transactions.json
│   └── maintenance.json
├── pom.xml
└── README.md
```

## Design Patterns Used

1. **MVC Architecture**: Separation of Model (entities), View (UI), and Controller (services)
2. **Service Layer Pattern**: Business logic isolated in service classes
3. **DAO Pattern**: DataPersistence class handles all data operations
4. **Factory Pattern**: IDGenerator creates unique IDs for entities
5. **Singleton Pattern**: Implicit through static service access

## Class Hierarchy

### Entity Relationships

```
User
  ├─ Student/Teacher/Sports Head (via UserRole enum)
  └─ Associated with requests and transactions

Equipment
  ├─ Contains category information
  └─ Tracked in inventory with status

EquipmentRequest
  ├─ Created by users
  ├─ Approved/Rejected by sports heads
  └─ Converts to Transaction upon approval

Transaction
  ├─ Represents issued equipment
  ├─ Tracks issue/return dates
  └─ Calculates penalties
```

## Key Features Implementation

### 1. Role-Based Access Control
- Students/Teachers: Search, Request, View requests, Return equipment
- Sports Heads: All student/teacher features + Add equipment, Approve requests, View inventory, Track transactions

### 2. Penalty Calculation
- Rs. 10 per day late
- Rs. 50 for damaged equipment
- Cumulative penalties possible

### 3. Equipment Tracking
- Status management (Available, Under Maintenance, Damaged, Retired)
- Quantity tracking (total vs available)
- Category organization

### 4. Data Persistence
- JSON-based storage in `/data/` directory
- Automatic serialization/deserialization
- Data preserved between sessions

## Building and Running

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- Maven 3.6+
- JavaFX SDK 21.0.1

### Build
```bash
cd SportsEquipmentSystem
mvn clean package
```

### Run
```bash
mvn javafx:run
```

Or via IDE (right-click SportsEquipmentApp.java => Run)

## Demo Accounts

For testing purposes, you can create accounts during registration, or modify the users.json file directly.

Example user creation flow:
1. Click "Register" tab
2. Enter details (e.g., "Ali", "Ali@college.edu.pk", "ali", "pass123")
3. Select role (STUDENT, TEACHER, or SPORTS_HEAD)
4. Click Register
5. Use created credentials to login

## Functional Requirements Coverage

The implementation covers 13+ functional requirements including:
- User registration for students, teachers, and sports heads
- Login system with authentication
- Sports heads can add/manage equipment
- Equipment categorization
- Quantity tracking
- Equipment requests workflow
- Request approval/rejection
- Equipment issue/return tracking
- Late return penalty calculation
- Equipment search and filtering
- Transaction history viewing
- Multi-user support

## Non-Functional Requirements Coverage

- **Performance**: Fast response time for all operations (< 2 seconds)
- **Scalability**: Designed to handle growth in users and equipment
- **Usability**: Simple, intuitive GUI with role-based navigation
- **Reliability**: Data persistence ensures consistency
- **Data Integrity**: JSON validation and error handling
- **Compatibility**: Runs on Windows, Mac, and Linux with Java installed

## Future Enhancements

1. Add maintenance record tracking UI
2. User warning system for frequent damage
3. Email notifications for request status
4. Report generation and analytics
5. Database backend (SQLite/MySQL) instead of JSON
6. Rest API for mobile app integration
7. User dashboard with statistics
8. Equipment reservation system
9. QR code scanning for equipment check-in/out
10. Multi-language support

## Error Handling

The application includes error handling for:
- Invalid credentials
- Duplicate user registration
- Insufficient equipment quantity
- Missing data fields
- Invalid user input
- File I/O errors

## Testing Recommendations

1. **Registration Testing**: Create multiple user roles and verify access
2. **Request Workflow**: Test complete request → approval → issue → return cycle
3. **Penalty Calculation**: Verify penalties for late returns and damage
4. **Search & Filter**: Test various search keywords and filters
5. **Data Persistence**: Restart app and verify data is retained
6. **Role-Based Access**: Verify Sports Head features are hidden from regular users

## Known Limitations

1. Single machine deployment (no network sync)
2. JSON file-based storage (not suitable for large datasets)
3. No concurrent user support
4. No advanced reporting features
5. Limited validation for input fields

## Learning Outcomes

This project demonstrates:
- Object-oriented design principles
- JavaFX GUI development
- JSON data persistence
- Service layer architecture
- Role-based access control
- Business logic implementation
- MVC architecture in desktop applications

## Submission Details

- **Format**: GitHub repository (public)
- **Language**: Java
- **IDE Recommended**: IntelliJ IDEA or Visual Studio
- **Date**: 01/05/2026
- **Course**: Software Design & Analysis
