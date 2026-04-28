# Bank Queue System
Final project for 2201 - Object Oriented Programming
# 🏦 Bank Queue Management System

![Language](https://img.shields.io/badge/Language-[JAVA]-blue.svg)
![Topic](https://img.shields.io/badge/Topic-Advanced%20Programming/-brightgreen.svg)
![Topic](https://img.shields.io/badge/Topic-System%20Simulation-orange.svg)

A robust command-line application that simulates a real-world bank queueing system. This project demonstrates the practical implementation of core Data Structures—specifically **Queues (FIFO)**—to manage customer flow, optimize waiting times, and handle teller assignments efficiently.

## 🧠 About the Project

In a real bank, handling customer requests in the order they arrive is critical for efficiency and fairness. This system simulates that environment by programmatically managing customer arrivals and departures. It showcases how to build and manipulate fundamental data structures from scratch (without relying purely on high-level built-in libraries), emphasizing memory management and algorithmic efficiency.

### ✨ Key Features
* **FIFO Implementation:** Strictly follows the First-In-First-Out principle using a custom-built Queue data structure.
* **Customer Management:** Simulates enqueueing (adding customers to the line) and dequeueing (serving customers) seamlessly.
* **Real-time Queue Tracking:** Displays the current state of the queue, including the number of waiting customers and the next person in line.
* **Dynamic Data Allocation:** Handles an arbitrary number of customers dynamically, ensuring optimal memory usage without fixed limits.

## 🛠️ Technologies & Core Concepts
* **Language:** `[Insert Language, e.g., C++ / Java / Python]`
* **Data Structures:** Queues, Linked Lists / Arrays (depending on implementation), Nodes.
* **Concepts:** System Simulation, Time Complexity ($O(1)$ Enqueue/Dequeue operations), Resource Management.

## 🚀 Getting Started

Follow these instructions to get a copy of the project up and running on your local machine.

### Prerequisites
Make sure you have the appropriate compiler/environment installed, such as `[Insert requirement, e.g., GCC for C++ or JDK for Java]`.

### Installation
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/AnasAbdelraouf128/Bank-System-Queue-System.git](https://github.com/AnasAbdelraouf128/Bank-System-Queue-System.git)



=========================================
      BANK QUEUE MANAGEMENT SYSTEM       
=========================================

[1] Add Customer to Queue (Enqueue)
[2] Serve Next Customer (Dequeue)
[3] View Current Queue
[4] Exit

Select an option: 1
-> Enter Customer Name/ID: CUST-001
[SUCCESS] CUST-001 added to the queue.

Select an option: 1
-> Enter Customer Name/ID: CUST-002
[SUCCESS] CUST-002 added to the queue.

Select an option: 3
-> Current Queue: [Front] CUST-001 -> CUST-002 [Rear]
-> Total waiting: 2

Select an option: 2
[SERVED] CUST-001 has been served and removed from the queue.


