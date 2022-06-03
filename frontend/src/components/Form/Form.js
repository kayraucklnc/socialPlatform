import React from "react";
import Logo from "../Logo/Logo";
import Info from "../Info/Info";
import LoginForm from "../LoginForm/LoginForm";
import RegisterForm from "../RegisterForm/RegisterForm";
import logout from "../../assets/log-out.svg";
import { useHistory } from "react-router-dom";
import "./Form.css";

const Form = ({ type }) => {
  // page type checking for rendering correct component
  let isLogin = type === "login" ? true : false;
  let isRegister = type === "register" ? true : false;

  const user = JSON.parse(sessionStorage.getItem("user"));
  const token = sessionStorage.getItem("token");

  const history = useHistory();

  const redirectTo = (path) => {
    history.push(path);
    window.scrollTo(0, 0);
  };

  return (
    <div className="formContainer">
      {token !== null && user && user.roles[0] === "ROLE_ADMIN" && (
        <div className="toHomeContainer" onClick={() => redirectTo("/home")}>
          <div className="toHomeContainerText">Home Page</div>
          <img src={logout} alt="Log Out" />
        </div>
      )}
      <Logo text="LinkedHU_CENG" />
      {isLogin && (
        <>
          <LoginForm />
          <div></div>
          <Info />
        </>
      )}
      {isRegister && (
        <>
          <RegisterForm />
          <div></div>
        </>
      )}
    </div>
  );
};

export default Form;
