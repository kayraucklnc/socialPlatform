import React, { useEffect } from "react";
import { useState } from "react";
import { useParams, useHistory } from "react-router-dom";
import Navbar from "../../components/Navbar/Navbar";
import Sidebar from "../../components/Sidebar/Sidebar";
import Feed from "../../components/Feed/Feed";
import Rightbar from "../../components/Rightbar/Rightbar";
import Like from "../../components/Like/Like";
import AlertSnackbar from "../../components/Snackbar/AlertSnackbar";

import {
  ArrowDropDown,
  Edit,
  Save,
  Star,
  PriorityHigh,
  DownloadForOfflineRounded,
} from "@mui/icons-material";
import { pink } from "@mui/material/colors";
import DropdownMenu from "../../components/DropdownMenu/DropdownMenu";
import "./Profile.css";
import RatingStars from "../../components/RatingStars/RatingStars";
import Follow from "../../components/Follow/Follow";

import Snackbar from "@mui/material/Snackbar";
import MuiAlert from "@mui/material/Alert";

const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const Profile = (props) => {
  const [isSearched, setSearched] = useState(false);
  const [keyword, setKeyword] = useState("");

  const [followed, setFollowed] = useState(false);

  const [rating, setRating] = useState(0);
  const [submittedRatings, setSubmittedRatings] = useState([]);
  const [currentuserRating, setCurrentuserRating] = useState(0);
  const [openDropdown, setOpenDropdown] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const [ping, setPing] = useState(false);

  const [showSuccess, setShowSucess] = useState(false);

  const [about, setAbout] = useState("");
  const [banner, setBanner] = useState("");
  const [profilePicture, setProfilePicture] = useState("");

  const [width, setWidth] = useState(0);

  const [rows, setRows] = useState([]);
  const [isReportDets, setReportDets] = useState(true);
  const [reportLength, setReportLength] = useState(0);

  const widthChangeHandler = (event) => {
    setWidth(event.target.value.length);
  };

  const user = JSON.parse(sessionStorage.getItem("user"));
  const token = sessionStorage.getItem("token");

  const { profilename } = useParams();

  const [isOwnProfile, setOwnProfile] = useState(
    user.sub === profilename ? true : false
  );

  const history = useHistory();

  const redirectTo = (path) => {
    history.push(path);
    window.scrollTo(0, 0);
    history.go(0);
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
    fetch(`http://localhost:8080/api/profile/${profilename}`, opts)
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        setRating(Math.round(data.rating * 100) / 100); 
        setAbout(data.about);
        setBanner(linkFormatter(data.banner));
        setProfilePicture(linkFormatter(data.profilePicture));
        setWidth(data.about.length);

        setOwnProfile(user.sub === profilename ? true : false);
        return data;
      })
      .then(() => {
        fetch(`http://localhost:8080/api/rating/username/${profilename}`, opts)
          .then((res) => {
            return res.json();
          })
          .then((data) => {
            if (
              data === 0 ||
              data === 1 ||
              data === 2 ||
              data === 3 ||
              data === 4 ||
              data === 5
            ) {
              setCurrentuserRating(data);
            }
          })
          .catch((error) => {
            if (error instanceof SyntaxError){
              setCurrentuserRating(0);
            }
            else{
              console.error("There's an error", error);
            }
          });
      })
      .catch((err) => {
        console.error(err);
      });
  }, [profilename]);

  const saveHandler = () => {
    setIsEdit(!isEdit);

    const opts = {
      method: "PUT",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        about: about,
      }),
    };
    fetch("http://localhost:8080/api/profile/update/about", opts)
      .then((res) => {
        return res;
      })
      .then((data) => {})
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  const editHandler = () => {
    setIsEdit(!isEdit);
  };

  useEffect(() => {
    const opts = {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    };

    fetch(
      `http://localhost:8080/api/rating/average/username/${profilename}`,
      opts
    )
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        if (data !== null) {
          setRating(Math.round(data * 100) / 100);
        }
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  }, [ping]);

  const reportInputChangeHandler = () => {
    setReportLength(document.getElementById("reportInputField").value.length);
  };

  const reportUserTab = () => {
    setReportDets(!isReportDets);
    if (isReportDets) {
      document.getElementById("reportDets").style.display = "flex";
    } else {
      document.getElementById("reportDets").style.display = "none";
    }
  };

  const reportUserReason = () => {
    reportUserTab();
    setReportLength(0);
    let reason = document.getElementById("reportInputField").value;
    document.getElementById("reportInputField").value = "";
    if (reason === "") {
      reason = "No reason";
    }
    fetch("http://localhost:8080/api/report", {
      method: "POST",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        username: profilename,
        reportContent: reason,
      }),
    })
      .then((res) => {
        setShowSucess(true);
        return res;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  function dataParser(data) {
    let picked = {};
    picked.about = data.about;
    picked.email = data.email;
    picked.username = data.username;
    picked.firstName = data.firstName;
    picked.lastName = data.lastName;
    picked.rating = Math.round(data.rating * 100) / 100;
    picked.posts = [];
    for (const a of data.posts) {
      let k = {};
      k.title = a.title;
      k.content = a.content;
      k.timestamp = a.timestamp;
      k.photoLink = a.photoLink;
      k.likedCount = a.likedByUsers.length;
      picked.posts.push(k);
    }
    return picked;
  }

  const download_table_as_csv = () => {
    const opts = {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    };

    fetch(`http://localhost:8080/api/requestinfo/${profilename}`, opts)
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        const element = document.createElement("a");
        const file = new Blob(
          ["\ufeff", JSON.stringify(dataParser(data), null, 2)],
          {
            type: "text/plain",
          }
        );
        element.href = URL.createObjectURL(file);
        element.download = `${profilename}.txt`;
        document.body.appendChild(element);
        element.click();
        document.body.removeChild(element);

        return data;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  // Following functionality will be handled here

  const followGetterHandler = () => {
    fetch("http://localhost:8080/api/follow/username/" + profilename, {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        return res.json();
      })
      .then((e) => {
        setFollowed(e.following);
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  const handleFollow = () => {
    fetch("http://localhost:8080/api/follow/username/" + profilename, {
      method: "POST",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        return res.json();
      })
      .then((e) => {
        setFollowed(e.following);
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  useEffect(() => {
    followGetterHandler();
  }, [props]);

  return (
    <>
      <Navbar setSearched={props.setSearched} setKeyword={props.setKeyword} />
      <AlertSnackbar
        isNotify={props.isNotify}
        setIsNotify={props.setIsNotify}
        messageSender={props.messageSender}
      />
      <div className="profile">
        <div className="profileRight">
          <div className="profileRightTop">
            <div className="profileCover">
              <img className="profileCoverImg" src={banner} alt="" />
              <div className="profileRateStars">
                <RatingStars
                  username={profilename}
                  currentuserRating={currentuserRating}
                  updateRating={setPing}
                />
              </div>
              <img className="profileUserImg" src={profilePicture} alt="" />
              <div className="aboutIcons">
                {isEdit
                  ? isOwnProfile && (
                      <Save
                        className="infoIcons"
                        sx={{
                          color: "#ff6a6a",
                          width: "20px",
                          height: "20px",
                          padding: "1px",
                        }}
                        onClick={saveHandler}
                      />
                    )
                  : isOwnProfile && (
                      <Edit
                        className="infoIcons"
                        sx={{
                          color: "white",
                          width: "20px",
                          height: "20px",
                          padding: "1px",
                        }}
                        onClick={editHandler}
                      />
                    )}
              </div>
              <div className="profileRating">
                <Star className="profileRateCurrentStar" />{" "}
                <h2 className="profileRate">{rating}</h2>
                <h2 className="profileMaxRate">/5</h2>
              </div>
              {!isOwnProfile && (
                <div className="followArea" onClick={() => handleFollow()}>
                  <Follow followed={followed} />
                </div>
              )}
              {user &&
                (user.roles[0] === "ROLE_ADMIN" ||
                  user.roles[0] === "ROLE_ACADEMICIAN") && (
                  <div className="downloadUserInfo">
                    <DownloadForOfflineRounded
                      className="download-icon"
                      style={{ fontSize: 40 }}
                      onClick={download_table_as_csv}
                    />
                  </div>
                )}
              {isOwnProfile && (
                <ArrowDropDown
                  style={{ color: "white", height: "30px", width: "30px" }}
                  className="dropDownIcon infoIcons"
                  onClick={() => setOpenDropdown(!openDropdown)}
                />
              )}
              {/*<HomeOutlined
                style={{ height: "30px", width: "30px" }}
                className="homeIcon infoIcons"
                onClick={() => {
                  redirectTo("/home");
                  window.scrollTo(0, 0);
                }}
              />*/}
              {openDropdown && <DropdownMenu />}
              {!isOwnProfile && (
                <div>
                  <Snackbar
                    className="profile-snackbar"
                    open={showSuccess}
                    autoHideDuration={6000}
                    onClose={() => setShowSucess(false)}
                    anchorOrigin={{ vertical: "top", horizontal: "right" }}
                  >
                    <Alert
                      onClose={() => setShowSucess(false)}
                      severity="success"
                      sx={{ width: "100%" }}
                    >
                    Your report is submitted successfully!
                    </Alert>
                  </Snackbar>
                  <div id="reportDets" className="reportDetails">
                    <hr />
                    <h2>Reporting `{profilename}`</h2>
                    <hr style={{ marginBottom: 18 }} />
                    <h4>
                      Reason ({80 - reportLength}):
                      <input
                        placeholder="No reason"
                        className="reportInput"
                        type="text"
                        name="name"
                        id="reportInputField"
                        autoComplete="off"
                        maxLength={80}
                        onChange={reportInputChangeHandler}
                      />
                    </h4>

                    <button
                      className="btn shareButton reportButton"
                      onClick={() => {
                        reportUserReason();
                      }}
                    >
                      Report User
                    </button>
                  </div>

                  <PriorityHigh
                    style={{ color: pink[500], height: "30px", width: "30px" }}
                    className="dropDownIcon infoIcons"
                    onClick={() => reportUserTab()}
                  />
                </div>
              )}
            </div>
            <div className="profileInfo">
              <h4 className="profileInfoName">{profilename}</h4>
              {!isEdit ? (
                <span className="profileInfoDesc">{about}</span>
              ) : (
                <div className="inputGroup">
                  <input
                    type="text"
                    className="aboutInput"
                    defaultValue={about}
                    onChange={(event) => {
                      setAbout(event.target.value);
                      widthChangeHandler(event);
                    }}
                    style={{ width: width + "ch" }}
                  />
                  <span className="bar"></span>
                </div>
              )}
            </div>
          </div>
          <div className="profileRightBottom">
            <div className="profileSidebar">
              <Sidebar />
            </div>
            <div className="profileFeed">
              <Feed type="profile" profilename={profilename}/>
            </div>
            <Rightbar profile profilename={profilename}/>
          </div>
        </div>
      </div>
    </>
  );
};
export default Profile;
