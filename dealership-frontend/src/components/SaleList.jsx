import Sale from "./Sale";

function SaleList(props) {
  return (
    <div>
      <h2 className="mb-3">Sales History</h2>

      <div className="row g-4">
        {props.sales.map((sale) => (
          <div className="col-md-6 col-lg-4" key={sale.id}>
            <Sale
              id={sale.id}
              customerName={sale.customerName}
              carMake={sale.carMake}
              carModel={sale.carModel}
              salePrice={sale.salePrice}
            />
          </div>
        ))}
      </div>
    </div>
  );
}

export default SaleList;
