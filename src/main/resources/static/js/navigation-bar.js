

function toggleDropdown() {
    const dropdownMenu = document.getElementById('dropdownMenu');
    const isVisible = dropdownMenu.style.display === 'block';

    // 添加 0.25 秒渐变效果
    dropdownMenu.style.transition = 'opacity 0.25s ease';
    dropdownMenu.style.opacity = isVisible ? '0' : '1';

    // 使用 setTimeout 延迟移除 display
    if (isVisible) {
        setTimeout(() => {
            dropdownMenu.style.display = 'none';
        }, 250); // 0.25 秒后隐藏 dropdown-menu
    } else {
        // 第一步：显示元素，但不透明
        dropdownMenu.style.transition = 'opacity 0.25s ease'; // 设置渐变
        dropdownMenu.style.opacity = '0'; // 初始透明度为 0
        dropdownMenu.style.display = 'block'; // 显示元素

        // 第二步：延迟设置 opacity 为 1，触发渐变
        setTimeout(() => {
            dropdownMenu.style.opacity = '1'; // 渐入到完全不透明
        }, 10); // 延迟 10ms，确保浏览器渲染
    }

    // 关闭 mobile-menu
    const mobileMenu = document.getElementById('mobileMenu');
    mobileMenu.classList.remove('active');
}

function toggleMobileMenu() {
    const mobileMenu = document.getElementById('mobileMenu');
    const isVisible = mobileMenu.classList.contains('active');

    // 添加 0.25 秒渐变效果
    mobileMenu.style.transition = 'opacity 0.25s ease';
    mobileMenu.style.opacity = isVisible ? '0' : '1';

    // 使用 setTimeout 延迟移除 display
    if (isVisible) {
        setTimeout(() => {
            mobileMenu.classList.remove('active');
        }, 250); // 0.25 秒后移除 active 类
    } else {
        // 第一步：显示元素，但不透明
        mobileMenu.style.transition = 'opacity 0.25s ease'; // 设置渐变
        mobileMenu.style.opacity = '0'; // 初始透明度为 0
        mobileMenu.classList.add('active'); // 显示元素

        // 第二步：延迟设置 opacity 为 1，触发渐变
        setTimeout(() => {
            mobileMenu.style.opacity = '1'; // 渐入到完全不透明
        }, 10); // 延迟 10ms，确保浏览器渲染
    }

    // 关闭 dropdown-menu
    const dropdownMenu = document.getElementById('dropdownMenu');
    dropdownMenu.style.display = 'none';
}

// Close dropdown when clicking outside
window.addEventListener('click', function (event) {
    const dropdownMenu = document.getElementById('dropdownMenu');
    const userInfo = document.querySelector('.user-info');

    if (!userInfo.contains(event.target)) {
        dropdownMenu.style.transition = 'opacity 0.25s ease';
        dropdownMenu.style.opacity = '0';
        setTimeout(() => {
            dropdownMenu.style.display = 'none';
        }, 250); // 0.25 秒后隐藏 dropdown-menu
    }
});

// Logout Form Handler
document.addEventListener('DOMContentLoaded', function () {
    const logoutForm = document.getElementById('logoutForm');
    if (logoutForm) {
        console.log('Logout form found'); // 调试日志
        logoutForm.addEventListener('submit', function (event) {
            event.preventDefault();
            console.log('Logout form submitted'); // 调试日志
            fetch(this.action, {
                method: this.method,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(response => {
                console.log('Logout response received:', response.status); // 调试日志
                if (response.status === 204) {
                    console.log('Reloading page...'); // 调试日志
                    window.location.reload(true);
                } else {
                    console.warn('Logout failed with status:', response.status); // 调试日志
                }
            }).catch(error => {
                console.error('Logout request failed:', error); // 调试日志
            });
        });
    } else {
        console.warn('Logout form not found'); // 调试日志
    }
});


// Hide mobile menu when window width is greater than 800px
function handleResize() {
    const mobileMenu = document.getElementById('mobileMenu');
    if (window.innerWidth > 800) {
        mobileMenu.classList.remove('active'); // 隐藏 mobile-menu
    }
}

// Add resize event listener
window.addEventListener('resize', handleResize);

// Initial check on page load
handleResize();


// 打开 AI 模态窗口
function openAIModal() {
    document.getElementById('aiModal').style.display = 'block';
}

// 关闭 AI 模态窗口
function closeAIModal() {
    document.getElementById('aiModal').style.display = 'none';
}

// 提交 AI 问题
function submitAIQuestion() {
    const input = document.getElementById('aiInput').value.trim(); // 获取输入内容并去除首尾空格
    const output = document.getElementById('aiOutput');

    // 如果输入内容为空，显示提示并返回
    if (!input) {
        output.innerHTML = '请输入问题内容！'; // 显示提示消息
        return; // 阻止继续执行
    }

    // 清空输出框
    output.innerHTML = '加载中...';

    // 调用 AIDemandController 的接口
    fetch('/ai/ask', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `content=${encodeURIComponent(input)}`, // 将内容编码为表单格式
    })
        .then(response => response.text()) // 注意这里是 text()，因为接口返回的是字符串
        .then(data => {
            output.innerHTML = data; // 直接将返回的字符串显示在输出框中
        })
        .catch(error => {
            output.innerHTML = '请求失败，请重试。';
            console.error('Error:', error);
        });
}

// 点击模态窗口外部时关闭窗口
window.onclick = function(event) {
    const modal = document.getElementById('aiModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
};

