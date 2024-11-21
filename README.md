# Hospital Management System

## Overview
The **Hospital Management System** is a software solution designed to manage various operations within a hospital, such as patient management, appointment scheduling, billing, staff management, and medicine inventory tracking. The system supports multiple user roles (Administrator, Manager, Staff) to streamline workflows and ensure effective data management.

---

## Features

- **User Management**
  - Create, update, and manage users with roles such as Administrator, Manager, and Staff.

- **Appointment Management**
  - Schedule, update, and cancel appointments.
  - Track patient appointment history.

- **Patient and Staff Records**
  - Maintain detailed records of patients, including medical histories.
  - Manage staff roles and employee information.

- **Billing System**
  - Generate, update, and manage patient billing records.

- **Medicine Inventory**
  - Track medicine stock levels and restock as needed.
  - Maintain detailed records of medicine information, including expiration dates.

- **Request Management**
  - Handle staff requests for replenishing medical supplies or resources.

- **Utility Tools**
  - Provide debugging, login management, and common helper functions.

---

## Project Structure
> '''
Hospital Management System/ │   
├── Users/ # User management module   
├── appointment/ # Appointment scheduling and management   
├── bin/ # Core application logic with DIP implementation   
├── enums/ # Enumerations for user roles, status codes, etc.   
├── managers/ # Manager-specific operations   
├── utility/ # Helper utilities and debugging tools   
├── Bills.csv # Billing details   
├── Medicine_List.csv # Medicine inventory details   
├── Patient_List.csv # Primary patient record   
├── Staff_List.csv # Primary staff record   
├── Replenish_Request_List.csv # Requests for resource replenishment   
└── README.md # Project documentation
'''
