//Modal
function openManagePermissionsModal() {
    document.getElementById('managePermissionsModal').style.display = 'block';
}

function closeManagePermissionsModal() {
    document.getElementById('managePermissionsModal').style.display = 'none';
}


//Role.js
document.addEventListener('DOMContentLoaded', function() {
    loadRoles();

    document.getElementById('showAddRoleForm').addEventListener('click', function() {
        document.getElementById('addRoleDiv').style.display = 'block';
        document.getElementById('editRoleDiv').style.display = 'none';
        document.getElementById('showAddRoleForm').style.display = 'none';
        document.getElementById('showEditRoleForm').style.display = 'block';
    });

    document.getElementById('showEditRoleForm').addEventListener('click', function() {
        document.getElementById('addRoleDiv').style.display = 'none';
        document.getElementById('editRoleDiv').style.display = 'block';
        document.getElementById('showAddRoleForm').style.display = 'block';
        document.getElementById('showEditRoleForm').style.display = 'none';
    });

    document.getElementById('addRoleForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const formData = new FormData(this);
        fetch('/role/createRole', {
            method: 'POST',
            body: new URLSearchParams(formData)
        }).then(response => {
            if (response.ok) {
                loadRoles();
                this.reset();
            }
        });
    });

    document.getElementById('editRoleForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const formData = new FormData(this);
        const roleId = formData.get('roleId');
        fetch(`/role/updateRole?roleId=${roleId}`, {
            method: 'PUT',
            body: new URLSearchParams(formData)
        }).then(response => {
            if (response.ok) {
                loadRoles();
                this.reset();
            }
        });
    });

    document.getElementById('addPermissionForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const formData = new FormData(this);
        const roleId = document.getElementById('editRoleId').value;
        const permissionId = formData.get('permissionSelect');
        fetch(`/role/bindPermissionToRole?roleId=${roleId}&permissionId=${permissionId}`, {
            method: 'POST'
        }).then(response => {
            if (response.ok) {
                loadBoundPermissions(roleId);
            }
        });
    });
});

function loadRoles() {
    fetch('/role/listRoles?page=0&size=10')
        .then(response => response.json())
        .then(data => {
            const roleList = document.getElementById('roleList');
            roleList.innerHTML = '';
            data.forEach(role => {
                const li = document.createElement('li');
                li.className = 'list-group-item';
                li.textContent = role.roleName;
                const btnContainer = document.createElement('div');
                btnContainer.className = 'btn-container';
                const editButton = document.createElement('button');
                editButton.className = 'btn btn-sm btn-warning';
                editButton.textContent = '编辑';
                editButton.onclick = () => editRole(role);
                const deleteButton = document.createElement('button');
                deleteButton.className = 'btn btn-sm btn-danger';
                deleteButton.textContent = '删除';
                deleteButton.onclick = () => deleteRole(role.roleId);
                const managePermissionsButton = document.createElement('button');
                managePermissionsButton.className = 'btn btn-sm btn-info';
                managePermissionsButton.textContent = '管理权限';
                managePermissionsButton.onclick = () => managePermissions(role.roleId);
                btnContainer.appendChild(editButton);
                btnContainer.appendChild(deleteButton);
                btnContainer.appendChild(managePermissionsButton);
                li.appendChild(btnContainer);
                roleList.appendChild(li);
            });
        });
}

function editRole(role) {
    document.getElementById('editRoleId').value = role.roleId;
    document.getElementById('editRoleName').value = role.roleName;
    document.getElementById('addRoleDiv').style.display = 'none';
    document.getElementById('editRoleDiv').style.display = 'block';
    document.getElementById('showAddRoleForm').style.display = 'block';
    document.getElementById('showEditRoleForm').style.display = 'none';
}

function deleteRole(roleId) {
    openDeleteConfirmationModal(roleId); // 打开确认弹窗
}

function managePermissions(roleId) {
    document.getElementById('editRoleId').value = roleId;
    loadBoundPermissions(roleId);
    loadAvailablePermissions();
    openManagePermissionsModal(); // 使用原生 JavaScript 打开模态框
}


function loadBoundPermissions(roleId) {
    fetch(`/role/getPermissionsByRole?roleId=${roleId}`)
        .then(response => response.json())
        .then(data => {
            const boundPermissionsList = document.getElementById('boundPermissionsList');
            boundPermissionsList.innerHTML = '';
            data.forEach(permission => {
                const li = document.createElement('li');
                li.className = 'list-group-item d-flex justify-content-between align-items-center';
                li.textContent = permission.permissionName;
                const unbindButton = document.createElement('button');
                unbindButton.className = 'btn btn-sm btn-danger';
                unbindButton.textContent = '解绑';
                unbindButton.onclick = () => unbindPermission(roleId, permission.permissionId);
                li.appendChild(unbindButton);
                boundPermissionsList.appendChild(li);
            });
        });
}

function loadAvailablePermissions() {
    fetch('/permission/getAllPermissions')
        .then(response => response.json())
        .then(data => {
            const permissionSelect = document.getElementById('permissionSelect');
            permissionSelect.innerHTML = '';
            data.forEach(permission => {
                const option = document.createElement('option');
                option.value = permission.permissionId;
                option.textContent = permission.permissionName;
                permissionSelect.appendChild(option);
            });
        });
}

function unbindPermission(roleId, permissionId) {
    fetch(`/role/unbindPermissionFromRole?roleId=${roleId}&permissionId=${permissionId}`, {
        method: 'DELETE'
    }).then(response => {
        if (response.ok) {
            loadBoundPermissions(roleId);
        }
    });
}

//删除确认弹窗逻辑
let roleToDeleteId = null; // 用于保存待删除的角色 ID

// 打开删除确认模态框
function openDeleteConfirmationModal(roleId) {
    roleToDeleteId = roleId; // 保存待删除的角色 ID
    document.getElementById('deleteConfirmationModal').style.display = 'block';
}

// 关闭删除确认模态框
function closeDeleteConfirmationModal() {
    document.getElementById('deleteConfirmationModal').style.display = 'none';
    roleToDeleteId = null; // 清空待删除的角色 ID
}

// 确认删除
function confirmDelete() {
    if (roleToDeleteId) {
        fetch(`/role/deleteRole?roleId=${roleToDeleteId}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    loadRoles(); // 删除成功后刷新角色列表
                }
            });
    }
    closeDeleteConfirmationModal(); // 关闭确认模态框
}

