function Login(props) {
  return (
    <div>
      <h2>Login</h2>
      <div className="mb-3">
        <input
          className="form-control"
          type="text"
          placeholder="Username"
          value={props.username}
          onChange={(event) => props.setUsername(event.target.value)}
        />
      </div>
      <div className="mb-3">
        <input
          className="form-control"
          type="password"
          placeholder="Password"
          value={props.password}
          onChange={(event) => props.setPassword(event.target.value)}
        />
      </div>
      <button className="btn btn-primary w-100" onClick={props.login}>
        Login
      </button>
    </div>
  );
}

export default Login;
