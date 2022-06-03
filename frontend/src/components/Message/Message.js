import React from "react";
import { useState, useEffect } from "react";
import "./Message.css";

const Message = ({ own, imageLink, message, date, keyField }) => {
  const [formattedTime, setFormattedTime] = useState("");

  const timeNowInSeconds = new Date().getTime() / 1000;
  const timestampInSeconds = new Date(date).getTime() / 1000;
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
      setFormattedTime(`${date.toLocaleString()}`);
    }
  };

  useEffect(() => {
    formatTime(timeDiffInSeconds);
  }, []);

  return (
    <div className={own ? "message own" : "message"} key={keyField}>
      <div className="messageTop">
        <img className="messageImg" src={imageLink} alt="" />
        <p className="messageText">{message}</p>
      </div>
      <div className="messageBottom">{formattedTime}</div>
    </div>
  );
};

export default Message;
