# Aashray – AI-Enabled Microservices-Based Social Welfare Platform

## Status: Phase 6 of 7 complete

This is a full microservices platform. Given its real size, it's being built in verified phases rather than as one unreviewable dump. Each phase is real, compiling, runnable code — not a stub.

| Phase | Scope | Status |
|---|---|---|
| 1 | Eureka Server, API Gateway (JWT filter + routing), Auth Service (JWT auth, roles, admin user mgmt, RabbitMQ producer), MySQL schema, Docker Compose | ✅ Done |
| 2 | Food Donation Service (donor/NGO/admin flows) + RabbitMQ producer wiring | ✅ Done |
| 3 | Monetary Donation Service + Notification Service (RabbitMQ consumer, email) | ✅ Done |
| 4 | Education Support Service + Volunteer Service | ✅ Done |
| 5 | AI Chatbot endpoint (Gemini/OpenAI) | ✅ Done |
| 6 | React frontend (auth, role-based routing, all dashboards) | ✅ Done |
| 7 | Full docker-compose integration, Postman collection, Swagger consolidation, deployment docs | ⏳ Next |

Say "continue with phase 7" when ready and it'll be built the same way: real code, in this same repo structure, on top of what's already here.

## What's in Phase 1

```
aashray/
├── pom.xml                    # Maven reactor parent
├── docker-compose.yml         # mysql, rabbitmq, eureka, gateway, auth-service
├── eureka-server/             # Service discovery (port 8761)
├── api-gateway/               # Spring Cloud Gateway + JWT validation filter (port 8080)
└── auth-service/              # Registration, login, profile, admin user mgmt (port 8081)
    ├── entity/User.java, Role.java
    ├── dto/                   # records + response DTOs, ApiResponse<T> envelope
    ├── repository/UserRepository.java
    ├── security/               # JwtService, JwtAuthenticationFilter, CustomUserDetails(Service)
    ├── service/AuthService, UserService, EventPublisherService
    ├── controller/AuthController, UserController, AdminUserController
    ├── config/                # SecurityConfig, SwaggerConfig, RabbitMQConfig, JpaAuditingConfig
    ├── exception/              # GlobalExceptionHandler + custom exceptions
    └── resources/
        ├── application.yml
        └── db/schema.sql       # reference schema (Hibernate ddl-auto=update creates it live)
```

## Phase 2 additions

```
food-donation-service/          # port 8082
├── entity/FoodDonation.java, FoodType.java, DonationStatus.java
├── dto/                        # Create/Update requests, DonationResponse, DonationStatusEvent
├── repository/FoodDonationRepository.java
├── security/                   # stateless JWT validation (same shared secret, no DB lookup)
├── service/FoodDonationService, EventPublisherService
├── controller/FoodDonationController.java
├── config/                     # SecurityConfig (@PreAuthorize), RabbitMQConfig, SwaggerConfig
└── exception/                  # GlobalExceptionHandler + custom exceptions
```

### Food Donation Service API

| Method | Endpoint | Role | Description |
|---|---|---|---|
| POST | `/api/food-donations` | DONOR | Create a donation |
| PUT | `/api/food-donations/{id}` | DONOR (owner) | Edit own PENDING donation |
| DELETE | `/api/food-donations/{id}` | DONOR (owner) | Delete own PENDING donation |
| GET | `/api/food-donations/my-donations` | DONOR | Own donation history |
| GET | `/api/food-donations/available` | NGO | View PENDING donations |
| POST | `/api/food-donations/{id}/accept` | NGO | Accept a donation |
| POST | `/api/food-donations/{id}/reject` | NGO | Reject a donation |
| PATCH | `/api/food-donations/{id}/pickup` | NGO (assigned) | Mark PICKED_UP |
| PATCH | `/api/food-donations/{id}/delivered` | NGO (assigned) | Mark DELIVERED |
| GET | `/api/food-donations/ngo/history` | NGO | Donations this NGO handled |
| GET | `/api/food-donations/admin/all` | ADMIN | All donations |
| GET | `/api/food-donations/admin/stats` | ADMIN | Counts by status |
| GET | `/api/food-donations/admin/recent?limit=` | ADMIN | Recent donations |

State machine: `PENDING → ACCEPTED → PICKED_UP → DELIVERED`. Only the donor can edit/delete while PENDING; only the accepting NGO can advance pickup/delivered status.

