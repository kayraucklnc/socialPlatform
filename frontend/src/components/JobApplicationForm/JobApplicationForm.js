import React, { useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import { CSSTransition } from "react-transition-group";
import { Delete, Edit, Save, HistoryEdu, SendRounded, Person } from "@mui/icons-material";
import Button from '@mui/material/Button'
import { styled } from '@mui/material/styles';

import JobApplication from "../JobApplication/JobApplication";
import "./JobApplicationForm.css";

const JobApplicationForm = ({ applicationForm }) => {

  const [isApply, setIsApply] = useState(false);
  const [formattedTime, setFormattedTime] = useState("");
  const [isEdit, setIsEdit] = useState(false);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [photoLink, setPhotoLink] = useState("");
  const [resume, setResume] = useState(null);
  const [showError, setShowError] = useState(false);
  const [extraNoteContent, setExtraNoteContent] = useState("");
  const [applicationsCheck, setApplicationsCheck] = useState(false);
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);

  const history = useHistory();
  const user = JSON.parse(sessionStorage.getItem("user"));
  const token = sessionStorage.getItem("token");

  const timestamp = new Date(); 

  const timeNowInSeconds = new Date().getTime() / 1000;
  const timestampInSeconds = new Date().getTime() / 1000;
  const timeDiffInSeconds = timeNowInSeconds - timestampInSeconds;

  const formatTime = (sec) => {
    // 1 hour = 3600 seconds || 1 day = 86400 seconds || 14 days = 1209600 seconds
    if (sec < 60) {
      // Under a minute -> Say "just now"
      setFormattedTime(`just now`);
    } else if (sec < 3600) {
      // Under a hour -> Say "x mins ago"
      setFormattedTime(`${Math.floor(sec / 60)} minutes ago`);
    } else if (sec < 86400) {
      // Under 24 hours -> Say "x hours ago"
      setFormattedTime(`${Math.floor(sec / 3600)} hours ago`);
    } else if (sec < 1209600) {
      // Under 14 days -> Say "x days ago"
      setFormattedTime(`${Math.floor(sec / 86400)} days ago`);
    } else {
      // After 14 days -> Say Day/Month/Year
      setFormattedTime(`${timestamp.toLocaleString()}`);
    }
  };

  useEffect(() => {
    formatTime(timeDiffInSeconds);
  }, []);

  const resetErrors = () => {
    setShowError(false);
  };

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

  const editHandler = () => {
    resetErrors();
    setIsEdit(!isEdit);
    setTitle(applicationForm?.title);
    setContent(applicationForm?.content);
    setPhotoLink(applicationForm?.photoLink);
  };

  const saveHandler = () => {
    setIsEdit(!isEdit);

    if (title === "" && content === "") {
      setShowError(true);
      setTimeout(() => {
        setShowError(false);
      }, 2000);
      return;
    }

    const opts = {
      method: "PUT",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        title: title,
        photoLink: photoLink,
        content: content,
      }),
    };
    fetch(`http://localhost:8080/api/scholarshipJob/${applicationForm.id}`, opts)
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

  const deleteHandler = () => {
    const opts = {
      method: "DELETE",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    };
    fetch(`http://localhost:8080/api/scholarshipJob/${applicationForm.id}`, opts)
      .then((res) => {
        return res;
      })
      .then((data) => {
        window.location.reload();
        return data;
      });
  };

  const applyHandler = () => {
    setIsApply(!isApply);
  }

  const applySendHandler = () => {

    let formData = new FormData();

    formData.append("description", extraNoteContent);
    formData.append("resumeFileName", resume ? resume.name : "");
    formData.append("resume", resume ? resume : new Blob());

    const opts = {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: formData
    };
    fetch(`http://localhost:8080/api/scholarshipJob/appeal/${applicationForm.id}`, opts)
    .then((res) => {
      return res;
    })
    .then((data) => {
      console.log(data);
      setApplicationsCheck(!applicationsCheck);
      return data;
    })
    .catch((err) => {
      console.log(err);
    });
    
  }

  useEffect(() => {
    setLoading(true);
    const opts = {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      }
    };
    fetch(`http://localhost:8080/api/scholarshipJob/appeal/of/${applicationForm.id}`, opts)
    .then((res) => {
      return res.json();
    })
    .then((data) => {
      setApplications(data);
      setLoading(false);
      return data;
    })
    .catch((err) => {
      console.error(err);
    });
  },[applicationsCheck]);

  const UploadPdfInput = styled('input')({
    display: 'none',
  });

  const handleResume = (e) => {
    const file = e.target.files[0];
    setResume(file);
  }

  return (
    <div className="post">
      <div className="postWrapper">
        <div className="postTop">
          <div className="postTopLeft">
            <img
              className="postProfileImg"
              src={linkFormatter(applicationForm.author.profile.profilePicture)}
              alt=""
            />
            <span
              className="postUsername"
              onClick={() => redirectTo(`/profile/${applicationForm.author.username}`)}
            >
              {applicationForm.author.username}
            </span>
            <span className="postDate">{formattedTime}</span>
          </div>
          {(applicationForm.author.username === user.sub || user.roles[0] === "ROLE_ADMIN") && (
            <div className="postTopRight">
              {isEdit ? (
                <Save className="postOptionsBtn" onClick={saveHandler} />
              ) : (
                <React.Fragment>
                  <Delete
                    className="postOptionsBtn"
                    style={{ cursor: "pointer" }}
                    onClick={deleteHandler}
                  />
                  <Edit
                    className="postOptionsBtn"
                    style={{ marginLeft: "10px" }}
                    onClick={editHandler}
                  />
                </React.Fragment>
              )}
            </div>
          )}
        </div>
        <div className="postCenter">
          {showError && (
            <h4 className="alert-text invalidEdit">
              Invalid Title and Description!
            </h4>
          )}
          {!isEdit ? (
            <h2 className="postTitle">{applicationForm?.title}</h2>
          ) : (
            <input
              className="editInput postTitle"
              type="text"
              defaultValue={applicationForm?.title}
              onChange={(event) => setTitle(event.target.value)}
              placeholder="Title"
            />
          )}

          {!isEdit ? (
            <span className="postText">{applicationForm?.content}</span>
          ) : (
            <input
              className="editInput postText"
              type="text"
              defaultValue={applicationForm?.content}
              onChange={(event) => setContent(event.target.value)}
              placeholder="Content"
            />
          )}
          {!isEdit ? (
            <img className="postImg" src={applicationForm?.photoLink} alt="" />
          ) : (
            <input
              className="editInput postText"
              type="text"
              defaultValue={applicationForm?.photoLink}
              onChange={(event) => setPhotoLink(event.target.value)}
              placeholder="Image Link"
            />
          )}
        </div>
        <hr></hr>

        <div className="allApplications">
          {!loading && (applications.map((a) => 
            <JobApplication key={a.id} application={a} setApplicationsCheck={setApplicationsCheck} applicationsCheck={applicationsCheck}/>
          ))}
        </div>

        <div className="apply">
          <div className="postBottom">
            {isApply && (
              <div className="uploadPDF">
                <label htmlFor="contained-button-file">
                  <UploadPdfInput accept="application/pdf" id="contained-button-file" type="file" onChange={(e) => handleResume(e)}/>
                  <Button variant="contained" component="span">          
                    {"Upload Resume (As PDF)"}
                  </Button>
                </label>
                {resume && <span className="pdfName">{resume.name}</span>}
              </div>
            )}
            {!isApply && (
              <div className="postBottomLeft">
                {(user.sub === applicationForm.author.username || user.roles[0] === "ROLE_ADMIN") && (
                  <div style={{display: "contents"}}>
                    <Person className="applicantIcon"/>
                    <span className="postLikeCounter">
                      {applications.length + " Applicants"} 
                    </span>
                  </div>
                )}
              </div>
            )}
            <div className="postBottomRight" onClick={applyHandler} >
              <span className="applyText unselectable"> <HistoryEdu style={{marginRight:4}} /> Apply </span>
            </div>
          </div>
          <CSSTransition
            in={isApply}
            classNames="applyToForm"
            timeout={500}
            unmountOnExit> 
            <div className="applyToForm">
              <br></br>
              <input
                id={`applyInputField`}
                className="applyInput"
                type="text"
                placeholder={"Extra notes!"}
                onChange={(event) => setExtraNoteContent(event.target.value)}
              />
              <SendRounded className="applySendIcon" style={{fontSize:30}} onClick={applySendHandler} />
            </div>
          </CSSTransition>
        </div>
      </div>
    </div>
  );
};

export default JobApplicationForm;
