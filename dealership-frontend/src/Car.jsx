function Car(props) {
  return (
    <div className="card h-100 shadow-sm">
      <div className="card-body">
        <h4 className="card-title">
          {props.make} {props.model}
        </h4>

        <p className="card-text">Year: {props.year}</p>

        <p className="card-text">Price: ${props.price.toLocaleString()}</p>

        {props.role === "ADMIN" && (
          <div className="d-flex gap-2">
            <button
              className="btn btn-outline-primary"
              onClick={() => props.onEdit(props.id)}
            >
              Edit
            </button>

            <button
              className="btn btn-outline-danger"
              onClick={() => props.onDelete(props.id)}
            >
              Delete
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default Car;
