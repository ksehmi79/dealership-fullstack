function Customer(props) {
  return (
    <div className="card h-100 shadow-sm">
      <div className="card-body">
        <h4 className="card-title">{props.name}</h4>

        <p className="card-text">{props.email}</p>

        <div className="d-flex gap-2">
          <button
            className="btn btn-outline-primary"
            onClick={() => props.onCustomerEdit(props.id)}
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
      </div>
    </div>
  );
}

export default Customer;
