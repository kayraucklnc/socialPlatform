import React, {useState} from "react";
import { Search } from "@mui/icons-material";
import "./Searchbar.css";
import { useHistory } from "react-router-dom";

const Searchbar = ({setSearched, setKeyword}) => {


  const history = useHistory();

  const redirectTo = (path) => {
    history.push(path);
    window.scrollTo(0, 0);
  };

  const handleSubmit = (event) => {
    setSearched(true);
    setKeyword(event.target[0].value);
    if (event.target[0].value === ""){
      setSearched(false);
    }
    redirectTo("/home");
    event.preventDefault();
    return false;
  };

  return (
    <div className="searchbar">
      <Search className="searchIcon" />
      <form className="searchForm"  onSubmit={(event) => handleSubmit(event)}>
        <input
          placeholder="Search for posts, announcements and users"
          className="searchInput"
        />
      </form>
    </div>
  );
};

export default Searchbar;
