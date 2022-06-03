import React, { useState, useEffect } from "react";
import {Download} from "@mui/icons-material";
import { useHistory } from "react-router-dom";

import "./JobApplication.css";

const JobApplication = ({application, setApplicationsCheck, applicationsCheck}) => {

  const [content, setContent] = useState(application.content);
  const [profilename, setProfilename] = useState(application.author);
  const [resume, setResume] = useState({});

  const token = sessionStorage.getItem("token");
  const history = useHistory();

  useEffect(() => {
    // Process the file retrieved
    // file = new Blob(application.resume) ...
    setResume(application.file);
  },[application])

  const deleteHandler = () => {
    const opts = {
      method: "DELETE",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    };
    fetch(`http://localhost:8080/api/scholarshipJob/appeal/${application.id}`, opts)
      .then((res) => {
        return res;
      })
      .then((data) => {
        setApplicationsCheck(!applicationsCheck);
        return data;
      });
  }

  const downloadHandler = () => {
    const opts = {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    };
    fetch(`http://localhost:8080/api/scholarshipJob/appeal/resume/${application.id}`, opts)
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        var base64str = data.resume;

        // decode base64 string, remove space for IE compatibility
        var binary = atob(base64str.replace(/\s/g, ''));
        var len = binary.length;
        var buffer = new ArrayBuffer(len);
        var view = new Uint8Array(buffer);
        for (var i = 0; i < len; i++) {
            view[i] = binary.charCodeAt(i);
        }

        // create the blob object with content-type "application/pdf"               
        var blob = new Blob( [view], { type: "application/pdf" });
        var url = URL.createObjectURL(blob);

        const link = document.createElement("a");

        link.href = url;
        link.download = data.resumeFileName;
      
        document.body.appendChild(link);
        link.dispatchEvent(
          new MouseEvent('click', { 
            bubbles: true, 
            cancelable: true, 
            view: window 
          })
        );
      
        // Remove link from body
        document.body.removeChild(link);
        
        return data;
      });
  }

  const redirectTo = (path) => {
    history.push(path);
    window.scrollTo(0, 0);
  };

  return (
    <div className="application">
      <div className="applicationBody">
        <img
          className="applicationProfilePic"
          src={application.applicant.profile.profilePicture}
          alt=""
        />
        <div className="applicationBox">
          <div className="applicationAuthor" onClick={() => redirectTo(`/profile/${application.applicant.username}`)}> 
            {application.applicant.username} 
          </div>
          {application.resumeFileName !== "" && (<div className="downloadResume unselectable" onClick={downloadHandler} > <Download style={{fontSize: 20}} /> Download Resume {`(${application.resumeFileName})`} </div>)}
          {application.description !== "" && (<div className="extraNotes">{application.description} </div>)}
        </div>
      </div>
      <div className="applicationFooter">
        <a className={"unselectable deleteBtnPassive"} onClick={deleteHandler}> Delete </a>
      </div>
    </div>
  );
};

export default JobApplication;
