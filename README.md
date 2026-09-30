# HallBook — Production-Oriented Function Hall Booking System

This repository is a production-oriented upgrade of the original college project. It is still intentionally readable for a fresher, but includes important real-world safeguards.

## Included

- Java 17 + Spring Boot
- Spring JDBC / JdbcTemplate
- MySQL
- BCrypt password hashing
- Registration/login
- Hall listing and search
- Hall availability
- Transactional booking creation
- Capacity validation
- Razorpay order creation
- Razorpay signature verification before confirmation
- Payment records
- Booking cancellation
- Razorpay refund request for paid bookings
- Admin booking view
- Hall soft-delete endpoint
- Reviews
- Connection-pool settings
- Global API validation/error handling
- Render environment configuration

## Important production notes

This is **production-oriented**, not a claim that a college project is automatically ready for unrestricted commercial traffic. Before going live, perform security testing, configure HTTPS, use a managed MySQL database, rotate secrets, add monitoring/backups, and add Razorpay webhooks for asynchronous payment/refund reconciliation.

### Authentication

The sample frontend stores a user object in localStorage and sends a user ID to APIs. This is intentionally simple for learning, but it is **not sufficient authentication for a real public service**.

For a true production deployment, replace this with:
- server-side sessions with secure, HttpOnly, SameSite cookies, or
- short-lived JWT access tokens + refresh-token rotation,
- server-side authorization on every user/admin operation,
- CSRF protection if cookie authentication is used,
- rate limiting and account lockout for login endpoints.

### Payments

The frontend never receives the Razorpay secret. The server creates the order and verifies the Razorpay signature before marking a booking paid.

For real launch:
- enable Razorpay webhooks,
- verify webhook signatures,
- reconcile payment/refund state asynchronously,
- make webhook handlers idempotent,
- do not treat a client-side callback alone as proof of payment.

### Booking concurrency

Booking creation is wrapped in a database transaction and uses a locking availability check. For very high traffic, use a stronger database reservation strategy and idempotency keys.

## Local setup

Create:

```sql
CREATE DATABASE function_hall_booking;
```

Set:

```text
DB_URL=jdbc:mysql://localhost:3306/function_hall_booking?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=your_password
RAZORPAY_KEY_ID=your_test_key
RAZORPAY_KEY_SECRET=your_test_secret
ADMIN_EMAIL=admin@example.com
ADMIN_PASSWORD=change-this
COOKIE_SECURE=false
```

Run:

```bash
mvn spring-boot:run
```

Open:

```text
http://localhost:8080
```

## GitHub

Never commit secrets.

```bash
git init
git add .
git commit -m "Production-oriented function hall booking system"
git branch -M main
git remote add origin YOUR_REPOSITORY_URL
git push -u origin main
```

## Render

Use a managed external MySQL-compatible database and set the environment variables in Render.

Build:

```bash
mvn clean package
```

Start:

```bash
java -jar target/function-hall-booking-1.0.0.jar
```

Set:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
RAZORPAY_KEY_ID
RAZORPAY_KEY_SECRET
ADMIN_EMAIL
ADMIN_PASSWORD
COOKIE_SECURE=true
```

For a production site, use HTTPS and a real domain.


## If halls do not appear on Render

Open:

```text
https://YOUR-APP.onrender.com/api/health
```

You should see JSON containing `"status":"UP"` and a positive `"hallCount"`.

This version includes `DataSeeder`, which inserts sample halls at application startup if the `function_halls` table is empty. It includes Tirupati, Hyderabad, Vijayawada and Nellore sample venues.

If `/api/health` fails, the problem is the Render database/environment configuration rather than the frontend.

If `/api/health` works and `hallCount` is positive, open:

```text
https://YOUR-APP.onrender.com/api/halls
```

That endpoint should return the hall JSON.

## Final verification endpoints

After deployment, check these in order:

1. `/` — homepage
2. `/api/health` — database/service health
3. `/api/halls` — hall data
4. `/api/halls?city=Nellore` — Nellore filtering

The application seeds missing sample halls by name at startup, so an existing database does not need to be deleted just to add the sample venues.


## Project structure

This repository uses the standard Maven/Spring Boot layout. Java source files are under `src/main/java`, and the website files are under `src/main/resources/static`. Spring Boot serves `index.html` and `/css/style.css` directly from that static directory.
