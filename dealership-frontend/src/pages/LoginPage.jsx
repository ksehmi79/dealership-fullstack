import Login from "../components/Login";

function LoginPage(props) {
  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6 col-lg-4">
          <div className="card shadow">
            <div className="card-body p-4">
              <h2 className="text-center mb-4">AutoVibe Login</h2>

              <Login
                username={props.username}
                setUsername={props.setUsername}
                password={props.password}
                setPassword={props.setPassword}
                login={props.login}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;

// import Login from "../components/Login";

// function LoginPage(props) {
//     return (
//         <div>

//             <Login
//                 username={props.username}
//                 setUsername={props.setUsername}
//                 password={props.password}
//                 setPassword={props.setPassword}
//                 login={props.login}
//             />
//         </div>
//     );
// }

// export default LoginPage;
