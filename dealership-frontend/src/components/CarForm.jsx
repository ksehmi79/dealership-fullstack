function CarForm(props) {
  return (
    <div>
      <h2>{props.editingId === null ? "Add Car" : "Edit Car"}</h2>
      <div className="mb-3">
        <input
          className="form-control"
          type="text"
          placeholder="Make"
          value={props.make}
          onChange={(event) => props.setMake(event.target.value)}
        />
      </div>

      <div className="mb-3">
        <input
          className="form-control"
          type="text"
          placeholder="Model"
          value={props.model}
          onChange={(event) => props.setModel(event.target.value)}
        />
      </div>

      <div className="mb-3">
        <input
          className="form-control"
          type="number"
          placeholder="Year"
          value={props.year}
          onChange={(event) => props.setYear(event.target.value)}
        />
      </div>

      <div className="mb-3">
        <input
          className="form-control"
          type="number"
          placeholder="Price"
          value={props.price}
          onChange={(event) => props.setPrice(event.target.value)}
        />
      </div>

      <button className="btn btn-primary" onClick={props.addCar}>
        {props.editingId === null ? "Add Car" : "Update Car"}
      </button>
    </div>
  );
}

export default CarForm;
