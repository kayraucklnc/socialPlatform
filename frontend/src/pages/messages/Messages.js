import React from "react";
import { useState, useEffect, useRef } from "react";
import Conversation from "../../components/Conversation/Conversation";
import Message from "../../components/Message/Message";
import Navbar from "../../components/Navbar/Navbar";
import Sidebar from "../../components/Sidebar/Sidebar";
import FriendList from "../../components/FriendList/FriendList";
import AlertSnackbar from "../../components/Snackbar/AlertSnackbar";
import Input from "../../components/Input/Input";
import { Send } from "@mui/icons-material";
import "./Messages.css";
import Snackbar from "@mui/material/Snackbar";
import MuiAlert from "@mui/material/Alert";

const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const Messages = (props) => {
  const [followedUsers, setFollowedUsers] = useState([]);
  const [followers, setFollowers] = useState([]);
  const [messages, setMessages] = useState([]);
  const [currentMessage, setCurrentMessage] = useState(null);
  const [profileImage, setProfileImage] = useState("");
  const [sentNewMessage, setSentNewMessage] = useState(false);
  const [isEmpty, setIsEmpty] = useState(false);
  const [firstMessage, setFirstMessage] = useState(false);
  const [firstMessageTrigger, setFirstMessageTrigger] = useState(false);
  const scrollRef = useRef();

  const [newMessage, setNewMessage] = useState("");
  const userId = sessionStorage.getItem("id");

  const token = sessionStorage.getItem("token");
  const user = JSON.parse(sessionStorage.getItem("user"));

  useEffect(() => {
    fetch("http://localhost:8080/api/follow/followedUsers", {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        setFollowedUsers(data);
        return data;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });

    fetch("http://localhost:8080/api/follow/followers/username/" + user.sub, {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        setFollowers(data);
        return data;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  }, []);

  const linkFormatter = (link) => {
    if (link.charAt(0) === ".") {
      link = link.substring(1);
    }

    return link;
  };

  useEffect(() => {
    fetch(`http://localhost:8080/api/profile/${user.sub}`, {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
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

  useEffect(() => {
    // we'll use currentMessage state to fetch the messages here
    // setMessages will be used as well
    if (currentMessage) {
      fetch(`http://localhost:8080/messages/${userId}/${currentMessage.id}`, {
        method: "GET",
        headers: {
          "Content-type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
        .then((res) => {
          return res.json();
        })
        .then((data) => {
          setMessages(data);
          console.log(data);
          if (data.length === 0 && firstMessage) {
            setFirstMessageTrigger(!firstMessageTrigger);
          }
          return data;
        })
        .catch((error) => {
          console.error("There's an error", error);
        });
    }
  }, [currentMessage, sentNewMessage, props.isNotify, firstMessageTrigger]);

  useEffect(() => {
    scrollRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const handleSubmit = (event) => {
    event.preventDefault();
    if (newMessage === "") {
      setIsEmpty(true);
      return;
    }
    const message = {
      senderId: userId,
      recipientId: currentMessage.id,
      senderName: user.sub,
      recipientName: currentMessage.username,
      content: newMessage,
    };
    console.log("HERE NOW:");
    console.log(props.stompClient);
    props.stompClient.send("/app/chat", {}, JSON.stringify(message));
    setSentNewMessage(!sentNewMessage);
    setNewMessage("");
    setFirstMessage(true);
  };

  return (
    <>
      <Navbar setSearched={props.setSearched} setKeyword={props.setKeyword} />
      <Snackbar
        className="profile-snackbar"
        open={isEmpty}
        autoHideDuration={6000}
        onClose={() => setIsEmpty(false)}
        anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
      >
        <Alert
          onClose={() => setIsEmpty(false)}
          severity="warning"
          sx={{
            width: "100%",
            backgroundColor: "#f50057",
            marginBottom: "0.5rem",
          }}
        >
          Message is empty!
        </Alert>
      </Snackbar>
      <AlertSnackbar
        isNotify={props.isNotify}
        setIsNotify={props.setIsNotify}
        messageSender={props.messageSender}
      />
      <div className="messages">
        <Sidebar />
        <div className="messageBox">
          <div className="messageBoxWrapper">
            {currentMessage ? (
              <>
                <div className="messageBoxTop">
                  {messages.map((m) => (
                    <div ref={scrollRef}>
                      <Message
                        keyField={m.id}
                        own={m.senderName === user.sub}
                        message={m.content}
                        imageLink={
                          m.senderName === user.sub
                            ? profileImage
                            : currentMessage.profile.profilePicture
                        }
                        date={m.timestamp}
                      />
                    </div>
                  ))}
                </div>
                <div className="messageBoxBottom">
                  <textarea
                    className="messageBoxInput"
                    placeholder="Enter Your Message"
                    onChange={(e) => setNewMessage(e.target.value)}
                    value={newMessage}
                  ></textarea>
                  <div className="messageBoxSubmitButton">
                    <Send
                      onClick={handleSubmit}
                      style={{ color: "white", height: "32px", width: "32px" }}
                    />
                  </div>
                </div>
              </>
            ) : (
              <h4 className="noConversation">Select a Conversation</h4>
            )}
          </div>
        </div>
        <div className="messageMenu">
          <div className="messageMenuWrapper">
            <div className="messageMenuUsers">
              {followedUsers.map((u) => (
                <div
                  key={u.id}
                  onClick={() => {
                    setCurrentMessage(u);
                    setFirstMessage(false);
                  }}
                >
                  <Conversation
                    key={u.id}
                    imageLink={linkFormatter(u.profile.profilePicture)}
                    followedUsername={u.firstName + " " + u.lastName}
                    currentConvo={u?.id === currentMessage?.id}
                  />
                </div>
              ))}
              {followers
                .filter((u1) =>
                  followedUsers.every((u2) => u1.username !== u2.username)
                )
                .map((u) => (
                  <div
                    key={u.id}
                    onClick={() => {
                      setCurrentMessage(u);
                      setFirstMessage(false);
                    }}
                  >
                    <Conversation
                      key={u.id}
                      imageLink={linkFormatter(u.profile.profilePicture)}
                      followedUsername={u.firstName + " " + u.lastName}
                      currentConvo={u?.id === currentMessage?.id}
                    />
                  </div>
                ))}
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Messages;
