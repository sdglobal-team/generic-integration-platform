document.addEventListener("DOMContentLoaded", () => {
  loadStats();
  loadOutboundData();
  loadProcessedFiles();

  const uploadForm = document.getElementById("uploadForm");
  uploadForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files;
    if (!file.length) return;

    const formData = new FormData();
    for (let i = 0; i < file.length; i++) {
      formData.append("file", file[i]);
    }

    try {
      await axios.post("/api/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" }
      });
      showAlert("All files uploaded successfully!");
      fileInput.value = "";
      refreshData();
    } catch (error) {
      showAlert("Upload failed: " + error.message, "danger");
    }
  });

  document.getElementById("refreshButton").addEventListener("click", () => {
    refreshData();
  });
});

function refreshData() {
  loadStats();
  loadOutboundData();
  loadProcessedFiles();
}

function showAlert(message, type = "success", timeout = 3000) {
  const alertPlaceholder = document.getElementById("alertPlaceholder");
  const alertId = "alert-" + Date.now(); // unique ID
  alertPlaceholder.innerHTML = `
    <div id="${alertId}" class="alert alert-${type} alert-dismissible fade show" role="alert">
      ${message}
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
  `;

  // Automatically remove after `timeout` ms
  setTimeout(() => {
    const alertElem = document.getElementById(alertId);
    if (alertElem) {
      alertElem.classList.remove("show");
      alertElem.classList.add("hide");
    }
  }, timeout);
}


async function loadStats() {
  try {
    const res = await axios.get("/api/stats");
    document.getElementById("processedFilesCount").innerText = res.data.processedFiles;
    document.getElementById("pendingCount").innerText = res.data.pending;
    document.getElementById("failedCount").innerText = res.data.failed;
    document.getElementById("processedRecordsCount").innerText = res.data.processedRecords;

  } catch (e) {
    console.error("Error loading stats", e);
  }
}

async function loadOutboundData() {
  try {
    const res = await axios.get("/api/outbound");
    const tbody = document.querySelector("#outboundTable tbody");
    tbody.innerHTML = "";
    res.data.forEach(record => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${record.fileId}</td>
        <td>${record.fileName}</td>
        <td>${record.fileType}</td>
        <td>${record.fileSize}</td>
        <td>${record.createdBy}</td>
        <td>${record.createdDate}</td>
      `;
      tbody.appendChild(tr);
    });
  } catch (e) {
    console.error("Error loading outbound", e);
  }
}


async function loadProcessedFiles() {
  try {
    const res = await axios.get("/api/processed-files");
    const tbody = document.querySelector("#processedTable tbody");
    tbody.innerHTML = "";
    res.data.forEach(record => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${record.fileId}</td>
        <td>${record.fileName}</td>
        <td>${record.fileType}</td>
        <td>${record.fileSize}</td>
        <td>${record.createdBy}</td>
        <td>${record.createdDate}</td>
      `;
      tbody.appendChild(tr);
    });
  } catch (e) {
    console.error("Error loading processed files", e);
  }
}
