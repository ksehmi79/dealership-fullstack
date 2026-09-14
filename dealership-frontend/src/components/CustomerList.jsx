import Customer from "./Customer";

function CustomerList(props) {
  return (
    <div>
      <h2 className="mb-3">Customer List</h2>

      <div className="row g-4">
        {props.customers.map((customer) => (
          <div className="col-md-6 col-lg-4" key={customer.id}>
            <Customer
              id={customer.id}
              name={customer.name}
              email={customer.email}
              onDelete={props.onDelete}
              onCustomerEdit={props.onCustomerEdit}
            />
          </div>
        ))}
      </div>
    </div>
  );
}

export default CustomerList;
