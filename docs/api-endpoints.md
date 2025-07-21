# API Endpoints – Store Application

This document lists all the REST API endpoints for the Store Application.

---

## User Authentication

### 1. Register a new user

**POST** `/api/register`

**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "123"
}
```

**Responses**:
- `200 OK` – User registered successfully
- `409 Conflict` – User already exists

---

### 2. Login

**POST** `/api/login`

**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "123"
}
```

**Response**:
```json
{
  "sessionId": "abc123xyz"
}
```

**Responses**:
- `200 OK` – Login successful
- `401 Unauthorized` – Invalid credentials

---

### 3. Reset Password

**POST** `/api/reset-password`

**Request Body**:
```json
{
  "email": "user@example.com"
}
```

**Response**:
- Sends reset instructions to the email (token-based reset flow)

---

## Product APIs

### 4. Get All Products

**GET** `/api/products`

**Response**:
```json
[
  {
    "productId": "19",
    "title": "Screwdriver Set",
    "available": 12,
    "price": 15.99
  },
  {
    "productId": "21",
    "title": "Chair",
    "available": 12,
    "price": 23.75
  }
]
```

**Responses**:
- `200 OK` – Returns full product list

---

## Cart APIs

> Requires session ID for all actions below

### 5. Add Item to Cart

**POST** `/api/cart/add`

**Request Body**:
```json
{
  "id": "19",
  "quantity": 2
}
```

**Responses**:
- `200 OK` – Item added to cart
- `400 Bad Request` – Quantity invalid or exceeds stock
- `409 Conflict` – Item already in cart

---

### 6. View Cart

**GET** `/api/cart`

**Response**:
```json
{
  "items": [
    {
      "ordinal": 0,
      "title": "Screwdriver Set",
      "quantity": 2,
      "subtotal": 31.98
    }
  ],
  "totalSubtotal": 31.98
}
```

**Responses**:
- `200 OK` – Returns cart contents
- `404 Not Found` – No cart found for session

---

### 7. Modify Cart Item

**PUT** `/api/cart/update`

**Request Body**:
```json
{
  "id": "19",
  "quantity": 3
}
```

**Behavior**:
- The quantity should be greater than 0.
- If quantity exceeds stock, returns error

**Responses**:
- `200 OK` – Cart item updated or removed
- `400 Bad Request` – Invalid quantity or stock unavailable
- `404 Not Found` – Item not found in cart

---

### 8. Remove Item from Cart

**DELETE** `/api/cart/remove/{productId}`

**Path Param**:
- `productId`: ID of the product to remove

**Responses**:
- `200 OK` – Item removed
- `404 Not Found` – Item not found in cart

---

## Order APIs

### 9. Checkout

**POST** `/api/checkout`

**Behavior**:
- Validates cart: checks price consistency and stock availability
- If all is good, finalizes the order

**Response**:
```json
{
  "orderId": "ORD123456",
  "status": "confirmed"
}
```

**Responses**:
- `200 OK` – Checkout successful
- `400 Bad Request` – Price mismatch or insufficient stock
- `404 Not Found` – Cart not found

---

### 10. Cancel Order

**POST** `/api/orders/{id}/cancel`

**Path Param**:
- `id`: Order ID to cancel

**Responses**:
- `200 OK` – Order cancelled successfully
- `403 Forbidden` – Cannot cancel order (already processed)
- `404 Not Found` – Order not found

---

### 11. Get All Orders for User

**GET** `/api/orders`

**Response**:
```json
[
  {
    "orderId": "ORD123456",
    "date": "2025-07-20",
    "total": 149.99,
    "status": "confirmed"
  }
]
```

**Responses**:
- `200 OK` – Order history returned
- `401 Unauthorized` – Invalid or missing session



---

## Notes

- All requests/responses use `application/json`
- Standard error format:
```json
{
  "error": "Error message here"
}
```
