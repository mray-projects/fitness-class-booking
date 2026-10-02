
document.addEventListener("DOMContentLoaded", function () {

    // registration

    const registrationForm = document.getElementById("registrationForm");

    if (registrationForm) {

        registrationForm.addEventListener("submit", async function (event) {

            event.preventDefault();

            const user = {
                first_name: document.getElementById("firstName").value,
                last_name: document.getElementById("lastName").value,
                email: document.getElementById("email").value,
                username: document.getElementById("username").value,
                password: document.getElementById("password").value
            };

            try {

                const response = await fetch("http://localhost:8080/users/register", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(user)
                });

                const result = await response.text();

                if (response.ok) {
                    alert("Registration successful!");
                    window.location.href = "main.html";
                } else {
                    alert("Registration failed: " + result);
                }

            } catch (error) {
                alert("Could not connect to the server.");
                console.error(error);
            }
        });
    }


    // login

    const loginForm = document.getElementById("loginForm");

    if (loginForm) {

        loginForm.addEventListener("submit", async function (event) {

            event.preventDefault();

            const loginData = {
                username: document.getElementById("loginUsername").value,
                password: document.getElementById("loginPassword").value
            };

            try {

                const response = await fetch("http://localhost:8080/users/login", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(loginData)
                });

                const result = await response.text();

                if (response.ok) {
                    alert("Login successful!");
                } else {
                    alert("Login failed: " + result);
                }

            } catch (error) {
                alert("Could not connect to the server.");
                console.error(error);
            }
        });
    }

});

