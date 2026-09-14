function Sale(props) {
  return (
    <div className="card h-100 shadow-sm">
      <div className="card-body">
        <h5 className="card-title">Sale #{props.id}</h5>

        <p className="card-text">
          <strong>Customer:</strong> {props.customerName}
        </p>

        <p className="card-text">
          <strong>Vehicle:</strong> {props.carMake} {props.carModel}
        </p>

        <h4>${props.salePrice.toLocaleString()}</h4>
      </div>
    </div>
  );
}

export default Sale;
