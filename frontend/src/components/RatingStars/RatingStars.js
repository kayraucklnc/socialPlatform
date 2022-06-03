import React from "react";
import { useState, useEffect } from "react";
import { useHistory } from "react-router-dom";
import "./RatingStars.css";
import { Star, StarBorder } from "@mui/icons-material";
import Box from "@mui/material/Box";
import Rating from "@mui/material/Rating";

const RatingStars = ({ username, currentuserRating, updateRating }) => {
  const [starFill, setStarFill] = useState(0);
  const [rate, setRate] = useState(0); //Get previous rate from backend
  const [filledStarStyle, setFilledStarStyle] = useState({});
  const [emptyStarStyle, setEmptyStarStyle] = useState({});
  const [fetchStatus, setFetchStatus] = useState(false);
  const [profilename, setProfilename] = useState(username);

  const user = JSON.parse(sessionStorage.getItem("user"));
  const token = sessionStorage.getItem("token");
  const history = useHistory();

  // Submit rate here
  useEffect(() => {
    if (rate){
      const opts = {
        method: "PUT",
        headers: {
          "Content-type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          username: username,
          rating: rate,
        }),
      };

      fetch(`http://localhost:8080/api/rating/username`, opts)
        .then((res) => {
          return res.json();
        })
        .then((data) => {
          return data;
        })
        .then(() => {
          updateRating(!fetchStatus);
          setFetchStatus(!fetchStatus);
        })
        .catch((error) => {
          console.error("There's an error", error);
        });
    }
  },[rate]);

  // Fix visuals if user rated on this profile previously
  useEffect(() => {
    setRate(currentuserRating);
  }, [currentuserRating]);

  
  return (
    <div>
      <Box
        className="cardStars"
        sx={{
          "& > legend": { mt: 2 },
          display: "flex",
        }}
      >
        <Rating
          name="simple-controlled"
          value={rate}
          precision={1}
          size="medium"
          style={{fontSize: 40}}
          onChange={(event,newValue) => {
            if (newValue){
              setRate(newValue);
            }
          }}
        />
      </Box>

    </div>
  );
};

export default RatingStars;
