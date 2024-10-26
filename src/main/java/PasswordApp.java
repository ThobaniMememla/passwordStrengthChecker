import io.javalin.Javalin;
import io.javalin.http.Context;

public class PasswordApp {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            // config.staticFiles.add("/public");  // Optional: serve static files if needed
        }).start(7000);

        app.get("/", PasswordApp::renderForm);
        app.post("/checkPassword", PasswordApp::checkPassword);
    }

    private static void renderForm(Context ctx) {
        String htmlForm = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Password Strength Checker</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f0f8ff;
                            color: #333;
                            text-align: center;
                        }
                        h2 {
                            color: #2e8b57;
                        }
                        form {
                            margin-top: 20px;
                            background-color: #fff;
                            padding: 20px;
                            border-radius: 8px;
                            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
                        }
                        label {
                            display: block;
                            margin-bottom: 10px;
                            font-weight: bold;
                        }
                        input[type=password], input[type=text] {
                            width: 200px;
                            padding: 10px;
                            margin-bottom: 10px;
                            border: 1px solid #ccc;
                            border-radius: 4px;
                            transition: border-color 0.3s;
                        }
                        input[type=password]:focus, input[type=text]:focus {
                            border-color: #2e8b57;
                        }
                        button {
                            padding: 10px 15px;
                            margin: 5px;
                            border: none;
                            border-radius: 4px;
                            background-color: #2e8b57;
                            color: #fff;
                            cursor: pointer;
                            transition: background-color 0.3s;
                        }
                        button:hover {
                            background-color: #2a7a4b;
                        }
                        .strength {
                            margin-top: 10px;
                            font-weight: bold;
                            font-size: 1.2em;
                        }
                    </style>
                </head>
                <body>
                    <h2>Password Strength Checker</h2>
                    <form action="/checkPassword" method="post">
                        <label for="password">Enter Password:</label>
                        <input type="password" id="password" name="password" oninput="checkStrength()" required>
                        <button type="button" onclick="generatePassword()">Generate Password</button>
                        <button type="submit">Check Password Strength</button>
                        <div id="strength" class="strength"></div>
                    </form>
                    <script>
                        function checkStrength() {
                            let strength = document.getElementById("strength");
                            let password = document.getElementById("password").value;
                            if (password.length < 6) {
                                strength.textContent = "Weak";
                                strength.style.color = "red";
                            } else if (password.match(/[A-Z]/) && password.match(/[0-9]/) && password.match(/[\\W_]/)) {
                                strength.textContent = "Strong";
                                strength.style.color = "green";
                            } else {
                                strength.textContent = "Moderate";
                                strength.style.color = "orange";
                            }
                        }
                        function generatePassword() {
                            let charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()";
                            let password = "";
                            for (let i = 0; i < 12; i++) {
                                password += charset.charAt(Math.floor(Math.random() * charset.length));
                            }
                            let passwordField = document.getElementById("password");
                            passwordField.value = password;
                            passwordField.type = "text"; // Change the input type to text to show the password
                            checkStrength();
                        }
                    </script>
                </body>
                </html>
                """;
        ctx.html(htmlForm);
    }

    private static void checkPassword(Context ctx) {
        String password = ctx.formParam("password");
        String strength = getPasswordStrength(password);

        String resultPage = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Password Strength Result</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f0f8ff;
                        color: #333;
                        text-align: center;
                    }
                    h2 {
                        color: #2e8b57;
                    }
                </style>
            </head>
            <body>
                <h2>Password Strength Result</h2>
                Password Strength: """ + strength + """
                <br>
                <a href="/">Go back</a>
            </body>
            </html>
            """;
        ctx.html(resultPage);
    }

    private static String getPasswordStrength(String password) {
        if (password.length() < 6) {
            return "Weak";
        } else if (password.matches(".*[A-Z].*") && password.matches(".*[0-9].*") && password.matches(".*[\\W_].*")) {
            return "Strong";
        } else {
            return "Moderate";
        }
    }
}