On `accept` / `pickup` / `delivered`, the service publishes `donation.accepted`, `donation.picked_up`, `donation.delivered` events to the shared `aashray.notification.exchange` — the Notification Service consumes these for donor/NGO email alerts.

**Pickup location (Leaflet + OpenStreetMap).** `CreateDonationRequest`/`UpdateDonationRequest` accept optional `latitude`/`longitude` alongside `pickupAddress`; `DonationResponse` returns them. They're optional on purpose — a donor who never opens the map can still submit with a typed address only, same as before this feature. On the frontend, `LocationPicker` is a "Select Location From Map" button that opens a Leaflet map in a modal — click anywhere to drop a pin, drag it to fine-tune, then Confirm to send just the coordinates back to the form (no address lookup, no API key). `LocationMapModal` shows a read-only map + "Get Directions" link wherever a donation with coordinates is viewed (donor's own list, NGO's available/history views, admin's all-donations view). Both use `react-leaflet` with plain OpenStreetMap tiles — no API key, no billing account, no setup beyond `npm install`.

Swagger UI: `http://localhost:8082/swagger-ui.html`

## Phase 3 additions

```
monetary-donation-service/      # port 8083
├── entity/MonetaryDonation.java, PaymentStatus.java
├── dto/                        # CreateDonationRequest, DonationResponse, MonetaryDonationEvent
├── repository/MonetaryDonationRepository.java
├── security/                   # stateless JWT validation, same pattern as food-donation-service
├── service/MonetaryDonationService, EventPublisherService
├── controller/MonetaryDonationController.java
├── config/                     # SecurityConfig (@PreAuthorize), RabbitMQConfig, SwaggerConfig
└── exception/                  # GlobalExceptionHandler + custom exceptions

notification-service/           # port 8084
├── entity/NotificationLog.java, NotificationStatus.java
├── dto/                        # consumer-side mirrors of every producer's event payload
├── repository/NotificationLogRepository.java
├── security/                   # JWT validation, guards the Admin log endpoints
├── service/EmailService (send-or-simulate), NotificationLogService
├── listener/                   # @RabbitListener per event: UserRegistered, FoodDonation*, MonetaryDonation*
├── controller/NotificationAdminController.java
├── config/                     # SecurityConfig, RabbitMQConfig (consumer side), SwaggerConfig
└── exception/GlobalExceptionHandler.java
```

### Monetary Donation Service API

| Method | Endpoint | Role | Description |
|---|---|---|---|
| POST | `/api/monetary-donations/create-order` | DONOR | Step 1: creates a Razorpay order + local `PENDING` donation, returns everything Checkout needs |
| POST | `/api/monetary-donations/verify-payment` | DONOR (owner) | Step 2: verifies the Razorpay signature server-side; only this can move a donation to `SUCCESS` |
| POST | `/api/monetary-donations/payment-failed` | DONOR (owner) | Reports a failed/abandoned checkout so the donation doesn't stay `PENDING` forever |
| GET | `/api/monetary-donations/my-donations` | DONOR | Own donation history |
| GET | `/api/monetary-donations/{id}` | DONOR (owner) | View a single own transaction |
| GET | `/api/monetary-donations/admin/all` | ADMIN | All monetary donations |
| GET | `/api/monetary-donations/admin/stats` | ADMIN | Counts by status (incl. `refunded`) + total amount raised |
| GET | `/api/monetary-donations/admin/recent?limit=` | ADMIN | Recent donations |

**Real Razorpay integration.** The frontend never gets to say a payment succeeded — `verify-payment` recomputes the HMAC-SHA256 signature from `razorpayOrderId|razorpayPaymentId` using the key secret and only accepts the payment if it matches exactly what Razorpay signed. The service talks to Razorpay's REST API directly (`POST /v1/orders`, `GET /v1/payments/{id}`) rather than the SDK — see `RazorpayService`. On verified `SUCCESS`, it publishes a `monetary.donation.success` event to `aashray.notification.exchange` for the receipt email, same as before.

