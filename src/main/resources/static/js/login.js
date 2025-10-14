$(document).ready(function () {
        $('#loginForm').on('submit', function (e) {
            const usernameEl = $('#username');
            const passwordEl = $('#password');
            const username = usernameEl.val().trim();
            const password = passwordEl.val().trim();

            const usernameRegex = /^[a-zA-Z0-9]{4,8}$/;
            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=,./;'[\]<>:"{}]).{8,}$/;

            let isValid = true;
            const popoverOptions = {
                trigger: 'manual',
                placement: 'right'
            };

            function showPopover(el, msg) {
                el.addClass('is-invalid');
                el.attr('data-bs-content', msg);
                let pop = bootstrap.Popover.getInstance(el[0]);
                if (!pop) {
                    pop = new bootstrap.Popover(el[0], popoverOptions);
                }
                pop.setContent({ '.popover-body': msg });
                pop.show();
            }



            function clearPopover(el) {
                el.removeClass('is-invalid');
                let pop = bootstrap.Popover.getInstance(el[0]);
                if (pop) pop.hide();
            }

            clearPopover(usernameEl);
            clearPopover(passwordEl);
            console.log(username);
            console.log(password);
            if (!usernameRegex.test(username)) {
                showPopover(usernameEl, "Tên đăng nhập phải từ 4-8 ký tự, chỉ gồm chữ cái và số.");
                isValid = false;
            }

            if (!passwordRegex.test(password)) {
                showPopover(passwordEl, "Mật khẩu phải từ 8 ký tự, có chữ hoa, chữ thường và ký tự đặc biệt.");
                isValid = false;
            }

            if (!isValid) {
                e.preventDefault();
                if (usernameEl.hasClass('is-invalid')) {
                    usernameEl.focus();
                } else {
                    passwordEl.focus();
                }
            }
        });
    });

    function showPassword() {
        const input = document.getElementById('password');
        console.log("show password", input)
        input.type = 'text';
        $("#hide-password").css({"display": "block"});
        $("#show-password").css({"display": "none"});
    }



    function hidePassword() {
        const input = document.getElementById('password');
        console.log("hide password", input)
        input.type = 'password';
        $("#show-password").css({"display": "block"});
        $("#hide-password").css({"display": "none"});
    }