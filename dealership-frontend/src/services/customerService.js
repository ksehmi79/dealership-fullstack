const API_URL = "/api/customers";

export function getCustomers(token) {
  return fetch(API_URL, {
    headers: {
      Authorization: "Bearer " + token,
    },
  });
}

export function createCustomer(customer, token) {
  return fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    body: JSON.stringify(customer),
  });
}

export function deleteCustomerById(id, token) {
  return fetch(API_URL + "/" + id, {
    method: "DELETE",
    headers: {
      Authorization: "Bearer " + token,
    },
  });
}

export function updateCustomer(id, customer, token) {
  return fetch(API_URL + "/" + id, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    body: JSON.stringify(customer),
  });
}