**Setup:** get a test-mode key pair from the [Razorpay Dashboard](https://dashboard.razorpay.com/app/keys) and set `RAZORPAY_KEY_ID` / `RAZORPAY_KEY_SECRET` as environment variables (or in `docker-compose.yml`) before starting `monetary-donation-service`. Without real keys, `create-order` will fail with a 502 the moment it tries to reach Razorpay.

Swagger UI: `http://localhost:8083/swagger-ui.html`

### Notification Service

Pure RabbitMQ consumer (plus a small Admin audit-log API) — no donor/NGO-facing REST endpoints. It listens on every queue bound to `aashray.notification.exchange`:

| Queue | Published by | Triggers |
|---|---|---|
| `user.registered.queue` | auth-service | Welcome email |
| `donation.accepted.queue` | food-donation-service | "Donation accepted" email |
| `donation.picked_up.queue` | food-donation-service | "Donation picked up" email |
| `donation.delivered.queue` | food-donation-service | "Donation delivered" email |
| `monetary.donation.success.queue` | monetary-donation-service | Donation receipt email |

**Email dispatch is opt-in.** By default (`EMAIL_ENABLED=false`, no SMTP credentials needed) every notification is logged and persisted to `notification_logs` with status `SIMULATED`, so the full event-driven flow — publish → consume → "send" — is demoable and auditable without a real mailbox. Set `EMAIL_ENABLED=true` and provide `MAIL_USERNAME` / `MAIL_PASSWORD` (e.g. a Gmail App Password) to dispatch real emails via `JavaMailSender`.

**Known scope limitation:** `food-donation-service`'s events carry donor/NGO names and ids but not email addresses (that data lives only in `auth-service`, and food-donation-service intentionally never stores it). Those three donation-lifecycle notifications are therefore always logged with an `unresolved:<id>:<name>` recipient rather than dispatched — a production build would add an internal Auth Service lookup here. `user.registered` and `monetary.donation.success` events do carry the real email and dispatch normally when `EMAIL_ENABLED=true`.

| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/api/notifications/admin/logs` | ADMIN | All notification logs |
| GET | `/api/notifications/admin/logs/event/{eventType}` | ADMIN | Logs filtered by event type |
| GET | `/api/notifications/admin/stats` | ADMIN | Counts by status (`SENT`/`SIMULATED`/`FAILED`) |

Swagger UI: `http://localhost:8084/swagger-ui.html`

**Phase 4 addition:** the service now also consumes `educator.assigned.queue` and `volunteer.assigned.queue` (published by Education Service and Volunteer Service respectively) via `EducationEventListener` / `VolunteerEventListener`. Same scope limitation as the food-donation listeners — names/ids only, so these log with an `unresolved:<id>:<name>` recipient rather than dispatching.

## Phase 4 additions

```
education-service/               # port 8085
├── entity/Student.java, StudentStatus.java, EducationAssignment.java, AssignmentStatus.java
├── dto/                          # student + assignment request/response DTOs, EducatorAssignedEvent
├── repository/StudentRepository.java, EducationAssignmentRepository.java
├── security/                     # stateless JWT validation, same pattern as food-donation-service
├── service/EducationService, EventPublisherService
├── controller/EducationController.java
├── config/                       # SecurityConfig (@PreAuthorize), RabbitMQConfig, SwaggerConfig
└── exception/                    # GlobalExceptionHandler + custom exceptions

volunteer-service/                # port 8086
├── entity/VolunteerTask.java, TaskStatus.java
├── dto/                          # AssignTaskRequest, TaskResponse, VolunteerAssignedEvent
├── repository/VolunteerTaskRepository.java
├── security/                     # stateless JWT validation, same pattern as food-donation-service
├── service/VolunteerTaskService, EventPublisherService
├── controller/VolunteerTaskController.java
├── config/                       # SecurityConfig (@PreAuthorize), RabbitMQConfig, SwaggerConfig
└── exception/                    # GlobalExceptionHandler + custom exceptions
```

### Education Support Service API

Educator and Student accounts are registered through `auth-service` (roles `EDUCATOR` / the NGO registering a `Student` record here). Since education-service never stores emails, the NGO supplies the educator's id/name directly when assigning — the same cross-service limitation documented above for donor/NGO names.

| Method | Endpoint | Role | Description |
|---|---|---|---|
| POST | `/api/education/students` | NGO | Register a new student |
| PUT | `/api/education/students/{id}` | NGO (owner) | Update an own student's details |
| GET | `/api/education/students/my-ngo` | NGO | Students registered by this NGO |
| POST | `/api/education/assignments` | NGO | Assign an educator to a registered student |
| PATCH | `/api/education/assignments/{id}/cancel` | NGO (owner) | Cancel an ACTIVE assignment |
| GET | `/api/education/assignments/ngo/history` | NGO | Assignments made by this NGO |
| GET | `/api/education/assignments/my-students` | EDUCATOR | Students assigned to this educator |
| PATCH | `/api/education/assignments/{id}/complete` | EDUCATOR (assigned) | Mark an assignment COMPLETED |
| GET | `/api/education/admin/students` | ADMIN | All registered students |
| GET | `/api/education/admin/assignments` | ADMIN | All educator assignments |
| GET | `/api/education/admin/stats` | ADMIN | Student/assignment counts |
| GET | `/api/education/admin/recent?limit=` | ADMIN | Recent assignments |

A student starts `UNASSIGNED`; assigning an educator moves it to `ASSIGNED`. Cancelling or completing the assignment reverts the student to `UNASSIGNED` if no other ACTIVE assignment exists for them. On assignment, the service publishes an `educator.assigned` event to `aashray.notification.exchange`.

Swagger UI: `http://localhost:8085/swagger-ui.html`

### Volunteer Service API

| Method | Endpoint | Role | Description |
|---|---|---|---|
| POST | `/api/volunteers/tasks` | NGO | Assign a task to a volunteer |
| PATCH | `/api/volunteers/tasks/{id}/cancel` | NGO (owner) | Cancel a non-terminal task |
| GET | `/api/volunteers/tasks/ngo/history` | NGO | Tasks assigned by this NGO |
| GET | `/api/volunteers/tasks/my-tasks` | VOLUNTEER | Own tasks (dashboard) |
| GET | `/api/volunteers/tasks/completed` | VOLUNTEER | Own completed task history |
| PATCH | `/api/volunteers/tasks/{id}/start` | VOLUNTEER (assigned) | Mark ASSIGNED → IN_PROGRESS |
| PATCH | `/api/volunteers/tasks/{id}/complete` | VOLUNTEER (assigned) | Mark IN_PROGRESS → COMPLETED |
| GET | `/api/volunteers/admin/all` | ADMIN | All volunteer tasks |
| GET | `/api/volunteers/admin/stats` | ADMIN | Task counts by status |
| GET | `/api/volunteers/admin/recent?limit=` | ADMIN | Recent tasks |

State machine: `ASSIGNED → IN_PROGRESS → COMPLETED`, with `CANCELLED` reachable by the assigning NGO from either non-terminal state. On assignment, the service publishes a `volunteer.assigned` event to `aashray.notification.exchange`.

Swagger UI: `http://localhost:8086/swagger-ui.html`

### Auth Service API

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register as Donor/NGO/Educator/Volunteer/Beneficiary/Admin |
| POST | `/api/auth/login` | Public | Returns JWT |
| GET | `/api/auth/profile` | Authenticated | Own profile |
| PUT | `/api/auth/profile` | Authenticated | Update own profile |
| GET | `/api/auth/admin/users` | ADMIN | List all users |
| GET | `/api/auth/admin/users/role/{role}` | ADMIN | Filter by role |
| PATCH | `/api/auth/admin/users/{id}/status?enabled=` | ADMIN | Enable/disable account |
| GET | `/api/auth/admin/dashboard/counts` | ADMIN | Role counts for Admin Dashboard |

Swagger UI: `http://localhost:8081/swagger-ui.html` (direct) — will also be reachable via the gateway once routes are finalized in Phase 7.

On successful registration, `auth-service` publishes a `user.registered` event to the `aashray.notification.exchange` topic exchange in RabbitMQ. The Notification Service (Phase 3) will consume this to send the welcome email — this decoupling is why registration never fails due to email issues.

## Phase 5 addition

```
chatbot-service/                  # port 8087
├── dto/
│   ├── ChatRequest.java, ChatResponse.java
│   └── gemini/                   # internal request/response shape for Gemini's generateContent API
├── security/                     # stateless JWT validation (same pattern as other services) — optional, not enforced
├── service/
│   ├── ChatbotService.java       # builds the system prompt, decides Gemini vs fallback
│   ├── GeminiClientService.java  # calls Gemini's REST API via a Spring RestClient
│   └── FaqFallbackService.java   # rule-based canned answers, used when no API key is set
├── controller/ChatbotController.java
├── config/                       # SecurityConfig (public endpoint), SwaggerConfig, GeminiProperties, GeminiClientConfig (RestClient bean)
└── exception/                    # GlobalExceptionHandler + ChatbotUpstreamException
```

No database and no RabbitMQ wiring for this service — it's stateless, matching its scope (answer a question, return an answer).

### Chatbot Service API

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/chatbot/chat` | Public (Bearer token optional) | Ask the AI assistant a question; returns a reply plus which mode answered it |

Request body: `{ "message": "How do I register as an NGO?" }` (`sessionId` is accepted but not yet used — no multi-turn memory in this phase). Response: `{ "reply": "...", "source": "gemini" | "faq-fallback", "respondedAt": "..." }`.

If a Bearer token is supplied and valid, the caller's role is folded into the system prompt so Gemini can tailor its answer (e.g. a Donor vs. an NGO); it's never required.

**Two response modes:**
- **`gemini`** — when `GEMINI_API_KEY` is set, every message is sent to Google's `gemini-1.5-flash` model (configurable via `GEMINI_MODEL`) with a system prompt describing Aashray's modules and roles.
- **`faq-fallback`** — when no API key is configured, `FaqFallbackService` keyword-matches the message against canned answers covering registration, food/monetary donations, education, volunteering, admin dashboards, notifications and navigation. This keeps the module fully demoable with zero external setup; it does not attempt free-form understanding.

If a key **is** configured but the Gemini call itself fails (network error, bad response), the service returns `502 Bad Gateway` via `ChatbotUpstreamException` rather than silently falling back — a configured-but-broken key should be visible, not masked.

The gateway (`/api/chatbot/**`) and this service's own `SecurityConfig` both leave the endpoint public, since the chatbot needs to help people who haven't registered yet (e.g. "how do I sign up as a Donor?").

Swagger UI: `http://localhost:8087/swagger-ui.html`

## Phase 6 addition

```
frontend/                         # React + Vite, port 5173
├── index.html, vite.config.js, package.json, .env.example
└── src/
    ├── api/                      # one file per microservice: authApi, foodApi, monetaryApi,
    │                              # educationApi, volunteerApi, chatbotApi — all calling the
    │                              # gateway (:8080), plus axiosInstance.js (JWT header + 401 handling)
    ├── context/AuthContext.jsx   # JWT storage, auto-login on refresh, logout, role helpers
    ├── routes/                  # ProtectedRoute (must be logged in), RoleRoute (must have role)
    ├── components/
    │   ├── layout/              # Navbar, Sidebar (role-based links), Footer, DashboardLayout, PublicLayout
    │   ├── common/              # Loading, ErrorMessage, ErrorBoundary, StatusBadge, StatCard, toast helper
    │   └── chatbot/ChatbotWidget.jsx   # floating button + popup, wired to POST /api/chatbot/chat
    └── pages/
        ├── common/              # Home, NotFound, Unauthorized, ErrorPage
        ├── auth/                # Login, Register (all 6 roles), ForgotPassword, Profile
        ├── admin/               # Dashboard (aggregates all 5 services' stats), Users,
        │                        # AllFoodDonations, AllMonetaryDonations, AllStudents,
        │                        # AllAssignments, AllVolunteerTasks
        ├── donor/               # MyFoodDonations, FoodDonationForm (add/edit), DonateMoney,
        │                        # MonetaryDonationHistory
        ├── ngo/                 # AvailableDonations, NgoDonationHistory, NgoStudents,
        │                        # AssignEducator, AssignmentHistory, AssignVolunteerTask,
        │                        # NgoVolunteerTasks
        ├── educator/MyStudents.jsx
        └── volunteer/           # MyTasks, CompletedTasks
```

Every page talks to the backend exclusively through the gateway (`VITE_API_BASE_URL`, default `http://localhost:8080`) — no service is called directly. `axiosInstance` unwraps the shared `ApiResponse<T>` envelope so pages just deal with `data`, attaches `Authorization: Bearer <token>` automatically when present, and on a `401` clears storage and redirects to `/login`.

Routing is fully role-gated: `ProtectedRoute` guards anything requiring login, `RoleRoute` further restricts a subtree to specific roles (e.g. `/admin/**` → `ADMIN` only), and anyone hitting a route their role can't access lands on `/unauthorized` rather than a broken page.

The chatbot from Phase 5 is mounted globally (`ChatbotWidget`, floating button bottom-right) on both public and authenticated layouts, since `/api/chatbot/chat` is a public endpoint — it works logged out and, when logged in, the JWT rides along automatically for a role-tailored answer.

`ForgotPassword` is intentionally a UI-only placeholder: the Auth Service has no password-reset endpoint yet, so the page collects the email and tells the user plainly that reset isn't wired up rather than pretending to send an email.

### Running the frontend
```bash
cd frontend
cp .env.example .env     # adjust VITE_API_BASE_URL if the gateway isn't on localhost:8080
npm install
npm run dev              # http://localhost:5173
```
Requires the gateway (`:8080`) and whichever backend services you're testing to already be running (Option A or B below), since every API call is routed through it.

## Running it

### Option A — Docker Compose (recommended)
```bash
docker compose up --build
```
This starts MySQL, RabbitMQ, Eureka (`:8761`), Gateway (`:8080`), Auth Service (`:8081`), Food Donation Service (`:8082`), Monetary Donation Service (`:8083`), Notification Service (`:8084`), Education Service (`:8085`), Volunteer Service (`:8086`), and Chatbot Service (`:8087`).

### Option B — Locally with Maven
Prereqs: Java 21, Maven 3.9+, a running MySQL 8 on `:3306` (user `root`/`root`, or override via env vars) and RabbitMQ on `:5672`.

```bash
# from repo root
mvn clean install -DskipTests

# in separate terminals, in this order:
cd eureka-server              && mvn spring-boot:run
cd api-gateway                && mvn spring-boot:run
cd auth-service                && mvn spring-boot:run
cd food-donation-service       && mvn spring-boot:run
cd monetary-donation-service   && mvn spring-boot:run
cd notification-service        && mvn spring-boot:run
cd education-service           && mvn spring-boot:run
cd volunteer-service           && mvn spring-boot:run
cd chatbot-service             && mvn spring-boot:run
```

### Quick smoke test
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Asha Verma","email":"asha@example.com","password":"Passw0rd","phoneNumber":"9876543210","city":"Pune","role":"DONOR"}'

curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"asha@example.com","password":"Passw0rd"}'
```
Copy the `accessToken` from the login response and use it as `Authorization: Bearer <token>` to call `/api/auth/profile`.

Everything above also works routed through the gateway on `:8080` once Eureka registration completes (~30s after the service starts) — e.g. `POST http://localhost:8080/api/auth/login`.

```bash
curl -X POST http://localhost:8087/api/chatbot/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"How do I register as an NGO?"}'
```
No token needed. Works immediately in `faq-fallback` mode; set `GEMINI_API_KEY` and restart the service to switch to real Gemini responses.

## Environment variables (override defaults in `application.yml`)

| Var | Default | Service |
|---|---|---|
| `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD` | localhost/3306/aashray_auth_db/root/root | auth-service |
| `RABBITMQ_HOST`, `RABBITMQ_PORT`, `RABBITMQ_USERNAME`, `RABBITMQ_PASSWORD` | localhost/5672/guest/guest | auth-service |
| `JWT_SECRET` | dev placeholder — **change in production** | api-gateway, auth-service |
| `JWT_EXPIRATION_MS` | 86400000 (24h) | auth-service |
| `EUREKA_URL` | http://localhost:8761/eureka | api-gateway, auth-service |
| `EMAIL_ENABLED` | false (emails logged/simulated, not sent) | notification-service |
| `MAIL_HOST`, `MAIL_PORT` | smtp.gmail.com/587 | notification-service |
| `MAIL_USERNAME`, `MAIL_PASSWORD` | empty — required only when `EMAIL_ENABLED=true` | notification-service |
| `MAIL_FROM` | no-reply@aashray.org | notification-service |
| `GEMINI_API_KEY` | empty — endpoint runs in `faq-fallback` mode until set | chatbot-service |
| `GEMINI_MODEL` | gemini-1.5-flash | chatbot-service |
| `GEMINI_BASE_URL` | https://generativelanguage.googleapis.com/v1beta/models | chatbot-service |
| `GEMINI_CONNECT_TIMEOUT_MS`, `GEMINI_READ_TIMEOUT_MS` | 5000 / 15000 | chatbot-service |
| `GEMINI_TEMPERATURE`, `GEMINI_MAX_OUTPUT_TOKENS` | 0.4 / 512 | chatbot-service |

## Default seeded admin (from `schema.sql`, optional manual seed)
`admin@aashray.org` / `Admin@12345` — **rotate this immediately**, it's only there so you have an ADMIN account to test admin endpoints on day one.
