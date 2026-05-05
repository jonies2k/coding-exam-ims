const baseUrl = "http://localhost:8080/ims";
let deleteModalResolver = null;

function initPage() {
    const subscriberForm = document.getElementById("subscriberForm");
    const showPassword = document.getElementById("showPassword");
    const passwordInput = document.getElementById("password");

    const confirmDeleteBtn = document.getElementById("confirmDeleteBtn");
    const cancelDeleteBtn = document.getElementById("cancelDeleteBtn");
    const modalOverlay = document.querySelector("#deleteConfirmModal .custom-modal-overlay");

    if (subscriberForm) {
        subscriberForm.addEventListener("submit", function(event) {
            event.preventDefault();
            saveSubscriber();
        });
    }

    if (showPassword && passwordInput) {
        showPassword.addEventListener("change", function () {
            togglePassword();
        });
    }

    if (confirmDeleteBtn) {
        confirmDeleteBtn.addEventListener("click", function() {
            closeDeleteModal(true);
        });
    }

    if (cancelDeleteBtn) {
        cancelDeleteBtn.addEventListener("click", function() {
            closeDeleteModal(false);
        });
    }

    if (modalOverlay) {
        modalOverlay.addEventListener("click", function() {
            closeDeleteModal(false);
        });
    }

}

function initSubscriberPayload() {
    return {
        phoneNumber: document.getElementById("phoneNumber").value.trim(),
        username: document.getElementById("username").value.trim(),
        password: document.getElementById("password").value.trim(),
        domain: document.getElementById("domain").value.trim(),
        status: document.getElementById("status").value,
        features: {
            callForwardNoReply: {
                provisioned: document.getElementById("callForwardNoReply").checked,
                destination: document.getElementById("destination").value.trim()
            }
        }
    };
}

function saveSubscriber() {
    const subscriber = initSubscriberPayload();

    if (!subscriber.phoneNumber) {
        showFormMessage("Phone number is required.", "error");
        return;
    }

    fetch(baseUrl + "/subscriber/" + encodeURIComponent(subscriber.phoneNumber), {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(subscriber)
    })
        .then(function(response) {
            if (!response.ok) {
                throw new Error("Unable to save subscriber. HTTP Status: " + response.status);
            }
            return response.json();
        })
        .then(function(data) {
            showFormMessage("Subscriber saved successfully.", "success");
            showResult(data);
        })
        .catch(function(error) {
            showFormMessage(error.message, "error");
        });
}

function getSubscriber() {
    const phoneNumber = document.getElementById("searchPhoneNumber").value.trim();

    if (!phoneNumber) {
        showActionMessage("Please enter a phone number to search.", "error");
        return;
    }

    fetch(baseUrl + "/subscriber/" + encodeURIComponent(phoneNumber))
        .then(function(response) {
            if (!response.ok) {
                throw new Error("Subscriber not found. HTTP Status: " + response.status);
            }
            return response.json();
        })
        .then(function(data) {
            showActionMessage("Subscriber details retrieved successfully.", "success");
            showResult(data);
            populateForm(data);
        })
        .catch(function(error) {
            showActionMessage(error.message, "error");
            document.getElementById("result").textContent = "No subscriber record found.";
        });
}

async function deleteSubscriber() {
    const phoneNumber = document.getElementById("searchPhoneNumber").value.trim();

    if (!phoneNumber) {
        showActionMessage("Please enter a phone number to delete.", "error");
        return;
    }

    const confirmed = await showDeleteModal(phoneNumber);

    if (!confirmed) {
        return;
    }

    fetch(baseUrl + "/subscriber/" + encodeURIComponent(phoneNumber), {
        method: "DELETE"
    })
        .then(function(response) {
            if (!response.ok) {
                throw new Error("Unable to delete subscriber. HTTP Status: " + response.status);
            }

            return response.text();
        })
        .then(function(message) {
            showActionMessage("Subscriber deleted successfully.", "success");
            document.getElementById("result").textContent = message || "Subscriber deleted.";
            clearForm();
        })
        .catch(function(error) {
            showActionMessage(error.message, "error");
        });
}

function populateForm(data) {
    document.getElementById("phoneNumber").value = data.phoneNumber || "";
    document.getElementById("username").value = data.username || "";
    document.getElementById("password").value = data.password || "";
    document.getElementById("domain").value = data.domain || "";
    document.getElementById("status").value = data.status || "ACTIVE";

    document.getElementById("callForwardNoReply").checked = false;
    document.getElementById("destination").value = "";

    if (data.features && data.features.callForwardNoReply) {
        document.getElementById("callForwardNoReply").checked =
            data.features.callForwardNoReply.provisioned === true;

        document.getElementById("destination").value =
            data.features.callForwardNoReply.destination || "";
    }
}

function showResult(data) {
    document.getElementById("result").textContent = JSON.stringify(data, null, 2);
}

function clearForm() {
    document.getElementById("subscriberForm").reset();
    document.getElementById("formMessage").className = "status-message";
    document.getElementById("formMessage").textContent = "";
    const passwordInput = document.getElementById("password");
    const showPassword = document.getElementById("showPassword");

    if (passwordInput) {
        passwordInput.type = "password";
    }

    if (showPassword) {
        showPassword.checked = false;
    }
}

function showFormMessage(message, type) {
    const messageBox = document.getElementById("formMessage");
    messageBox.textContent = message;
    messageBox.className = "status-message " + type;
}

function showActionMessage(message, type) {
    const messageBox = document.getElementById("actionMessage");
    messageBox.textContent = message;
    messageBox.className = "status-message " + type;
}

function showDeleteModal(phoneNumber) {
    const modal = document.getElementById("deleteConfirmModal");
    const message = document.getElementById("deleteModalMessage");

    if (!modal) {
        return Promise.resolve(false);
    }

    if (message) {
        message.innerHTML =
            'Are you sure you want to delete subscriber <strong>' +
            phoneNumber +
            '</strong>?<br>This action cannot be undone.';
    }

    modal.classList.remove("hidden");

    return new Promise(function(resolve) {
        deleteModalResolver = resolve;
    });
}

function closeDeleteModal(result) {
    const modal = document.getElementById("deleteConfirmModal");

    if (modal) {
        modal.classList.add("hidden");
    }

    if (deleteModalResolver) {
        deleteModalResolver(result);
        deleteModalResolver = null;
    }
}