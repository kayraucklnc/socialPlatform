import React from "react";
import Form from "../../components/Form/Form";
import "./Register.css";
 
const Register = () => {
  return (
    <div className="registerContainer">
      <div className="registerWrapper">
        <Form type="register" />
      </div>
    </div>
  );
};

export default Register;
