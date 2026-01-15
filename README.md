# Datacenter Backend API

Spring Boot REST API for managing datacenter equipment (**Devices**) and **Racks**, tracking rack power usage, placing devices into rack units (no overlaps), and generating an **on-demand balanced placement suggestion** (heuristic algorithm).

## What this project covers

### 1) Persisted system state (real placement)
The API supports placing a device into a rack at a given `startUnit` and enforces:
- **No unit overlap** (a unit can contain only one device)
- **Rack unit capacity** (`totalUnits`)
- **Rack power capacity** (`maxPowerW`)
- **One device can be placed only once** (cannot be placed into multiple racks)

This state is persisted using:
- `DeviceEntity`
- `RackEntity`
- `DevicePlacementEntity`

### 2) Placement suggestion (balanced distribution)
The API also provides an endpoint that **calculates a balanced distribution** of given devices across given racks:
- computed **on-demand**
- **not persisted**
- assumes all racks are **empty**
- does **not** consider current DB placements

DTOs for this feature are located in:  
`src/main/java/com/example/datacenter_backend_api/domain/dto/placement`

The algorithm is implemented in `PlacementService` and tested in `PlacementServiceTest`.

---

## Tech stack
- Java 21
- Spring Boot 4.0.1
- Spring WebMVC + Validation
- Spring Data JPA
- H2 (in-memory)
- Springdoc OpenAPI (Swagger UI)
- Lombok
- Spring Boot Actuator (health endpoint)
- Docker / Docker Compose

---

## Project structure (packages)

- `controller`  
  REST controllers (`DeviceController`, `RackController`, `PlacementController`)

- `domain`
    - `dto`  
      Public API DTOs for devices/racks + request DTOs  
      Also contains `dto/placement` for balanced-placement input/output
    - `mapper`  
      Mapping between entities and DTOs (`DeviceMapper`, `RackMapper`)

- `persistence`
    - `model`  
      JPA entities (`DeviceEntity`, `RackEntity`, `DevicePlacementEntity`)
    - `repository`  
      Spring Data repositories

- `service`  
  Business logic (`DeviceService`, `RackService`, `PlacementService`)

- `exception`  
  Custom exceptions + `GlobalExceptionHandler` + `ApiError`

---

## API Documentation (Swagger)
After starting the app, open:
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
    - (sometimes also works: `http://localhost:8080/swagger-ui.html`)
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

---

## Running the application

### Option A: Run locally (Maven)
```bash
mvn clean test
mvn spring-boot:run
```
### Option B: Run with Docker Compose
```bash
docker compose up --build
```
Stop containers:
```bash
docker compose down
```
---
## H2 Console
Enabled at:
- `http://localhost:8080/h2-console`

Default JDBC URL (as configured):
- `jdbc:h2:mem:datacenterdb`

Username:
`sa`

Password:(empty)

---

## Endpoints overview

### Devices (`/api/devices`)

- `POST /api/devices` (create)

- `GET /api/devices` (list)

- `GET /api/devices/{id}` (details)

- `PATCH /api/devices/{id}` (partial update)

- `DELETE /api/devices/{id}`

### Racks (`/api/racks`)

- `POST /api/racks` (create)

- `GET /api/racks` (list)

- `GET /api/racks/{id}` (details; includes currentPowerW)

- `PATCH /api/racks/{id}` (partial update)

- `DELETE /api/racks/{id}`

### Real placement (persisted)

- `POST /api/racks/{rackId}/devices/{deviceId}/place`
  - body: `{startUnit: 10`}
  - returns: '204 No Content'

### Balanced placement suggestion (NOT persisted)

- `POST /api/placements/balanced`
    - takes a list of devices + racks (DTOs)
    - returns a suggested distribution across racks

---

## Swagger testing walkthrough

### 1) Create a Rack
Swagger `-> POST /api/racks`
```bash
{
  "name": "Rack A",
  "description": "Test rack",
  "serialNumber": "RACK-A",
  "totalUnits": 42,
  "maxPowerW": 5000
}
```
### 2) Create a Device
Swagger `-> POST/api/devices`
```bash
{
  "name": "Server 1",
  "description": "Test server",
  "serialNumber": "SERVER-001",
  "units": 2,
  "powerConsumptionW": 1200
}
```
### 3) Place device into rack (persisted)

Swagger `-> POST /api/racks/{rackId}/devices/{deviceId}/place`
```bash
{
"startUnit": 10
}
```
Rules in this example:
- `startUnit >= 1`

- `endUnit = startUnit + device.units - 1` must be `<= rack.totalUnits`

- no overlaps with existing placed devices

- total rack power after placement must be `<= rack.maxPowerW`
### 4) Verify rack power usage

Swagger `-> GET /api/racks/{id}`

Response includes:

- `maxPowerW` (declared rack capacity)

- `currentPowerW` (sum of power of placed devices)

---

### Balanced placement suggestion (Swagger)

Swagger `-> POST /api/placements/balanced`

Example request:
```bash
{
  "devices": [
    { "name": "Device 1", "serialNumber": "D-1", "units": 2, "powerConsumptionW": 1200 },
    { "name": "Device 2", "serialNumber": "D-2", "units": 1, "powerConsumptionW": 500 },
    { "name": "Device 3", "serialNumber": "D-3", "units": 1, "powerConsumptionW": 300 },
    { "name": "Device 4", "serialNumber": "D-4", "units": 2, "powerConsumptionW": 900 }
  ],
  "racks": [
    { "name": "Rack 1", "serialNumber": "R-1", "totalUnits": 42, "maxPowerW": 5000 },
    { "name": "Rack 2", "serialNumber": "R-2", "totalUnits": 42, "maxPowerW": 5000 }
  ]
}

```


**Important:** This endpoint ignores DB state and assumes all racks are empty.
It returns a proposal where each rack’s **utilization** `(usedPowerW / maxPowerW)` is as close as possible.

---

## Testing
Only the balanced placement logic is required to be tested by the assignment.

Run:

```bash
mvn test
```
Check:

- `src/test/java/com/example/datacenter_backend_api/service/PlacementServiceTest.java`

---
## Notes
- Swagger is enabled for easier testing (`/swagger-ui/index.html`).
- To disable in production: `springdoc.api-docs.enabled=false` and `springdoc.swagger-ui.enabled=false`.