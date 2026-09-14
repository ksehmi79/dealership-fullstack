import { useEffect, useState } from "react";
import {
  Routes,
  Route,
  Link,
  NavLink,
  Navigate,
  useNavigate,
} from "react-router-dom";

import CarsPage from "./pages/CarsPage";
import CustomersPage from "./pages/CustomersPage";
import SalesPage from "./pages/SalesPage";
import LoginPage from "./pages/LoginPage";
import DashboardPage from "./pages/DashboardPage";
import ProtectedRoute from "./components/ProtectedRoute";

import {
  getCars,
  createCar,
  updateCar,
  deleteCarById,
} from "./services/carService";

import {
  getCustomers,
  createCustomer,
  updateCustomer,
  deleteCustomerById,
} from "./services/customerService";

import { getSales, createSale } from "./services/saleService";
import { loginUser } from "./services/authService";

function App() {
  const navigate = useNavigate();

  // Authentication
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [token, setToken] = useState(
    localStorage.getItem("token") || ""
  );

  // General message
  const [message, setMessage] = useState("");

  // Cars
  const [cars, setCars] = useState([]);
 
  // Customers
  const [customers, setCustomers] = useState([]);
   
  // Sales
  const [sales, setSales] = useState([]);
  
  // -------------------------
  // Authentication helpers
  // -------------------------

  function logout() {
    setToken("");
    setCars([]);
    setCustomers([]);
    setSales([]);
    setMessage("");

    localStorage.removeItem("token");

    navigate("/login");
  }

  async function login() {
    try {
      const response = await loginUser(username, password);

      if (!response.ok) {
        throw new Error("Invalid username or password");
      }

      const data = await response.json();

      setToken(data.token);
      localStorage.setItem("token", data.token);

      setUsername("");
      setPassword("");
      setMessage("");

      navigate("/dashboard");
    } catch (error) {
      setMessage(error.message);
    }
  }

  function getRoleFromToken(jwtToken) {
    if (!jwtToken) {
      return "";
    }

    try {
      const parts = jwtToken.split(".");

      if (parts.length !== 3) {
        return "";
      }

      let payload = parts[1];

      // Convert Base64URL to regular Base64
      payload = payload.replace(/-/g, "+").replace(/_/g, "/");

      // Add padding if required
      while (payload.length % 4) {
        payload += "=";
      }

      const decodedPayload = JSON.parse(atob(payload));

      return decodedPayload.role || "";
    } catch (error) {
      console.log("Invalid JWT:", error.message);
      return "";
    }
  }

  async function handleApiResponse(response) {
    if (response.status === 401) {
      setToken("");
      localStorage.removeItem("token");

      navigate("/login");

      throw new Error("Session expired. Please login again.");
    }

    if (response.status === 403) {
      throw new Error(
        "You do not have permission to perform this action."
      );
    }

    if (!response.ok) {
      let errorMessage = "Something went wrong. Please try again.";

      try {
        const backendMessage = await response.text();

        if (backendMessage) {
          errorMessage = backendMessage;
        }
      } catch {
        // Keep default error message
      }

      throw new Error(errorMessage);
    }

    return response;
  }

  const role = getRoleFromToken(token);

  // -------------------------
  // Load cars
  // -------------------------

  useEffect(() => {
    async function loadCars() {
      try {
        const data = await getCars();
        setCars(data);
      } catch (error) {
        setMessage(error.message);
        console.log("Failed to load cars:", error.message);
      }
    }

    loadCars();
  }, []);

  // -------------------------
  // Car operations
  // -------------------------



  async function addCar(carData, editingId) {


    try {
      if (editingId === null) {
        const response = await createCar(carData, token);

        await handleApiResponse(response);

        const savedCar = await response.json();

        setCars((currentCars) => [...currentCars, savedCar]);
      } else {
        const response = await updateCar(
          editingId,
          carData,
          token
        );

        await handleApiResponse(response);

        const savedCar = await response.json();

        setCars((currentCars) =>
          currentCars.map((car) =>
            car.id === editingId ? savedCar : car
          )
        );
      }

      
      setMessage("");
      return true; // Indicate success
    } catch (error) {
      setMessage(error.message);
      console.log(error.message);
      return false; // Indicate failure
    }
  }

  async function deleteCar(id) {
    try {
      const response = await deleteCarById(id, token);

      await handleApiResponse(response);

      setCars((currentCars) =>
        currentCars.filter((car) => car.id !== id)
      );

      setMessage("");
    } catch (error) {
      setMessage(error.message);
      console.log(error.message);
    }
  }

  

  // -------------------------
  // Load customers
  // -------------------------

  useEffect(() => {
    if (!token) {
      setCustomers([]);
      return;
    }

    async function loadCustomers() {
      try {
        const response = await getCustomers(token);

        await handleApiResponse(response);

        const data = await response.json();

        setCustomers(data);
      } catch (error) {
        setMessage(error.message);
        console.log(error.message);
      }
    }

    loadCustomers();
  }, [token]);

  // -------------------------
  // Customer operations
  // -------------------------



  async function addCustomer(customerData, editingCustomerId) {


    try {
      if (editingCustomerId === null) {
        const response = await createCustomer(
          customerData,
          token
        );

        await handleApiResponse(response);

        const savedCustomer = await response.json();

        setCustomers((currentCustomers) => [
          ...currentCustomers,
          savedCustomer,
        ]);
      } else {
        const response = await updateCustomer(
          editingCustomerId,
          customerData,
          token
        );

        await handleApiResponse(response);

        const savedCustomer = await response.json();

        setCustomers((currentCustomers) =>
          currentCustomers.map((customer) =>
            customer.id === editingCustomerId
              ? savedCustomer
              : customer
          )
        );
      }

      // clearCustomerForm();
      setMessage("");
      return true; // Indicate success
    } catch (error) {
      setMessage(error.message);
      console.log(error.message);
      return false; // Indicate failure
    }
  }

  async function deleteCustomer(id) {
    try {
      const response = await deleteCustomerById(id, token);

      await handleApiResponse(response);

      setCustomers((currentCustomers) =>
        currentCustomers.filter(
          (customer) => customer.id !== id
        )
      );

      setMessage("");
    } catch (error) {
      setMessage(error.message);
      console.log(error.message);
    }
  }

  
  // -------------------------
  // Load sales
  // -------------------------

  useEffect(() => {
    if (!token) {
      setSales([]);
      return;
    }

    async function loadSales() {
      try {
        const response = await getSales(token);

        await handleApiResponse(response);

        const data = await response.json();

        setSales(data);
      } catch (error) {
        setMessage(error.message);
        console.log(error.message);
      }
    }

    loadSales();
  }, [token]);

  // -------------------------
  // Sale operations
  // -------------------------



  async function recordSale(saleData) {
 

    try {
      const response = await createSale(saleData, token);

      await handleApiResponse(response);

      const savedSale = await response.json();

      setSales((currentSales) => [
        ...currentSales,
        savedSale,
      ]);

      
      setMessage("");
      return true; // Indicate success
    } catch (error) {
      setMessage(error.message);
      console.log(error.message);
      return false; // Indicate failure
    }
  }

  // -------------------------
  // UI
  // -------------------------

  return (
    <div>
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
        <div className="container">
          <Link className="navbar-brand" to="/dashboard">
            AutoVibe Dealership
          </Link>

          <div className="navbar-nav">
            {token && (
              <>
                <NavLink
                  className="nav-link"
                  to="/dashboard"
                >
                  Dashboard
                </NavLink>

                <NavLink
                  className="nav-link"
                  to="/cars"
                >
                  Cars
                </NavLink>

                <NavLink
                  className="nav-link"
                  to="/customers"
                >
                  Customers
                </NavLink>

                <NavLink
                  className="nav-link"
                  to="/sales"
                >
                  Sales
                </NavLink>
              </>
            )}

            {token ? (
              <button
                className="btn btn-outline-light ms-2"
                onClick={logout}
              >
                Logout
              </button>
            ) : (
              <Link
                className="nav-link"
                to="/login"
              >
                Login
              </Link>
            )}
          </div>
        </div>
      </nav>

      <div className="container mt-3">
        {message && (
          <div className="alert alert-danger">
            {message}
          </div>
        )}

        <Routes>
          <Route
            path="/login"
            element={
              <LoginPage
                username={username}
                setUsername={setUsername}
                password={password}
                setPassword={setPassword}
                login={login}
              />
            }
          />

          <Route
            path="/dashboard"
            element={
              <ProtectedRoute token={token}>
                <DashboardPage
                  cars={cars}
                  customers={customers}
                  sales={sales}
                />
              </ProtectedRoute>
            }
          />

          <Route
            path="/cars"
            element={
              <ProtectedRoute token={token}>
                <CarsPage
                  cars={cars}
                  addCar={addCar}
                  deleteCar={deleteCar}
                  role={role}
                />
              </ProtectedRoute>
            }
          />

          <Route
            path="/customers"
            element={
              <ProtectedRoute token={token}>
                <CustomersPage
                  customers={customers}
                  addCustomer={addCustomer}
                  deleteCustomer={deleteCustomer}
                />
              </ProtectedRoute>
            }
          />

          <Route
            path="/sales"
            element={
              <ProtectedRoute token={token}>
                <SalesPage
                  sales={sales}
                  customers={customers}
                  cars={cars}
                  recordSale={recordSale}
                />
              </ProtectedRoute>
            }
          />

          <Route
            path="/"
            element={
              token ? (
                <Navigate
                  to="/dashboard"
                  replace
                />
              ) : (
                <Navigate
                  to="/login"
                  replace
                />
              )
            }
          />

          <Route
            path="*"
            element={
              <Navigate
                to={token ? "/dashboard" : "/login"}
                replace
              />
            }
          />
        </Routes>
      </div>
    </div>
  );
}

export default App;