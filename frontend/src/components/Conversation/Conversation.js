import React from "react";
import "./Conversation.css";

const Conversation = ({imageLink, followedUsername, currentConvo}) => {
  return (
    <div className={currentConvo ? "conversation selectedConversation" : "conversation"}>
      <img className="conversationImg" src={imageLink} alt="" />
      <span className="conversationName">{followedUsername}</span>
    </div>
  );
};

export default Conversation;
