# E-Hotel

E-Hotel is a web application for managing the hotels of several hotel chains. Customers search for
available rooms and book them; employees manage customers, turn bookings into rentals at check-in and
record payments; managers maintain employees, hotels and rooms.

It is a server-rendered Spring Boot application on top of a PostgreSQL schema that contains much of
the domain logic itself (constraints, triggers, archive tables and views). It was originally built as
a university group project. The user interface is in French.

## Features

**Customers**
- Search for rooms that are free for a date range, optionally filtered by capacity, maximum price,
  hotel chain, city, hotel category (stars) and hotel size
- Book a room. A returning customer only needs their SIN (social insurance number); a new customer
  is registered from the booking form

**Employees**
- Create, edit and delete customers
- List current bookings and convert a booking into a rental when the customer checks in
- Record payments for rentals and list them

**Managers**
- Create, edit and delete employees, hotels and rooms

**Business rules**
- A room cannot be booked for dates that overlap an existing booking or rental of that room. The room
  row is locked while this is checked, so two concurrent requests cannot both succeed
- Forms are validated on the server with the same rules as the database (SIN format, US/Canadian/Mexican
  postal codes, star rating 1 to 5, positive prices, dates in the right order). Errors are shown
  next to the field
- Bookings and rentals are copied to archive tables by triggers, so their history survives the
  deletion of a room or hotel
- Each hotel's room count and each chain's hotel count are kept up to date by triggers
- Missing records show a 404 page; operations the database rejects (for example deleting a customer
  who still has a booking) show an explanation instead of a stack trace

## Technology

| Area | Choice |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2 (Spring MVC, Spring Data JPA / Hibernate 6, Bean Validation) |
| Views | Thymeleaf templates, Bootstrap 5 (CDN) |
| Database | PostgreSQL (PL/pgSQL triggers, views) |
| Build | Maven (wrapper included) |
| Tests | JUnit 5, Mockito, Spring MockMvc, embedded PostgreSQL ([zonky](https://github.com/zonkyio/embedded-database-spring-test)) |

## Architecture

```
Browser ──HTTP──▶ Controllers ──▶ Services ──▶ Spring Data repositories ──▶ PostgreSQL
                 (validation,    (business      (JPA entities,               (schema.sql:
                  view models)    rules,         JPQL queries)                constraints,
                                  transactions)                               triggers, views)
```

- **`controllers`**: handle HTTP requests, validate the form DTOs and pick the Thymeleaf view.
  `GlobalExceptionHandler` maps exceptions to error pages
- **`services`**: business rules and transactions, such as booking checks, booking-to-rental
  conversion and DTO-to-entity mapping
- **`repositories`**: Spring Data JPA interfaces, including the room search and overlap queries
- **`model`**: JPA entities mapped to the tables in `schema.sql`
- **`dtos`**: form objects with Bean Validation annotations

`schema.sql` is the source of truth for the database. Hibernate runs with `ddl-auto=validate`. It
checks at startup that the entities match the schema and never changes the schema itself.

```
E-Hotel/
├── schema.sql                 # tables, constraints, triggers, indexes, views and seed data
├── docs/screenshots/
└── EHotel/                    # Spring Boot application
    ├── pom.xml
    └── src/
        ├── main/java/com/example/EHotel/
        │   ├── controllers/  services/  repositories/  model/  dtos/  exceptions/
        └── main/resources/
            ├── application.properties
            └── templates/     # Thymeleaf pages (fragments/layout.html holds the shared layout)
```

## Getting started

### Prerequisites

- JDK 17 or newer
- PostgreSQL 11 or newer (tested with 14 and 16)

Maven does not need to be installed. The project includes the Maven wrapper (`./mvnw`).

### 1. Create the database

`schema.sql` creates every table, trigger and view and loads sample data: 5 hotel chains, 40 hotels,
200 rooms and 680 employees. It is meant to be run **once, on an empty database**.

```bash
createdb db_hotel
psql -d db_hotel -f schema.sql
```

### 2. Configure the connection

The connection is read from environment variables. The repository contains no credentials.

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/db_hotel` | JDBC URL of the database |
| `DB_USERNAME` | `postgres` | Database user |
| `DB_PASSWORD` | *(empty)* | Database password |
| `PORT` | `8080` | HTTP port |

```bash
export DB_PASSWORD='your-password'
```

### 3. Run the application

```bash
cd EHotel
./mvnw spring-boot:run
```

Open <http://localhost:8080>. To build a runnable jar instead, run `./mvnw package` and then
`java -jar target/EHotel-0.0.1-SNAPSHOT.jar`.

## Tests

```bash
cd EHotel
./mvnw test
```

The tests do not need a local PostgreSQL server or Docker. The integration tests download and start an
embedded PostgreSQL server and load the real `schema.sql`, so triggers, cascades and seed data are tested
as they are in production. The first run downloads the PostgreSQL binaries.

| Test class | What it covers |
|---|---|
| `BookingServiceTest` | Overlap and availability rules, reusing or creating the customer, booking-to-rental conversion (Mockito) |
| `DtoValidationTest` | SIN, postal code, country, star rating, price and date rules |
| `RoomControllerTest` | Validation errors redisplay the form, business-rule errors, 404 page, deletes require POST, dates survive redisplay |
| `DatabaseIntegrationTest` | Seed data, views, generated ids and room-count trigger, archiving, room search, cascading hotel deletion, removing a hotel manager |
| `EHotelApplicationTests` | The application context starts and the JPA mappings match `schema.sql` |

GitHub Actions runs the same suite on every push (`.github/workflows/ci.yml`).

## Known limitations

- **No authentication.** The customer, employee and manager areas are separate pages, but anyone can
  open any of them, and there is no CSRF protection. Adding Spring Security with roles would be the
  next step before any real deployment.
- **Room availability after check-in.** Converting a booking into a rental sets the room's
  `availability` flag to false (a trigger in `schema.sql`). Nothing sets it back automatically when
  the stay ends. A manager has to mark the room available again from the room edit form.
- **Date rules come from the schema.** Bookings cannot start in the past and payments cannot be
  backdated (`CHECK (... >= CURRENT_DATE)` constraints).
- **Hotel count when a hotel changes chain.** The triggers update a chain's hotel count when hotels
  are inserted or deleted, but not when an existing hotel is moved to another chain.
- **`ViewTotalCapacityByHotel`** converts the capacity labels to guest counts. The values for
  `Suite` (4) and `Penthouse` (6) are assumptions.
- **Schema setup.** `schema.sql` is not idempotent, so it is meant for an empty database. A database
  created from an older version of the script must be recreated, because ids are now generated
  identity columns.
- The archive tables keep their original names (`booking_archieve`, `rental_archieve`).
- The employee list is not paginated (680 rows with the seed data).

## Credits

E-Hotel was originally developed as a university group project. The `portfolio-improvements` branch
fixes bugs, adds validation, error handling and tests, and documents the application. The commit history
records who made each change.
