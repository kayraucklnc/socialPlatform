import React from "react";
import "./AlertSnackbar.css";
import Snackbar from "@mui/material/Snackbar";
import MuiAlert from "@mui/material/Alert";

const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const AlertSnackbar = ({ isNotify, setIsNotify, messageSender}) => {
  return (
    <>
      <Snackbar
        className="profile-snackbar"
        open={isNotify}
        autoHideDuration={6000}
        onClose={() => setIsNotify(false)}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert
          onClose={() => setIsNotify(false)}
          severity="info"
          sx={{ width: "100%" }}
        >
          You have a new message from {messageSender}!
        </Alert>
      </Snackbar>
    </>
  );
};

export default AlertSnackbar;
