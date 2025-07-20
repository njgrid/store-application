#  API Endpoints – Store Application

This document lists all the available REST API endpoints for the Store Application, grouped by functionality.

---

## ✅ User Authentication

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

- `200 OK` – Login successful
- `401 Unauthorized` – Invalid credentials

---

### 3. (Optional) Reset Password
**POST** `/api/reset-password`

**Request Body**:
```json
{
  "email": "user@example.com"
}
```

- Sends reset instructions (token-based) to email

---

## ✅ Product APIs

### 4. Get All Products
**GET** `/api/products`

**Response**:
```json
[
  {
    "id": "2411",
    "title": "Nail gun",
    "available": 8,
    "price": "23.95"
  }
]
```

- `200 OK` – Returns full product list

---

## ✅ Cart APIs

> These APIs require a valid session or JWT for authentication

### 5. Add Item to Cart
**POST** `/api/cart/add`

**Request Body**:
```json
{
  "id": "2411",
  "quantity": 2
}
```

**Responses**:
- `200 OK` – Item added to cart
- `400 Bad Request` – Insufficient stock

---

### 6. View Cart
**GET** `/api/cart`

**Response**:
```json
{
  "items": [
    {
      "ordinal": 1,
      "product": "Nail gun",
      "quantity": 2,
      "subtotal": 47.90
    }
  ],
  "total": 47.90
}
```

---

### 7. Modify Cart Item
**PUT** `/api/cart/update`

**Request Body**:
```json
{
  "id": "2411",
  "quantity": 3
}
```

- Updates item quantity
- If quantity = 0, consider it a remove

---

### 8. Remove Item from Cart
**DELETE** `/api/cart/remove/{productId}`

**Path Param**:
- `productId` – ID of the product to remove

**Response**:
- `200 OK` – Item removed
- `404 Not Found` – Not found in cart

---

## ✅ Order APIs

### 9. Checkout
**POST** `/api/checkout`

- Validates stock and cart prices
- If successful, creates an order

**Response**:
```json
{
  "orderId": "ORD123456",
  "status": "confirmed"
}
```

---

### 10. (Optional) Cancel Order
**POST** `/api/orders/{id}/cancel`

**Path Param**:
- `id` – Order ID to cancel

- Returns items back to stock

**Response**:
- `200 OK` – Order cancelled
- `403 Forbidden` – Cannot cancel

---

### 11. (Optional) Get All Orders for User
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

---

## ✅ Authorization

- Most APIs (except `/register`, `/login`, `/products`) require a valid `sessionId` or JWT.
- Can be passed via headers like:
```
Authorization: Bearer <sessionId>
```

---

##  ✅ Notes

- All responses are in JSON.
- For any invalid input or internal errors, API returns:
```json
{
  "error": "Some error message"
}
```

---
