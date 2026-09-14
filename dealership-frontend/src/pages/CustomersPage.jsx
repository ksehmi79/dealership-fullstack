import CustomerForm from "../components/CustomerForm";
import CustomerList from "../components/CustomerList";
import { useState } from "react";

function CustomersPage(props) {
    const [customerName, setCustomerName] = useState("");
    const [customerEmail, setCustomerEmail] = useState("");
    const [editingCustomerId, setEditingCustomerId] = useState(null);
  
      function clearCustomerForm() {
        setCustomerName("");
        setCustomerEmail("");
        setEditingCustomerId(null);
      }
    async function handleAddCustomer() {
      const customerData = {
        name: customerName,
        email: customerEmail,
      };

      const success = await props.addCustomer(
        customerData,
        editingCustomerId
      );

      if (success) {
        clearCustomerForm();
      }
    }
    
    function editCustomer(id) {
        const customerToEdit  = props.customers.find(
          (customer) => customer.id === id
        );
    
        if (!customerToEdit) {
          return;
        }
    
        setCustomerName(customerToEdit.name);
        setCustomerEmail(customerToEdit.email);
        setEditingCustomerId(id);
      }
    


  return (
    <div className="container mt-4">
      <h1 className="mb-4">Customers</h1>

      <div className="card shadow-sm mb-4">
        <div className="card-body">
          <CustomerForm
            customerName={customerName}
            setCustomerName={setCustomerName}
            customerEmail={customerEmail}
            setCustomerEmail={setCustomerEmail}
            addCustomer={handleAddCustomer}
            editingCustomerId={editingCustomerId}
          />
        </div>
      </div>

      <CustomerList
        customers={props.customers}
        onDelete={props.deleteCustomer}
        onCustomerEdit={editCustomer}
      />
    </div>
  );
}

export default CustomersPage;
