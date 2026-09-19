
// Get the currently logged-in user
function getLoggedInUser() {

    const userData = localStorage.getItem("loggedInUser");

    if (!userData) {
        window.location.href = "login.html";
        return null;
    }

    return JSON.parse(userData);
}


// Get the logged-in user's ID
function getUserId() {

    const user = getLoggedInUser();

    return user ? user.id : null;
}


// Headers for Expense API requests
function userHeaders() {

    const userId = getUserId();

    return {
        "Content-Type": "application/json",
        "X-User-Id": userId
    };
}


// Logout
function logout() {

    localStorage.removeItem("loggedInUser");

    window.location.href = "login.html";
}

