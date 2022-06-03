import React from "react";
import "./Info.css";

const Info = () => {
  return (
    <div>
      <p className="infoP">
        Connect with the associates of Hacettepe University
        <br />
        on LinkedHU_CENG.
      </p>
      <h4 className="infoBottom">
        Developed by{" "}
        <a
          className="link-2"
          href="https://github.com/BBM384-2021/bbm384-2022-demo-final-segmentationfault"
        >
          @SegmentationFault
        </a>{" "}
        <br />
      </h4>
    </div>
  );
};

export default Info;
