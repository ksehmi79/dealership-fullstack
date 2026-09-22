const API_URL = "/api/cars";

export function getCars() {
  return fetch(API_URL).then((response) => response.json());
}

export function createCar(car, token) {
  return fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    body: JSON.stringify(car),
  });
}

export function updateCar(id, car, token) {
  return fetch(API_URL + "/" + id, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    body: JSON.stringify(car),
  });
}

export function deleteCarById(id, token) {
  return fetch(API_URL + "/" + id, {
    method: "DELETE",
    headers: {
      Authorization: "Bearer " + token,
    },
  });
}
