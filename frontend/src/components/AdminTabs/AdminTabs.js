import React from "react";
import { useState } from "react";
import RequestTab from "./RequestTab";
import ReportManage from "./ReportManage";
import ApproveUsersTab from "./ApproveUsersTab";
import "./AdminTabs.css";

const AdminTabs = () => {
  const [toggleState, setToggleState] = useState(1);
  const [pingUserList, setPingUserList] = useState(false);

  const toggleTab = (index) => {
    setToggleState(index);
  };

  return (
    <div className="tabWrapper">
      <div className="tabContainer">
        <div className="tabsBlock">
          <button
            className={toggleState === 1 ? "tabs active-tabs" : "tabs"}
            onClick={() => toggleTab(1)}
          >
            All User Info
          </button>
          <button
            className={toggleState === 2 ? "tabs active-tabs" : "tabs"}
            onClick={() => toggleTab(2)}
          >
            Approve Users
          </button>
          <button
            className={toggleState === 3 ? "tabs active-tabs" : "tabs"}
            onClick={() => toggleTab(3)}
          >
            All Reports
          </button>
        </div>

        <div style={{ minHeight: "800px" }} className="content-tabs">
          <div
            style={{ minHeight: "800px", paddingBottom: 80 }}
            className={
              toggleState === 1 ? "content  active-content" : "content"
            }
          >
            <RequestTab pingUserList={pingUserList} />
          </div>

          <div
            className={
              toggleState === 2 ? "content  active-content" : "content"
            }
          >
            <ApproveUsersTab
              setPingUserList={setPingUserList}
              pingUserList={pingUserList}
            />
          </div>

          <div
            className={
              toggleState === 3 ? "content  active-content" : "content"
            }
          >
            <ReportManage />
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminTabs;
