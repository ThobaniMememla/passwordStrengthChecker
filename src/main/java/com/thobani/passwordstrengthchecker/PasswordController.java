package main.java.com.thobani.passwordstrengthchecker;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/checkPassword")
    public String checkPassword(@RequestParam("password") String password, Model model) {
        boolean isStrong = isStrongPassword(password);
        model.addAttribute("password", password);
        model.addAttribute("isStrong", isStrong);
        return "result";
    }

    public static boolean isStrongPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                hasSpecialChar = !Character.isLetterOrDigit(c);
            }
        }

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }
}
