document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");

    form.addEventListener("submit", function (event) {
        let isValid = true;
        clearErrors();

        const firstName = document.querySelector("input[name='firstName']");
        const lastName = document.querySelector("input[name='lastName']");
        const username = document.querySelector("input[name='username']");
        const password = document.querySelector("input[name='password']");
        const address = document.querySelector("input[name='address']");
        const email = document.querySelector("input[name='email']");

        // if (!/^[A-Z][a-zA-Z]{1,19}$/.test(firstName.value)) {
        //     showError(firstName, "Имя должно начинаться с заглавной буквы и содержать только буквы.");
        //     isValid = false;
        // }
        //
        // if (!/^[A-Z][a-zA-Z]{1,19}$/.test(lastName.value)) {
        //     showError(lastName, "Фамилия должна начинаться с заглавной буквы и содержать только буквы.");
        //     isValid = false;
        // }
        //
        // if (!/^[a-zA-Z0-9_]{5,20}$/.test(username.value)) {
        //     showError(username, "Логин должен содержать от 5 до 20 символов: буквы, цифры, _.");
        //     isValid = false;
        // }
        //
        // if (!/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/.test(password.value)) {
        //     showError(password, "Пароль должен содержать минимум 8 символов, хотя бы одну букву, цифру и спецсимвол.");
        //     isValid = false;
        // }
        //
        // if (address.value.trim() === "") {
        //     showError(address, "Адрес не может быть пустым.");
        //     isValid = false;
        // }
        //
        // // Изменено на проверку email
        // if (!/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$/.test(email.value)) {
        //     showError(email, "Email должен быть в формате example@example.com.");
        //     isValid = false;
        // }
        //
        // if (!isValid) {
        //     event.preventDefault();
        // }
    });

    function showError(input, message) {
        const error = document.createElement("p");
        error.classList.add("error");
        error.textContent = message;
        input.after(error);
    }

    function clearErrors() {
        document.querySelectorAll(".error").forEach(error => error.remove());
    }
});