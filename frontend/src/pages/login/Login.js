import React from "react";
import Form from "../../components/Form/Form";
import "./Login.css";

const Login = () => {
  return (
    <div className="loginContainer">
      <div className="loginWrapper">
        <Form type="login" />
      </div>
    </div>
  );
};

export default Login;
