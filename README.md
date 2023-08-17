# Vending Machine Backend Application Documentation

## Overview

The Vending Machine Application is a Spring Boot application that simulates a vending machine. Users can view available items, add items to the cart, proceed to checkout, make payments, and receive change. The application uses an in-memory database to store vending items and leverages the Thymeleaf template engine for the front-end.

## Table of Contents

- [Features](#features)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)
- [Endpoints](#endpoints)
- [Data Model](#data-model)
- [Testing](#testing)
- [Swagger](#swagger)

## Features

- View available vending items with prices.
- Add items to the shopping cart.
- View the shopping cart contents and total price.
- Proceed to checkout and make payments.
- Calculate and display change or amount due after payment.

## Setup and Installation

1. Clone the repository: `git clone https://github.com/motsifane/vending-backend.git`
2. Navigate to the project directory: `cd vending-backend`
3. Build and run the application: `./mvnw spring-boot:run`

## Endpoints

- `/`: Displays the list of available vending items.
- `/add-to-cart`: Adds items to the shopping cart.
- `/cart`: Displays the contents of the shopping cart and total price.
- `/checkout`: Proceeds to checkout, calculates total price.
- `/pay`: Accepts payment, calculates change or amount due.
- `/add-item`: Adds item to the vending machine.

## Data Model

The application uses an in-memory database to store vending items.

### VendingItem Entity

- `id`: Unique identifier
- `name`: Name of the item
- `price`: Price of the item
- `quantity`: Total quantity of the item available

## Testing

Unit tests have been implemented for the Vending Machine Service and Controller classes. Tests cover item retrieval, cart functionality, checkout, and payment calculation.

## Swagger

You can access the Swagger UI by navigating to http://localhost:8086/swagger-ui/index.html. 

