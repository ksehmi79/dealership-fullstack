function SaleForm(props) {
  return (
    <div>
      <h2>Record Sale</h2>
      <div className="mb-3">
        <select
          className="form-select"
          value={props.customerId}
          onChange={(event) => props.setCustomerId(event.target.value)}
        >
          <option value="">Select Customer</option>

          {props.customers.map((customer) => (
            <option key={customer.id} value={customer.id}>
              {customer.name}
            </option>
          ))}
        </select>
      </div>
      <div className="mb-3">
        <select
          className="form-select"
          value={props.carId}
          onChange={(event) => {
            const selectedCarId = event.target.value;

            props.setCarId(selectedCarId);

            const selectedCar = props.cars.find(
              (car) => car.id === Number(selectedCarId),
            );

            if (selectedCar) {
              props.setSalePrice(selectedCar.price);
            }
          }}
        >
          <option value="">Select Car</option>
          {props.cars.map((car) => (
            <option key={car.id} value={car.id}>
              {car.make} {car.model}
            </option>
          ))}
        </select>
      </div>
      <div className="mb-3">
        <input
          className="form-control"
          placeholder="Sale Price"
          type="number"
          value={props.salePrice}
          onChange={(event) => props.setSalePrice(event.target.value)}
        />
      </div>

      <button className="btn btn-success" onClick={props.recordSale}>
        Record Sale
      </button>
    </div>
  );
}

export default SaleForm;
