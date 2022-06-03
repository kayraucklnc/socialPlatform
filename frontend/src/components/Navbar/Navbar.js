import "./Navbar.css";
import { useState, useEffect } from "react";
import { Person, Chat, Notifications, HomeOutlined } from "@mui/icons-material";
import Searchbar from "../Searchbar/Searchbar";
import logo from "../../assets/whiteLogo.svg";
import { useHistory } from "react-router-dom";

const Navbar = ({ setSearched, setKeyword }) => {
  const user = JSON.parse(sessionStorage.getItem("user"));
  const token = sessionStorage.getItem("token");
  const history = useHistory();

  const [portal, setPortal] = useState(false);
  const [profileImage, setProfileImage] = useState("");

  const redirectTo = (path) => {
    history.push(path);
    window.scrollTo(0, 0);
  };

  const linkFormatter = (link) => {
    if (link.charAt(0) === ".") {
      link = link.substring(1);
    }

    return link;
  };

  const opts = {
    method: "GET",
    headers: {
      "Content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  };

  useEffect(() => {
    fetch(`http://localhost:8080/api/profile/${user.sub}`, opts)
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        setProfileImage(linkFormatter(data.profilePicture));

        return data;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  }, [user.sub]);

  return (
    <div className="navbarContainer">
      <div className="navbarLeft">
        <span className="logoContainer">
          <img
            className="navbarLogo"
            src={logo}
            alt="logo"
            onClick={() => {
              setSearched(false);
              setPortal(false);
              setKeyword("");
              redirectTo("/home");
            }}
          ></img>
        </span>
      </div>
      <div className="navbarCenter">
        <Searchbar setSearched={setSearched} setKeyword={setKeyword} />
      </div>
      <div className="navbarRight">
        <div className="navbarIcons">
          <HomeOutlined
            style={{ height: "35px", width: "35px" }}
            className="homeNavbarIcon infoNavbarIcons"
            onClick={() => {
              redirectTo("/home");
              window.scrollTo(0, 0);
            }}
          />
          <div
            className="profileArea unselectable"
            onClick={() => redirectTo(`/profile/${user.sub}`)}
          >
            <img src={profileImage} alt="" className="navbarProfilePicture" />
            <span className="profileAreaText">{user.sub}</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Navbar;
