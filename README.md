# J-Fines-Penalties

**Service for Inspectors to register administrative offenses and for Citizens to view decisions on their cases**

A system for recording fines/penalties (administrative violations), integrating with court decisions and citizen notifications.

## Overview

J-Fines-Penalties enables:

- **Inspectors** to register legal/administrative offenses and forward them to the judicial system
- **Citizens** to view rulings/decisions related to their cases

## Key Processes

### Offense Registration & Processing

1. Inspectors (**InspectorEntity**) register offenses in the system.
2. Registered offenses are sent to the **J-Court** service.
3. If the offender is not yet registered in the system:
    - Their personal data is fetched from **J-GCP** using **PINFL**.
4. Court rulings / decisions on the offenses are received asynchronously via **webhook** from **J-Court**.
5. Once a ruling is received:
    - The citizen is notified via email using the **J-Notification** service.
6. Citizens can log in to view their cases using:
    - **PINFL** + OTP (sent to the phone number registered in J-GCP)

## Integrations

| Service        | Repository / Location                               | Purpose                              |
|----------------|-----------------------------------------------------|--------------------------------------|
| **J-Notification** | https://github.com/Dadamuhames/notification-service | Sending email & other notifications  |
| **J-Court**        | https://github.com/Dadamuhames/court-service        | Integration with court decisions & rulings |
| **J-GCP**          |                                                     | Central personal data & identity service (PINFL lookup) |

### Authentication Flow

- Users authorize using **PINFL** (14-digit personal identifier used in Uzbekistan e-government services)
- One-time password (**OTP**) is sent to the phone number linked to the PINFL in **J-GCP**
- Inspectors authorize using **Personnel Number** and **Password** 