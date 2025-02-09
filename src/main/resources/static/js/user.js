document.addEventListener('DOMContentLoaded', function() {
    // 登录表单处理
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            event.preventDefault(); // 阻止默认提交
            const formData = new FormData(loginForm);

            fetch(loginForm.action, {
                method: loginForm.method,
                body: new URLSearchParams(formData)
            }).then(response => {
                if (response.status === 401) {
                    document.getElementById('error-message').style.display = 'block';
                } else if (response.status === 200) {
                    window.location.href = '/'; // 登录成功后重定向至首页
                }
            });
        });
    }

    // 注册表单处理
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', function(event) {
            event.preventDefault(); // 阻止默认提交
            const formData = new FormData(registerForm);

            fetch(registerForm.action, {
                method: registerForm.method,
                body: new URLSearchParams(formData)
            }).then(response => {
                if (response.status === 409) {
                    document.getElementById('error-message').style.display = 'block';
                } else if (response.status === 302 || response.status === 200) {
                    window.location.href = '/';
                }
            });
        });
    }
});
