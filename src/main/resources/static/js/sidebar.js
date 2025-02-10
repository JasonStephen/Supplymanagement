document.addEventListener("DOMContentLoaded", function () {
    const sidebar = document.querySelector('.sidebar');
    const content = document.querySelector('.content');
    const overlay = document.querySelector('.overlay');

    // 初始化宽度
    if (window.innerWidth > 750) {
        content.style.width = sidebar.classList.contains('active') ? 'calc(100% - 256px)' : '100%';
    } else {
        content.style.width = '100%';
    }
});

function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const content = document.querySelector('.content');
    const overlay = document.querySelector('.overlay');

    sidebar.classList.toggle('active');
    overlay.classList.toggle('active');

    // 调整内容区域宽度
    if (window.innerWidth > 750) {
        content.style.width = sidebar.classList.contains('active') ? 'calc(100% - 256px)' : '100%';
    } else {
        content.style.width = '100%';
    }
}

function closeSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const overlay = document.querySelector('.overlay');

    sidebar.classList.remove('active');
    overlay.classList.remove('active');

    // 调整内容区域宽度
    const content = document.querySelector('.content');
    if (window.innerWidth > 750) {
        content.style.width = 'calc(100% - 256px)';
    } else {
        content.style.width = '100%';
    }
}

window.addEventListener('resize', () => {
    const sidebar = document.querySelector('.sidebar');
    const content = document.querySelector('.content');

    if (window.innerWidth > 750) {
        content.style.width = sidebar.classList.contains('active') ? 'calc(100% - 256px)' : '100%';
    } else {
        content.style.width = '100%';
    }
});
