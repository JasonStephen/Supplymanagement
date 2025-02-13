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
document.getElementById('logoutForm')?.addEventListener('submit', function (event) {
    event.preventDefault();
    fetch(this.action, {
        method: this.method,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    }).then(response => {
        if (response.status === 204) {
            window.location.reload();
        }
    });
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
