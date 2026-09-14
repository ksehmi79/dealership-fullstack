import SaleForm from "../components/SaleForm";
import SaleList from "../components/SaleList";
import { useState } from "react";

function SalesPage(props) {
    const [saleCustomerId, setSaleCustomerId] = useState("");
    const [saleCarId, setSaleCarId] = useState("");
    const [salePrice, setSalePrice] = useState("");


    async function handleRecordSale() {
      const saleData = {
        customerId: Number(saleCustomerId),
        carId: Number(saleCarId),
        salePrice: Number(salePrice),
      };

      const success = await props.recordSale(saleData);

      if (success) {
        clearSaleForm();
      }
    }

    function clearSaleForm() {
      setSaleCustomerId("");
      setSaleCarId("");
      setSalePrice("");
    }

  return (
    <div className="container mt-4">
      <h1 className="mb-4">Sales</h1>

      <div className="card shadow-sm mb-4">
        <div className="card-body">
          <SaleForm
            customers={props.customers}
            customerId={saleCustomerId}
            setCustomerId={setSaleCustomerId}

            cars={props.cars}
            carId={saleCarId}
            setCarId={setSaleCarId}

            salePrice={salePrice}
            setSalePrice={setSalePrice}

            recordSale={handleRecordSale}
          />
        </div>
      </div>

      <SaleList sales={props.sales} />
    </div>
  );
}

export default SalesPage;
