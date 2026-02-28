export default function Login() {
    const passwordInput = document.getElementById('password') as HTMLInputElement | null;
    const showIcon = document.getElementById('show-password');
    const hideIcon = document.getElementById('hide-password');


    function showPassword(): void {
        if (!passwordInput || !showIcon || !hideIcon) return;
        passwordInput.type = 'text';
        hideIcon.style.display = 'block';
        showIcon.style.display = 'none';
    }

    function hidePassword(): void {
        if (!passwordInput || !showIcon || !hideIcon) return;
        passwordInput.type = 'password';
        showIcon.style.display = 'block';
        hideIcon.style.display = 'none';
    }

    return (
        <>
            <div className="login-body">
                <div className="login-box d-flex align-items-center justify-content-center">
                    <div className="wrapper w-100">
                        <div className="text-center mb-4">
                            <svg className="mb-2" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64">
                                <path fill="#6446fe"
                                    d="M59,8H5A1,1,0,0,0,4,9V55a1,1,0,0,0,1,1H59a1,1,0,0,0,1-1V9A1,1,0,0,0,59,8ZM58,54H6V10H58Z"
                                    className="color1d1f47 svgShape"></path>
                                <path fill="#6446fe"
                                    d="M36,35H28a3,3,0,0,1-3-3V27a3,3,0,0,1,3-3h8a3,3,0,0,1,3,3v5A3,3,0,0,1,36,35Zm-8-9a1,1,0,0,0-1,1v5a1,1,0,0,0,1,1h8a1,1,0,0,0,1-1V27a1,1,0,0,0-1-1Z"
                                    className="color0055ff svgShape"></path>
                                <path fill="#6446fe"
                                    d="M36 26H28a1 1 0 0 1-1-1V24a5 5 0 0 1 10 0v1A1 1 0 0 1 36 26zm-7-2h6a3 3 0 0 0-6 0zM32 31a1 1 0 0 1-1-1V29a1 1 0 0 1 2 0v1A1 1 0 0 1 32 31z"
                                    className="color0055ff svgShape"></path>
                                <path fill="#6446fe"
                                    d="M59 8H5A1 1 0 0 0 4 9v8a1 1 0 0 0 1 1H20.08a1 1 0 0 0 .63-.22L25.36 14H59a1 1 0 0 0 1-1V9A1 1 0 0 0 59 8zm-1 4H25l-.21 0a1.09 1.09 0 0 0-.42.2L19.73 16H6V10H58zM50 49H14a1 1 0 0 1-1-1V39a1 1 0 0 1 1-1H50a1 1 0 0 1 1 1v9A1 1 0 0 1 50 49zM15 47H49V40H15z"
                                    className="color1d1f47 svgShape"></path>
                                <circle cx="19.5" cy="43.5" r="1.5" fill="#6446fe" className="color0055ff svgShape"></circle>
                                <circle cx="24.5" cy="43.5" r="1.5" fill="#6446fe" className="color0055ff svgShape"></circle>
                                <circle cx="29.5" cy="43.5" r="1.5" fill="#6446fe" className="color0055ff svgShape"></circle>
                                <circle cx="34.5" cy="43.5" r="1.5" fill="#6446fe" className="color0055ff svgShape"></circle>
                                <circle cx="39.5" cy="43.5" r="1.5" fill="#6446fe" className="color0055ff svgShape"></circle>
                                <circle cx="44.5" cy="43.5" r="1.5" fill="#6446fe" className="color0055ff svgShape"></circle>
                                <path fill="#6446fe"
                                    d="M60 9a1 1 0 0 0-1-1H28.81l2.37-2.37A19.22 19.22 0 0 1 60 31zM35.19 56l-2.37 2.37A19.22 19.22 0 0 1 4 33V55a1 1 0 0 0 1 1z"
                                    opacity=".3" className="color0055ff svgShape"></path>
                            </svg>
                            <div className="logo mb-2">Thiên An Phú</div>
                            <div className="subtitle mb-2">Chào mừng quay trở lại</div>
                        </div>

                        {/* <div th:if="${param.logout}" className="alert alert-success">Đăng xuất thành công.</div>
                    <div th:if="${param.timeout}" className="alert alert-warning">Phiên đăng nhập đã hết hạn.</div>
                    <div th:if="${param.expired}" className="alert alert-warning">Tài khoản đã đăng nhập ở nơi khác.</div>
                    <div th:if="${session.loginError}" className="alert alert-danger" th:text="${session.loginError}"></div> */}


                        <form action="/process-login" method="post" id="loginForm">
                            <div className="mb-4">
                                <input type="text" className="" id="username" name="username" placeholder="Tên đăng nhập"
                                    data-bs-toggle="popover" data-bs-trigger="manual" data-bs-placement="right" />
                            </div>

                            <div className="mb-4 position-relative">
                                <div className="position-relative">
                                    <input type="password" className="" id="password" name="password" placeholder="Mật khẩu"
                                        data-bs-toggle="popover" data-bs-trigger="manual" data-bs-placement="right" />
                                    <i id="show-password" className="bi bi-eye" onClick={showPassword}></i>
                                    <i id="hide-password" className="bi bi-eye-slash" onClick={hidePassword}></i>
                                </div>
                            </div>

                            <div className="d-flex flex-row mb-4 justify-content-between">
                                <div className="remember-me">
                                    <input type="checkbox" name="" id="" className="me-2" />
                                    <span>Ghi nhớ</span>
                                </div>
                                <div className="forgot-password">
                                    <a href="">Quên mật khẩu?</a>
                                </div>
                            </div>

                            <button type="submit" className="btn btn-primary w-100">Đăng nhập</button>
                        </form>
                    </div>

                    <div className="d-flex flex-row justify-content-center documentation position-absolute bottom-0 mb-3">
                        <a href="/term-of-service" className="text-muted me-2"><span className="">Điều khoản dịch vụ</span></a>
                        <a href="/privacy-policy" className="text-muted"><span className="">Quyền riêng tư</span></a>
                    </div>
                </div>
            </div>
        </>
    )
}