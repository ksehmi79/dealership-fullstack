const AUTH_URL = "http://localhost:8080/auth/login";

export function loginUser(username, password) {
  const loginRequest = {
    username,
    password,
  };

  return fetch(AUTH_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(loginRequest),
  });
}
