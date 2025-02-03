# Gym Planner Mobile App

**Gym Planner** is a Kotlin Multiplatform project that provides end users with the ability to:

- Create personal training bookings
- View upcoming fitness classes organized by the gym
- Chat with a member of staff (**under development**)

This app leverages the power of Kotlin Multiplatform to provide a consistent experience across platforms while delivering essential gym management features.

---

## Technologies Used

- **Kotlin**: A modern, expressive, and concise language that works across multiple platforms.
- **Coroutines**: For asynchronous programming, allowing efficient background task management with structured concurrency.
- **Ktor**: A lightweight framework for building RESTful APIs, used for network communication in this app.
- **Compose**: A declarative UI framework for building native user interfaces with Kotlin.
- **MockK**: A Kotlin-first mocking library used for unit testing and mocking dependencies.

---

## Features

- **Personal Training Booking**: Allows users to check availability and book personal trainers.
- **Fitness Classes Schedule**: View upcoming fitness classes organized by the gym.
- **Live Chat**: Communicate with a gym staff member for support (**under development**).

---

## Services

This app integrates with a backend **RESTful service** that handles various gym operations, such as:

- Booking management
- Fitness class schedules
- Chat functionality (under development)

The backend is maintained in the [GymPlannerService repository](https://github.com/IanArb/GymPlannerService).

---

## Getting Started

### Prerequisites

To run the backend service locally, you need:

1. **ngrok Account**: Create an account with [ngrok](https://ngrok.com/). Ngrok allows you to expose your local backend service to the public internet.

2. **Spring Boot Backend**: Run the backend Spring Boot project locally on port `8080`.

   ```bash
   ./gradlew bootRun
   ```

3. Expose the Backend with ngrok: In a separate terminal window, run ngrok to expose your backend service publicly.

```bash
ngrok http 8080
```

Ngrok will provide a public URL, for example:

```bash
https://abcd1234.ngrok.io
```

4. Use this public URL in your app to access the backend service.

Contributing
We welcome contributions to improve the app! Feel free to fork the repository and submit pull requests.

License
This project is licensed under the MIT License.
