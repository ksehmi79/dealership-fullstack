const API_URL = "http://localhost:8080/sales";

export function getSales(token) {
  return fetch(API_URL, {
    headers: {
      Authorization: "Bearer " + token,
    },
  });
}

export function createSale(sale, token) {
  return fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    body: JSON.stringify(sale),
  });
}
