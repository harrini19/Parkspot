# ParkSpot — Society Visitor Parking Slot Tracker

> A smart, web-based visitor parking management system designed to digitize and streamline parking operations within residential societies.

## Overview

**ParkSpot** is a Spring Boot–based visitor parking management system developed to replace manual parking registers used in residential societies.

The system enables security personnel or society administrators to record visitor vehicle entries, assign available parking slots, track active vehicles, process vehicle exits, and maintain a daily visitor log.

ParkSpot also enforces parking rules at the application level, ensuring that an occupied parking slot cannot be assigned to another vehicle and that the same vehicle cannot maintain multiple active visits simultaneously.

---

## Problem Statement

Residential societies often manage visitor parking using physical registers or informal tracking methods. This can result in:

- Difficulty tracking currently parked visitor vehicles
- Duplicate allocation of parking slots
- Inaccurate or incomplete entry and exit records
- Time-consuming manual verification
- Poor visibility of daily visitor activity
- Difficulty maintaining historical parking records

**ParkSpot** provides a centralized digital solution for these challenges.

---

## Key Features

### 🚗 Visitor Entry Management
- Register visitor vehicle details
- Record the flat being visited
- Assign an available parking slot
- Automatically record the entry time

### 🅿️ Parking Slot Management
- Track available and occupied slots
- Prevent allocation of an already occupied slot
- Automatically make a slot available after vehicle exit

### 🚪 Vehicle Exit Management
- Record vehicle exit time
- Release the associated parking slot
- Prevent invalid or duplicate exit operations

### 📋 Visitor Tracking
- View currently occupied parking slots
- View the daily visitor log
- Track entry and exit timestamps

### 🔐 Business Rule Validation
ParkSpot validates important parking rules at the service layer:

- An occupied slot cannot be assigned again
- A vehicle with an active visit cannot be registered again
- A vehicle cannot be exited more than once
- Exit time cannot be earlier than entry time
- Referenced flats and parking slots must exist
- Invalid requests are rejected with appropriate HTTP responses

### ⚠️ Global Exception Handling
Centralized exception handling provides consistent and meaningful API error responses for:

- Resource not found
- Business rule violations
- Validation failures
- Invalid requests

---

## System Architecture

ParkSpot follows a layered Spring Boot architecture:

```text
                ┌──────────────────────┐
                │      Web UI /        │
                │   REST API Clients   │
                └──────────┬───────────┘
                           │
                           ▼
                ┌──────────────────────┐
                │     Controllers      │
                └──────────┬───────────┘
                           │
                           ▼
                ┌──────────────────────┐
                │       Services       │
                │  Business Rules &    │
                │     Validation       │
                └──────────┬───────────┘
                           │
                           ▼
                ┌──────────────────────┐
                │     Repositories     │
                │      Spring Data     │
                │         JPA          │
                └──────────┬───────────┘
                           │
                           ▼
                ┌──────────────────────┐
                │        MySQL         │
                │      Database        │
                └──────────────────────┘
