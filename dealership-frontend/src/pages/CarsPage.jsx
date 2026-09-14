import CarForm from "../components/CarForm";
import Car from "../Car";
import { useState } from "react";

function CarsPage(props) {
  const [make, setMake] = useState("");
  const [model, setModel] = useState("");
  const [year, setYear] = useState("");
  const [price, setPrice] = useState("");
  const [editingId, setEditingId] = useState(null);

  async function handleAddCar() {
    const carData = {
      make,
      model,
      year: Number(year),
      price: Number(price),
    };

    const success = await props.addCar(carData, editingId);

    if (success) {
      clearCarForm();
    }
  }

  function editCar(id) {
    const carToEdit = props.cars.find((car) => car.id === id);

    if (!carToEdit) {
      return;
    }

    setMake(carToEdit.make);
    setModel(carToEdit.model);
    setYear(carToEdit.year);
    setPrice(carToEdit.price);
    setEditingId(id);
  }

  function clearCarForm() {
    setMake("");
    setModel("");
    setYear("");
    setPrice("");
    setEditingId(null);
  }

  return (
    <div className="container mt-4">
      <h1 className="mb-4">Vehicle Inventory</h1>

      {props.role === "ADMIN" && (
        <div className="card shadow-sm mb-4">
          <div className="card-body">
            <CarForm
              make={make}
              setMake={setMake}
              model={model}
              setModel={setModel}
              year={year}
              setYear={setYear}
              price={price}
              setPrice={setPrice}
              addCar={handleAddCar}
              editingId={editingId}
            />
          </div>
        </div>
      )}

      <div className="row g-4">
        {props.cars.map((car) => (
          <div className="col-md-6 col-lg-4" key={car.id}>
            <Car
              id={car.id}
              make={car.make}
              model={car.model}
              year={car.year}
              price={car.price}
              onDelete={props.deleteCar}
              onEdit={editCar}
              role={props.role}
            />
          </div>
        ))}
      </div>
    </div>
  );
}

export default CarsPage;