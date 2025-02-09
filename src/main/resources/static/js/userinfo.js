function openEditModal() {
    document.getElementById('editModal').style.display = 'block';
}

function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
}

function openPasswordChange() {
    document.getElementById('passwordChange').style.display = 'block';
}

function openAvatarUpload() {
    document.getElementById('avatarUpload').style.display = 'block';
}

function changePassword() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (password !== confirmPassword) {
        alert('密码不匹配');
        return;
    }

    fetch('/api/v1/user/updatePassword', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `password=${password}`
    }).then(response => {
        if (response.ok) {
            alert('密码更新成功');
            closeEditModal();
            location.reload(); // 刷新页面以应用新密码
        } else {
            alert('密码更新失败');
        }
    }).catch(error => {
        console.error('密码更新错误:', error);
        alert('密码更新失败');
    });
}

function uploadAvatar() {
    const fileInput = document.getElementById('avatar');
    const file = fileInput.files[0];

    if (!file) {
        alert('请选择头像文件');
        return;
    }

    const formData = new FormData();
    formData.append('avatar', file);

    fetch('/api/v1/user/uploadAvatar', {
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            alert('头像上传成功');
            closeEditModal();
            location.reload(); // 刷新页面以显示新头像
        } else {
            alert('头像上传失败');
        }
    }).catch(error => {
        console.error('头像上传错误:', error);
        alert('头像上传失败');
    });
}

// 监听文件选择事件
document.getElementById('avatar').addEventListener('change', function () {
    const fileName = this.files[0] ? this.files[0].name : '未选择文件';
    alert('已选择文件: ' + fileName);
});

function submitEditForm() {
    const username = document.getElementById('username').value;
    const phone = document.getElementById('phone').value;
    const email = document.getElementById('email').value;

    fetch('/api/v1/user/updateUserInfo', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `username=${username}&phone=${phone}&email=${email}`
    }).then(response => {
        if (response.ok) {
            alert('用户信息更新成功');
            closeEditModal();
            location.reload(); // 刷新页面以显示更新后的信息
        } else {
            alert('用户信息更新失败');
        }
    }).catch(error => {
        console.error('用户信息更新错误:', error);
        alert('用户信息更新失败');
    });
}
