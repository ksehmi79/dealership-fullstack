const AUTH_URL = "/api/auth/login";

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
