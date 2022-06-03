import React from "react";
import { useState, useEffect } from "react";
import Input from "../Input/Input";
import { Link, Redirect, useHistory } from "react-router-dom";
import Loader from "../Loader/Loader";
import Button from "../Button/Button";
import Status from "../Status/Status";

import "./RegisterForm.css";
import { display } from "@mui/system";

const RegisterForm = () => {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("");

  const [approveUsername, setApproveUsername] = useState("");

  const [isApprove, setIsApprove] = useState(false);
  const [showError, setShowError] = useState(false);
  const [loading, setLoading] = useState(false);
  const [showRoleError, setShowRoleError] = useState(false);
  const [showSuccess, setShowSuccess] = useState(false);
  const [approved, setApproved] = useState(false);
  const [updated, setUpdated] = useState(false);

  const [userList, setUserList] = useState([]);

  const user = JSON.parse(sessionStorage.getItem("user"));
  const token = sessionStorage.getItem("token");

  const history = useHistory();

  const redirectTo = (path) => {
    history.push(path);
    window.scrollTo(0, 0);
  };

  const resetErrors = () => {
    /**
     * Have to remove previous warnings in case of sequential searches
     */
    if (showError === true) {
      setShowError(!showError);
    }
    if (showRoleError === true) {
      setShowRoleError(!showRoleError);
    }
  };

  const roleCheck = (role) => {
    if (role.toLowerCase() === "academician") return true;
    if (role.toLowerCase() === "student") return true;
    if (role.toLowerCase() === "graduate") return true;
    if (role.toLowerCase() === "admin") return true;

    return false;
  };

  const roleFormatter = (role) => {
    if (role.toLowerCase() === "academician") return "ROLE_ACADEMICIAN";
    if (role.toLowerCase() === "student") return "ROLE_STUDENT";
    if (role.toLowerCase() === "graduate") return "ROLE_GRADUATE";
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    // Loader start
    setLoading(true);

    // when it is not a valid role
    if (!roleCheck(role)) {
      setShowRoleError(true);
      // Loader start
      setLoading(false);
      return;
    }

    resetErrors();
    const roleFormatted = roleFormatter(role);
    const opts = {
      method: "POST",
      headers: {
        "Content-type": "application/json",
      },
      body: JSON.stringify({
        first_name: firstName,
        last_name: lastName,
        email: email,
        username: username,
        password: password,
        role: roleFormatted,
      }),
    };
    fetch("http://localhost:8080/api/signup", opts)
      .then((res) => {
        if (res.status === 200) {
          if (showError === true) {
            setShowError(!showError);
          }
          return res;
        } else {
          if (showError === false) {
            setShowError(!showError);
          }
          setLoading(false); // Loader stops
          throw new Error("Promise Chain Cancelled");
        }
      })
      .then(() => {
        setLoading(false); // Loader stops
        setShowSuccess(true);
        if (user) {
          if (user.roles[0] !== "ROLE_ADMIN") {
            setTimeout(() => {
              redirectTo("/login");
            }, 2000);
          }
        }
        setUpdated(!updated);
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  const handleApprove = (event) => {
    event.preventDefault();

    setLoading(true);
    const opts = {
      method: "POST",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        username: approveUsername,
      }),
    };

    resetErrors();
    fetch("http://localhost:8080/api/signup/approve", opts)
      .then((res) => {
        console.log(res);
        return res;
      })
      .then((data) => {
        setLoading(false); // Loader stops
        setApproved(!approved);
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  const options = {
    method: "GET",
    headers: {
      "Content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  };

  useEffect(() => {
    fetch("http://localhost:8080/api/signup", options)
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        setUserList(data);
        return data;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  }, [approved, updated]);

  return (
    <>
      {user && user.roles[0] !== "ROLE_ADMIN" && <Redirect to="/home" />}
      {isApprove ? (
        !loading ? (
          <div className="approveContainer">
            <div className="approveTitle">
              <h4>Approve Users</h4>
              <p>Enter the username of the user you wanted to approve:</p>
            </div>
            <div className="approveList">
              <div className="scrollbar">
                {userList.length > 0 ? (
                  userList.map((u) => (
                    <h4 key={u.username}>
                      {u.username} - {u.role}
                    </h4>
                  ))
                ) : (
                  <p>
                    <span id="no-domain">
                      There is no user waiting to be approved!
                    </span>
                    <br />
                  </p>
                )}
              </div>
            </div>
            <Input
              type="username"
              id="username"
              placeholder="Username"
              onChange={(event) => setApproveUsername(event.target.value)}
            />
            <Button
              text="Continue"
              type="submit"
              buttonType="btn btn-danger"
              onClick={handleApprove}
            />
          </div>
        ) : (
          <Loader />
        )
      ) : (
        <>
          <form onSubmit={handleSubmit}>
            {showError && (
              <h4 className="alert-text">You are already registered!</h4>
            )}
            {showRoleError && (
              <h4 className="alert-text">Please enter a valid role!</h4>
            )}
            {showSuccess && (
              <React.Fragment>
                {token === null || (user && user.roles[0] !== "ROLE_ADMIN") ? (
                  <h4 className="success-text">
                    You can login after you are approved!
                  </h4>
                ) : (
                  <h4 className="success-text">
                    You can now approve this user!
                  </h4>
                )}
              </React.Fragment>
            )}
            <Input
              type="text"
              id="firstName"
              placeholder="First Name"
              onChange={(event) => setFirstName(event.target.value)}
            />
            <Input
              type="text"
              id="lastName"
              placeholder="Last Name"
              onChange={(event) => setLastName(event.target.value)}
            />
            <Input
              type="email"
              id="email"
              placeholder="Email"
              onChange={(event) => setEmail(event.target.value)}
            />
            <Input
              type="username"
              id="username"
              placeholder="Username"
              onChange={(event) => setUsername(event.target.value)}
            />
            <Input
              type="password"
              id="password"
              placeholder="Password"
              onChange={(event) => setPassword(event.target.value)}
            />
            <div className="selectorDiv">
              <select
                onChange={(e) => {
                  let a = document.getElementById("slct");
                  setRole(a.options[a.selectedIndex].text);
                }}
                className="select"
                id="slct"
                required="required"
                defaultValue={""}
              >
                <option value="" disabled="disabled">
                  Select Role
                </option>
                <option value="#">Student</option>
                <option value="#">Academician</option>
                <option value="#">Graduate</option>
                <option value="#">Admin</option>
              </select>
              <Status/>
            </div>
            <Button
              text="Continue"
              type="submit"
              buttonType="btn btn-success"
              onSubmit={handleSubmit}
            />
            {loading && <Loader />}
            {(token === null || (user && user.roles[0] !== "ROLE_ADMIN")) && (
              <h4>
                Already have an account?{" "}
                <Link className="link-1" to="/login">
                  Log In
                </Link>{" "}
              </h4>
            )}
          </form>
        </>
      )}
      {token !== null && user && user.roles[0] === "ROLE_ADMIN" && (
        <div className="paginationButtons">
          {isApprove ? (
            <>
              <Button
                text=""
                buttonType="pagination-btn"
                onClick={() => {
                  setIsApprove(false);
                }}
              />
              <Button
                text=""
                buttonType="pagination-btn pagination-btn-active"
                onClick={() => {}}
              />
            </>
          ) : (
            <>
              <Button
                text=""
                buttonType="pagination-btn pagination-btn-active"
                onClick={() => {}}
              />
              <Button
                text=""
                buttonType="pagination-btn"
                onClick={() => {
                  setIsApprove(true);
                }}
              />
            </>
          )}
        </div>
      )}
    </>
  );
};

export default RegisterForm;
