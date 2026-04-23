# EduPulse: Gamifying the Classroom Experience 🎓🏆

EduPulse is a modern, high-fidelity **Classroom Management System** designed to bridge the gap between traditional teaching and interactive digital learning. Built with a focus on engagement, it transforms standard classroom tasks into a rewarding, gamified journey for students.

---

## 🛑 The Problem
Traditional online classrooms often feel static and uninspiring. Teachers struggle with:
- **Low Student Engagement:** Static PDFs and assignments often result in low motivation.
- **Scattered Communication:** Using multiple apps for quizzes, materials, and discussions leads to confusion.
- **Lack of Immediate Feedback:** Students often don't see their progress until the end of a semester.

## ✅ Our Solution: EduPulse
EduPulse solves these issues by centralizing everything in one place and adding a **Gamification Layer**. Every quiz solved and every resource read earns the student **Experience Points (XP)**, turning learning into a quest for knowledge and rank.

---

## 🛠️ Technology Stack
We used industry-standard frameworks to ensure the application is fast, secure, and scalable:
- **Backend:** [Java 25](https://www.oracle.com/java/) with **Spring Boot 4.0** (The engine of the app).
- **Security:** **Spring Security** (Handles logins and protects private data).
- **Database:** **H2 Database** with **Spring Data JPA** (Stores users, quizzes, and messages).
- **Frontend:** **Vanilla HTML5, CSS3, and JavaScript** (Modern UI without the bloat).
- **Real-Time:** **RESTful APIs & Polling** (Powers the real-time Discussion Hub).
- **Gamification:** Custom XP Logic & **Canvas Confetti API**.

---

## 🏗️ Core Computer Science Concepts

### 🧩 Object-Oriented Programming (OOP)
The entire system is built on solid OOP principles to ensure the code is clean and reusable:
- **Encapsulation:** We use **Classes** (like `User`, `Quiz`, `ChatMessage`) with private data fields. We use **Lombok** to manage these fields securely.
- **Abstraction:** The `Service` layer (e.g., `QuizService`) hides the complex logic of calculating scores and XP from the user interface.
- **Polymorphism:** We use **Interfaces** (like `JpaRepository`) which allow the system to interact with the database in a flexible, plug-and-play manner.
- **Modularity:** Each feature (Chat, Quizzes, Auth) is isolated into its own package, making the app easy to maintain and expand.

### 🧵 Multi-threading & Concurrency
To ensure a smooth user experience, the app handles multiple tasks at once:
- **Server-Side Concurrency:** **Spring Boot** uses a multi-threaded embedded **Tomcat server**. This allows multiple students to take quizzes and chat simultaneously without the server slowing down.
- **Frontend Asynchrony:** The **Discussion Hub** uses asynchronous JavaScript (`fetch` and `setInterval`) to poll for new messages in the background without freezing the webpage.
- **Desktop UI Threads:** The **Swing-based Teacher Dashboard** runs on the **Event Dispatch Thread (EDT)**, ensuring the interface remains responsive while background data is being fetched.

---

## 🌟 Key Features

### 👨‍🏫 For Teachers (The Architect)
- **Classroom Creation:** Instantly create rooms with unique 8-character join codes.
- **Resource Hub:** Drag & drop files (PDFs, PPTs) to share with the entire class.
- **Interactive Quiz Builder:** Create multi-choice quizzes with time limits and entry passwords.
- **Live Analytics:** Track student performance in real-time with automatic leaderboards.
- **Discussion Hub:** Moderated group chats and direct student support.

### 👨‍🎓 For Students (The Scholar)
- **Gamified Dashboard:** Watch your **XP bar** grow and **Level Up** as you learn.
- **Achievement Titles:** Progress from a *Novice* to a *Quiz Grandmaster*.
- **Assessment Portal:** Take timed quizzes with high-visibility countdowns and instant results.
- **Confetti Rewards:** Celebrate every successful submission with interactive visual rewards.
- **Peer Discussion:** Chat with classmates or ask the teacher questions directly in the app.

---

## 🖥️ How the Website Works (Simple Guide)

1. **The Entry:** Users land on a premium login page. They can choose to register as a Teacher or a Student.
2. **The Dashboard:**
   - **Teachers** see their classrooms. Clicking one opens the "Management View" where they upload files or check grades.
   - **Students** see their XP progress at the top. They can join a classroom using a code shared by their teacher.
3. **The Learning Flow:**
   - Teacher uploads a PDF -> Student gets it instantly.
   - Teacher publishes a Quiz -> Student enters the password, finishes it, and gets a **Confetti Celebration** along with **+500 XP**.
4. **The Discussion:** Both parties can switch to the "Discussion Hub" to chat, share extra help files, or use emojis to stay connected.

---

## 🚀 Getting Started

### Prerequisites
- Java 21 installed.
- A modern web browser.

### Running Locally
1. Clone the repository:
   ```bash
   git clone https://github.com/chakriroyal2007-star/JavaMiniProject.git
   ```
2. Run the application using Gradle:
   ```bash
   ./gradlew bootRun
   ```
3. Open your browser and navigate to:
   ```
   http://localhost:8080
   ```

---

*Made with ❤️ for CSE-A Classroom by Rahul and Team.*
