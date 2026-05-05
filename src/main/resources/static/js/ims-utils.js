
function togglePassword() {
    const el = document.getElementById("password");

    if (el.type === "password") {
        el.type = "text";
    } else {
        el.type = "password";
    }
}