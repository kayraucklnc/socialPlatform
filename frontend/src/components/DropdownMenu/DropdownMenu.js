import React from "react";
import {
  Person,
  PhotoSizeSelectActual,
  ArrowBack,
  Send,
} from "@mui/icons-material";
import { useState, useEffect, useRef } from "react";
import { CSSTransition } from "react-transition-group";
import "./DropdownMenu.css";

const DropdownMenu = () => {
  const [activeMenu, setActiveMenu] = useState("main");
  const [menuHeight, setMenuHeight] = useState(null);
  const [link, setLink] = useState("");
  const dropdownRef = useRef(null);

  const user = JSON.parse(sessionStorage.getItem("user"));
  const token = sessionStorage.getItem("token");

  useEffect(() => {
    setMenuHeight(dropdownRef.current?.firstChild.offsetHeight);
  }, []);

  const calculateHeight = (el) => {
    const height = el.offsetHeight;
    setMenuHeight(height);
  };

  const profilePhotoHandler = (type) => {
    let profilePhotoLink;

    if (type === "Mario") profilePhotoLink = "./assets/person/1.png";
    if (type === "Kirby") profilePhotoLink = "./assets/person/2.png";
    if (type === "Princess Zelda") profilePhotoLink = "./assets/person/3.png";
    if (type === "Waigu") profilePhotoLink = "./assets/person/4.png";
    if (type === "Donkey Kong") profilePhotoLink = "./assets/person/5.png";

    const opts = {
      method: "PUT",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        profilePicture: profilePhotoLink,
      }),
    };
    fetch("http://localhost:8080/api/profile/update/picture", opts)
      .then((res) => {
        return res;
      })
      .then(() => {
        window.location.reload();
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  const bannerPhotoHandler = (type) => {
    let bannerPhotoLink;

    if (type === "Study Room") bannerPhotoLink = "./assets/banner/1.jpg";
    if (type === "Beach") bannerPhotoLink = "./assets/banner/2.jpg";
    if (type === "Bridge") bannerPhotoLink = "./assets/banner/3.jpg";
    if (type === "Breath of the Wild")
      bannerPhotoLink = "./assets/banner/4.jpg";
    if (type === "Night City") bannerPhotoLink = "./assets/banner/5.jpg";

    const opts = {
      method: "PUT",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        banner: bannerPhotoLink,
      }),
    };
    fetch("http://localhost:8080/api/profile/update/banner", opts)
      .then((res) => {
        return res;
      })
      .then(() => {
        window.location.reload();
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  const bannerLinkHandler = (bannerLink) => {
    const opts = {
      method: "PUT",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        banner: bannerLink,
      }),
    };
    fetch("http://localhost:8080/api/profile/update/banner", opts)
      .then((res) => {
        return res;
      })
      .then(() => {
        window.location.reload();
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  const DropdownItem = (props) => {
    return (
      <div
        className="menuItem"
        onClick={() => props.goToMenu && setActiveMenu(props.goToMenu)}
      >
        <span className="iconButton">{props.leftIcon}</span>
        {props.children}
        <span className="iconRight">{props.rightIcon}</span>
      </div>
    );
  };

  const DropdownBannerLinkItem = (props) => {
    const [newLink, setNewLink] = useState("");
    console.log(newLink);

    return (
      <div
        className="menuItem"
        onClick={() => props.goToMenu && setActiveMenu(props.goToMenu)}
      >
        <span className="iconButton">{props.leftIcon}</span>
        <input
          type="text"
          id="link"
          className="linkText"
          placeholder="Enter Link"
          onChange={(e) => setNewLink(e.target.value)}
        ></input>
        {props.children}
        <span className="iconRight">
          <Send
            sx={{ width: "24px", height: "24px", cursor: "pointer" }}
            onClick={() => bannerLinkHandler(newLink)}
          />
        </span>
      </div>
    );
  };

  const photoLinkHandler = (photoLink) => {
    const opts = {
      method: "PUT",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        profilePicture: photoLink,
      }),
    };
    fetch("http://localhost:8080/api/profile/update/picture", opts)
      .then((res) => {
        return res;
      })
      .then(() => {
        window.location.reload();
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  const DropdownPhotoLinkItem = (props) => {
    const [newLink, setNewLink] = useState("");

    return (
      <div
        className="menuItem"
        onClick={() => props.goToMenu && setActiveMenu(props.goToMenu)}
      >
        <span className="iconButton">{props.leftIcon}</span>
        <input
          type="text"
          id="link"
          className="linkText"
          placeholder="Enter Link"
          onChange={(e) => setNewLink(e.target.value)}
        ></input>
        {props.children}
        <span className="iconRight">
          <Send
            sx={{ width: "24px", height: "24px", cursor: "pointer" }}
            onClick={() => photoLinkHandler(newLink)}
          />
        </span>
      </div>
    );
  };

  return (
    <div className="dropdown" style={{ height: menuHeight }} ref={dropdownRef}>
      <CSSTransition
        in={activeMenu === "main"}
        timeout={500}
        classNames="menu-primary"
        unmountOnExit
        onEnter={calculateHeight}
      >
        <div className="menu unselectable" style={{cursor:"pointer"}} >
          <DropdownItem leftIcon={<Person />} goToMenu="profile">
            Profile Photo
          </DropdownItem>
          <DropdownItem leftIcon={<PhotoSizeSelectActual />} goToMenu="banner">
            Banner
          </DropdownItem>
        </div>
      </CSSTransition>

      <CSSTransition
        in={activeMenu === "profile"}
        timeout={500}
        classNames="menu-secondary"
        unmountOnExit
        onEnter={calculateHeight}
      >
        <div className="menu">
          <DropdownItem goToMenu="main" leftIcon={<ArrowBack />}>
            Profile Photo
          </DropdownItem>
          <div className="profilePhotoLink unselectable" onClick={() => profilePhotoHandler("Mario")}>
            <DropdownItem>
              <div>
                Mario
              </div>
            </DropdownItem>
          </div>
          <div className="profilePhotoLink unselectable" onClick={() => profilePhotoHandler("Kirby")}>
            <DropdownItem>
              <div>
                Kirby
              </div>
            </DropdownItem>
          </div>
          <div className="profilePhotoLink unselectable" onClick={() => profilePhotoHandler("Princess Zelda")}>
            <DropdownItem>
              <div>
                Princess Zelda
              </div>
            </DropdownItem>
          </div>
          <div className="profilePhotoLink unselectable" onClick={() => profilePhotoHandler("Waigu")}>
            <DropdownItem>
              <div>
                Waigu
              </div>
            </DropdownItem>
          </div>
          <div className="profilePhotoLink unselectable" onClick={() => profilePhotoHandler("Donkey Kong")}>
            <DropdownItem>
              <div>
                Donkey Kong
              </div>
            </DropdownItem>
          </div>
          <DropdownPhotoLinkItem leftIcon="🔗"></DropdownPhotoLinkItem>
        </div>
      </CSSTransition>

      <CSSTransition
        in={activeMenu === "banner"}
        timeout={500}
        classNames="menu-secondary"
        unmountOnExit
        onEnter={calculateHeight}
      >
        <div className="menu">
          <DropdownItem goToMenu="main" leftIcon={<ArrowBack />}>
            Banner
          </DropdownItem>
          <div className="bannerPhotoLink unselectable" onClick={() => bannerPhotoHandler("Study Room")}>
            <DropdownItem leftIcon="💻">
              <div>
                Study Room
              </div>
            </DropdownItem>
          </div>
          <div className="bannerPhotoLink unselectable" onClick={() => bannerPhotoHandler("Beach")}>
            <DropdownItem leftIcon="🌊">
              <div>
                Beach
              </div>
            </DropdownItem>
          </div>
          <div className="bannerPhotoLink unselectable" onClick={() => bannerPhotoHandler("Bridge")}>
            <DropdownItem leftIcon="🌉">
              <div>
                Bridge
              </div>
            </DropdownItem>
          </div>
          <div className="bannerPhotoLink unselectable" onClick={() => bannerPhotoHandler("Breath of the Wild")}>
            <DropdownItem leftIcon="🎮">
              <div>
                Breath of the Wild
              </div>
            </DropdownItem>
          </div>
          <div className="bannerPhotoLink unselectable" onClick={() => bannerPhotoHandler("Night City")}>
            <DropdownItem leftIcon="🌃">
              <div>
                Night City
              </div>
            </DropdownItem>
          </div>
          <DropdownBannerLinkItem leftIcon="🔗"></DropdownBannerLinkItem>
        </div>
      </CSSTransition>
    </div>
  );
};

export default DropdownMenu;
