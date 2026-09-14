function DashboardPage(props) {
  const totalRevenue = props.sales.reduce(
    (total, sale) => total + sale.salePrice,
    0,
  );

  return (
    <div className="container mt-4">
      <h1 className="mb-4">Dashboard</h1>

      <div className="row g-4">
        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-body">
              <h5 className="card-title">Total Cars</h5>

              <h2>{props.cars.length}</h2>
            </div>
          </div>
        </div>

        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-body">
              <h5 className="card-title">Total Customers</h5>

              <h2>{props.customers.length}</h2>
            </div>
          </div>
        </div>

        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-body">
              <h5 className="card-title">Total Sales</h5>

              <h2>{props.sales.length}</h2>
            </div>
          </div>
        </div>

        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-body">
              <h5 className="card-title">Total Revenue</h5>

              <h2>${totalRevenue.toLocaleString()}</h2>
            </div>
          </div>
        </div>

        <div className="mt-5">
          <h2 className="mb-3">Recent Sales</h2>

          <div className="card shadow-sm">
            <div className="card-body">
              {props.sales.length === 0 ? (
                <p>No sales recorded yet.</p>
              ) : (
                props.sales
                  .slice(-5)
                  .reverse()
                  .map((sale) => (
                    <div key={sale.id} className="border-bottom py-3">
                      <strong>{sale.customerName}</strong>

                      <div>
                        {sale.carMake} {sale.carModel}
                      </div>

                      <div>${sale.salePrice.toLocaleString()}</div>
                    </div>
                  ))
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default DashboardPage;
