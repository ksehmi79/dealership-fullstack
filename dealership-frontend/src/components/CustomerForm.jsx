function CustomerForm(props) {
  return (
    <div>
      <h2>Customer Form</h2>
      <div className="mb-3">
        <input
          className="form-control"
          type="text"
          placeholder="Customer Name"
          value={props.customerName}
          onChange={(event) => props.setCustomerName(event.target.value)}
        />
      </div>
      <div className="mb-3">
        <input
          className="form-control"
          type="email"
          placeholder="Customer Email"
          value={props.customerEmail}
          onChange={(event) => props.setCustomerEmail(event.target.value)}
        />
      </div>
      <button className="btn btn-primary" onClick={props.addCustomer}>
        {props.editingCustomerId === null ? "Add Customer" : "Update Customer"}
      </button>
    </div>
  );
}

export default CustomerForm;
