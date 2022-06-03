import React, { useState, useEffect } from "react";
import { styled } from "@mui/material/styles";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableRow from "@mui/material/TableRow";
import { colors } from "@mui/material";
import { DataGrid, GridColDef, GridValueGetterParams } from "@mui/x-data-grid";

import DownloadForOfflineRoundedIcon from "@mui/icons-material/DownloadForOfflineRounded";

const RequestTab = ({ pingUserList }) => {
  const [pageSize, setPageSize] = useState(20);

  const StyledTableCell = styled(TableCell)(({ theme }) => ({
    [`&.${tableCellClasses.head}`]: {
      backgroundColor: theme.palette.common.black,
      color: theme.palette.common.white,
      fontSize: 22,
    },
    [`&.${tableCellClasses.body}`]: {
      fontSize: 16,
    },
  }));

  const StyledTableRow = styled(TableRow)(({ theme }) => ({
    "&:nth-of-type(odd)": {
      backgroundColor: colors.grey[100],
    },
    "&:nth-of-type(even)": {
      backgroundColor: colors.grey[300],
    },
    // hide last border
    "&:last-child td, &:last-child th": {
      border: 0,
    },
  }));

  const token = sessionStorage.getItem("token");

  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);

  const opts = {
    method: "POST",
    headers: {
      "Content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({
      page: 0,
      size: 1000,
    }),
  };

  function dataParser(data, i) {
    let picked = {};
    picked.i = i;
    picked.id = data.username;
    picked.username = data.username;
    picked.firstName = data.firstName;
    picked.lastName = data.lastName;
    picked.rating = Math.round(data.profile.rating * 100) / 100;
    return picked;
  }

  const columns = [
    { field: "i", headerName: "ID", flex: 1 },
    { field: "username", headerName: "Username", flex: 8 },
    { field: "firstName", headerName: "First name", flex: 8 },
    { field: "lastName", headerName: "Last name", flex: 8 },
  ];

  function download_table_as_csv() {
    const csvString = [
      ["Username", "First Name", "Last Name", "Rating"],
      ...rows.map((item) => [
        item.username,
        item.firstName,
        item.lastName,
        item.rating,
      ]),
    ]
      .map((e) => e.join(","))
      .join("\n");

    const element = document.createElement("a");
    const file = new Blob(["\ufeff", csvString], {
      type: "text/csv",
    });
    element.href = URL.createObjectURL(file);
    element.download = "userData.csv";
    document.body.appendChild(element);
    element.click();
    document.body.removeChild(element);
  }

  const setTableHeight = (i) => {
    document.getElementById("requestUsersTable").style.height = `${
      (i === 0 ? 400 : 92) + 36 * Math.min(i, pageSize)
    }px`;
    document.querySelector(
      "#root > div.announcementsContainer > div.tabWrapper > div > div.content-tabs > div.content.active-content > div > div.MuiDataGrid-root.css-1xy1myn-MuiDataGrid-root > div:nth-child(3) > div > div.MuiTablePagination-root.css-rtrcn9-MuiTablePagination-root > div > p"
    ).style.width = "110px";
  };

  useEffect(() => {
    fetch("http://localhost:8080/api/requestinfo/all", opts)
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        let demoRows = [];
        let i = 1;
        for (const element of data.content) {
          demoRows.push(dataParser(element, i));
          i++;
        }
        setRows(demoRows);
        setLoading(false);
        setTableHeight(data.content.length);
        return data;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  }, [pingUserList]);

  return (
    <div id="requestUsersTable">
      <div className="outerDiv">
        <DownloadForOfflineRoundedIcon
          onClick={download_table_as_csv}
          className="downloadButton"
          style={{ fontSize: 50, margin: 5, marginTop: -20 }}
          id="downloadIcon"
        />
        <h2 style={{ marginTop: -40 }}>List of users:</h2>
        <hr id="easeBar" className="loadBar"></hr>
      </div>

      <DataGrid
        rows={rows}
        columns={columns}
        pageSize={pageSize}
        rowsPerPageOptions={[pageSize]}
        density="compact"
      />
    </div>
  );
};

export default RequestTab;
