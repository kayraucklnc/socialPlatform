import React from "react";
import { useState, useEffect} from "react";
import { useHistory } from "react-router-dom";
import "./Share.css";
import { PermMedia, Label } from "@mui/icons-material";
import Button from "../Button/Button";

const Share = ({jobs}) => {
  const [showError, setShowError] = useState(false);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [image, setImage] = useState("");
  const [imageInput, setImageInput] = useState(false);
  const [isAnnouncement, setIsAnnouncement] = useState(false);

  const [profileImage, setProfileImage] = useState("");

  const user = JSON.parse(sessionStorage.getItem("user"));
  const token = sessionStorage.getItem("token");
  const history = useHistory();

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

  const imageHandle = () => {
    setImageInput(!imageInput);
  }
  
  const announcementHandle = () => {
    setIsAnnouncement(!isAnnouncement);
  }

  const redirectTo = (path) => {
    history.push(path);
    window.scrollTo(0, 0);
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    if ((title === "") && (content === "")){
      setShowError(true);
      return;
    }
    
    else if (isAnnouncement && (title === "")){
      setShowError(true);
      return;
    }

    // Creating Post
    if(!isAnnouncement && !jobs){
      const opts = {
        method: "POST",
        headers: {
          "Content-type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({
          title: title,
          photoLink: image,
          content: content,
        }),
      };
      fetch("http://localhost:8080/api/post", opts)
        .then((res) => {
          console.log(res);
        })
        .then(() => {
          window.location.reload();
        })
        .catch((error) => {
          console.error("There's an error", error);
        });
    }
    // Creating Announcement
    else if(isAnnouncement){
      const opts = {
        method: "POST",
        headers: {
          "Content-type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({
          title: title,
          content: content,
        }),
      };
      fetch("http://localhost:8080/api/announcement/create", opts)
        .then((res) => {
          console.log(res);
        })
        .then(() => {
          window.location.reload();
        })
        .catch((error) => {
          console.error("There's an error", error);
        });
    }
    else if(jobs){
      const opts = {
        method: "POST",
        headers: {
          "Content-type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({
          title: title,
          content: content,
          photoLink: image,
        }),
      };
      fetch("http://localhost:8080/api/scholarshipJob/", opts)
        .then((res) => {
          console.log(res);
        })
        .then(() => {
          window.location.reload();
        })
        .catch((error) => {
          console.error("There's an error", error);
        });
    }
  }


  return (
    <div className="share">
      <div className="shareWrapper">
        <form className="shareForm" onSubmit={handleSubmit}>
          <div className="shareTop">
            <img
              className="shareProfileImg"
              src={profileImage}
              alt=""
            />
            <input
              placeholder="What do you want to share?"
              className="shareInput"
              onChange={(event) => setTitle(event.target.value)}
            />
          </div>
          <hr className="shareHr" />
          <div className="shareDesc"> 
            <input 
              placeholder="Description (optional)"
              className="shareInput"
              onChange={(event) => setContent(event.target.value)}
            />
          </div>
          <hr className="shareHr" />
          <div className="shareBottom">
            <div className="shareOptions">
              {!isAnnouncement && (
                <div className="shareOption" onClick={imageHandle}>
                  <PermMedia color="success" className="shareIcon" />
                  <span className="shareOptionText unselectable">Photo</span>
                </div>
              )}
              {isAnnouncement && (
                <div className="shareOption">
                  <PermMedia color="disabled" className="shareIcon" />
                  <span className="shareOptionTextFade unselectable">Photo</span>
                </div>
              )}

              {!jobs && user && (user.roles[0] === "ROLE_ADMIN" || user.roles[0] === "ROLE_ACADEMICIAN") && !isAnnouncement && (
                <div className="shareOption" onClick={() => {announcementHandle(); setImageInput(false)}}>
                  <Label color="disabled" className="shareIcon" />
                  <span className="shareOptionTextFade unselectable" >Announcement</span>
                </div>
              )}

              {!jobs && user && (user.roles[0] === "ROLE_ADMIN" || user.roles[0] === "ROLE_ACADEMICIAN") && isAnnouncement && (
                <div className="shareOption" onClick={announcementHandle}>
                  <Label color="primary" className="shareIcon" />
                  <span className="shareOptionText unselectable" >Announcement</span>
                </div>
              )}
            </div>
            {showError && (
              <h4 className="alert-text">Invalid Title or Description!</h4>
            )}
            {(!isAnnouncement && !jobs) && (
              <Button text="Share" type="submit" buttonType="btn shareButton unselectable"/>
            )}
            {isAnnouncement && (
              <Button text="Announce" type="submit" buttonType="btn shareButton announceButton"/>
            )}
            {jobs && (
              <Button text="Post Application Form" type="submit" buttonType="btn shareButton unselectable applicationButton"/>
            )}
          </div>
          {imageInput && (
            <input 
              placeholder="Image link"
              className="shareInput shareImage"
              onChange={(event) => setImage(event.target.value)}
            />)
          }
        </form>
      </div>
    </div>
  );
};

export default Share;
