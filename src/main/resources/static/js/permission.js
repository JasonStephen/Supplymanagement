//Modal
function openManageRolesModal() {
    document.getElementById('manageRolesModal').style.display = 'block';
}

function closeManageRolesModal() {
    document.getElementById('manageRolesModal').style.display = 'none';
}



// Permission.js
document.addEventListener('DOMContentLoaded', function() {
    loadPermissions();

    document.getElementById('showAddPermissionForm').addEventListener('click', function() {
        document.getElementById('addPermissionDiv').style.display = 'block';
        document.getElementById('editPermissionDiv').style.display = 'none';
        document.getElementById('showAddPermissionForm').style.display = 'none';
        document.getElementById('showEditPermissionForm').style.display = 'block';
    });

    document.getElementById('showEditPermissionForm').addEventListener('click', function() {
        document.getElementById('addPermissionDiv').style.display = 'none';
        document.getElementById('editPermissionDiv').style.display = 'block';
        document.getElementById('showAddPermissionForm').style.display = 'block';
        document.getElementById('showEditPermissionForm').style.display = 'none';
    });

    document.getElementById('addPermissionForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const formData = new FormData(this);
        fetch('/permission/createPermission', {
            method: 'POST',
            body: new URLSearchParams(formData)
        }).then(response => {
            if (response.ok) {
                loadPermissions();
                this.reset();
            }
        });
    });

    document.getElementById('editPermissionForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const formData = new FormData(this);
        const permissionId = formData.get('permissionId');
        fetch(`/permission/updatePermission?permissionId=${permissionId}`, {
            method: 'PUT',
            body: new URLSearchParams(formData)
        }).then(response => {
            if (response.ok) {
                loadPermissions();
                this.reset();
            }
        });
    });

    document.getElementById('addRoleForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const formData = new FormData(this);
        const permissionId = document.getElementById('editPermissionId').value;
        const roleId = formData.get('roleSelect');
        fetch(`/role/bindPermissionToRole?roleId=${roleId}&permissionId=${permissionId}`, {
            method: 'POST'
        }).then(response => {
            if (response.ok) {
                loadBoundRoles(permissionId);
            }
        });
    });
});

function loadPermissions() {
    fetch('/permission/getAllPermissions')
        .then(response => response.json())
        .then(data => {
            const permissionList = document.getElementById('permissionList');
            permissionList.innerHTML = '';
            data.forEach(permission => {
                const li = document.createElement('li');
                li.className = 'list-group-item';
                li.textContent = permission.permissionName;
                const btnContainer = document.createElement('div');
                btnContainer.className = 'btn-container';
                const editButton = document.createElement('button');
                editButton.className = 'btn btn-sm btn-warning';
                editButton.textContent = '编辑';
                editButton.onclick = () => editPermission(permission);
                const deleteButton = document.createElement('button');
                deleteButton.className = 'btn btn-sm btn-danger';
                deleteButton.textContent = '删除';
                deleteButton.onclick = () => deletePermission(permission.permissionId);
                const manageRolesButton = document.createElement('button');
                manageRolesButton.className = 'btn btn-sm btn-info';
                manageRolesButton.textContent = '管理角色';
                manageRolesButton.onclick = () => manageRoles(permission.permissionId);
                btnContainer.appendChild(editButton);
                btnContainer.appendChild(deleteButton);
                btnContainer.appendChild(manageRolesButton);
                li.appendChild(btnContainer);
                permissionList.appendChild(li);
            });
        });
}

function editPermission(permission) {
    document.getElementById('editPermissionId').value = permission.permissionId;
    document.getElementById('editPermissionName').value = permission.permissionName;
    document.getElementById('editPermissionCode').value = permission.permissionCode;
    document.getElementById('addPermissionDiv').style.display = 'none';
    document.getElementById('editPermissionDiv').style.display = 'block';
    document.getElementById('showAddPermissionForm').style.display = 'block';
    document.getElementById('showEditPermissionForm').style.display = 'none';
}

function deletePermission(permissionId) {
    openDeleteConfirmationModal(permissionId); // 打开确认弹窗
}

function manageRoles(permissionId) {
    document.getElementById('editPermissionId').value = permissionId;
    loadBoundRoles(permissionId);
    loadAvailableRoles();
    openManageRolesModal(); // 使用原生 JavaScript 打开模态框
}


function loadBoundRoles(permissionId) {
    fetch(`/permission/getRolesByPermission?permissionId=${permissionId}`)
        .then(response => response.json())
        .then(data => {
            const boundRolesList = document.getElementById('boundRolesList');
            boundRolesList.innerHTML = '';
            data.forEach(role => {
                const li = document.createElement('li');
                li.className = 'list-group-item d-flex justify-content-between align-items-center';
                li.textContent = role.roleName;
                const unbindButton = document.createElement('button');
                unbindButton.className = 'btn btn-sm btn-danger';
                unbindButton.textContent = '解绑';
                unbindButton.onclick = () => unbindRole(permissionId, role.roleId);
                li.appendChild(unbindButton);
                boundRolesList.appendChild(li);
            });
        });
}

function loadAvailableRoles() {
    fetch('/role/listRoles?page=0&size=100')
        .then(response => response.json())
        .then(data => {
            const roleSelect = document.getElementById('roleSelect');
            roleSelect.innerHTML = '';
            data.forEach(role => {
                const option = document.createElement('option');
                option.value = role.roleId;
                option.textContent = role.roleName;
                roleSelect.appendChild(option);
            });
        });
}

function unbindRole(permissionId, roleId) {
    fetch(`/role/unbindPermissionFromRole?roleId=${roleId}&permissionId=${permissionId}`, {
        method: 'DELETE'
    }).then(response => {
        if (response.ok) {
            loadBoundRoles(permissionId);
        }
    });
}

//删除确认弹窗逻辑
// 删除确认相关的全局变量
let permissionToDeleteId = null; // 用于保存待删除的权限 ID

// 打开删除确认模态框
function openDeleteConfirmationModal(permissionId) {
    permissionToDeleteId = permissionId; // 保存待删除的权限 ID
    document.getElementById('deleteConfirmationModal').style.display = 'block';
}

// 关闭删除确认模态框
function closeDeleteConfirmationModal() {
    document.getElementById('deleteConfirmationModal').style.display = 'none';
    permissionToDeleteId = null; // 清空待删除的权限 ID
}

// 确认删除
function confirmDelete() {
    if (permissionToDeleteId) {
        fetch(`/permission/deletePermission?permissionId=${permissionToDeleteId}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    loadPermissions(); // 删除成功后刷新权限列表
                }
            });
    }
    closeDeleteConfirmationModal(); // 关闭确认模态框
}

