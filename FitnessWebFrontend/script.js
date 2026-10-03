document.addEventListener("DOMContentLoaded", async function () {

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

                const result = await response.json();

                if (response.ok) {

                    // Save the logged-in user's ID
                    localStorage.setItem("user_id", result.user_id);

                    alert("Login successful!");
                    window.location.href = "classes.html";

                } else {

                    alert("Login failed: " + result);
                }

            } catch (error) {
                alert("Could not connect to the server.");
                console.error(error);
            }
        });
    }


    // view classes

    const classesContainer = document.getElementById("classesContainer");

    if (classesContainer) {

        try {

            const response = await fetch("http://localhost:8080/classes");

            const classes = await response.json();

            classesContainer.innerHTML = "";

            classes.forEach(function (fitnessClass) {

                const classDiv = document.createElement("div");

                classDiv.innerHTML = `
                    <h3>${fitnessClass.name}</h3>
                    <p>Date: ${fitnessClass.class_date}</p>
                    <p>Time: ${fitnessClass.class_time}</p>
                    <p>Capacity: ${fitnessClass.capacity}</p>
                    <p>Location: ${fitnessClass.location || "Not specified"}</p>

                    <button onclick="bookClass(${fitnessClass.class_id})">
                        Book
                    </button>

                    <hr>
                `;

                classesContainer.appendChild(classDiv);
            });

        } catch (error) {

            classesContainer.innerHTML =
                "Could not load classes.";

            console.error(error);
        }
    }

});


// Book a fitness class
async function bookClass(classId) {

    // Get the logged-in user's ID
    const userId = localStorage.getItem("user_id");

    if (!userId) {
        alert("Please log in first.");
        return;
    }

    // Create the booking data
    const bookingData = {
        user_id: parseInt(userId),
        class_id: classId
    };

    try {

        const response = await fetch("http://localhost:8080/bookings", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(bookingData)
        });

        const result = await response.text();

        if (response.ok) {

            alert("Class booked successfully!");

        } else {

            alert("Booking failed: " + result);
        }

    } catch (error) {

        alert("Could not connect to the server.");
        console.error(error);
    }
}