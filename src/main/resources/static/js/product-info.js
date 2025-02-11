const productId = window.location.pathname.split('/').pop();

// 获取产品详情
function fetchProductDetails() {
    fetch(`/products/details/${productId}`)
        .then(response => response.json())
        .then(data => {
            // 渲染 Markdown 格式的描述
            renderMarkdown(data.description);
        })
        .catch(error => console.error('Error fetching product details:', error));
}

// 渲染 Markdown 格式的内容
function renderMarkdown(description) {
    const markdownElement = document.getElementById('description-markdown');
    if (markdownElement) {
        markdownElement.innerHTML = marked.parse(description);
    }
}

// 打开类别管理模态框
function openCategoryModal() {
    const modal = document.getElementById('categoryModal');
    modal.style.display = 'block';
    loadCategories(); // 加载类别列表
}

// 关闭类别管理模态框
function closeCategoryModal() {
    const modal = document.getElementById('categoryModal');
    modal.style.display = 'none';
}

// 打开绑定类别模态框 （功能废弃）
// function openBindCategoryModal() {
//     const modal = document.getElementById('bindCategoryModal');
//     modal.style.display = 'block';
//     loadParentCategories(); // 加载父类别
// }
//
// // 关闭绑定类别模态框
// function closeBindCategoryModal() {
//     const modal = document.getElementById('bindCategoryModal');
//     modal.style.display = 'none';
// }




// 加载类别列表
function loadCategories() {
    fetch('/product-categories')
        .then(response => response.json())
        .then(categories => {
            const tableBody = document.querySelector('#categoryTable tbody');
            tableBody.innerHTML = '';
            categories.forEach(category => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${category.categoryId}</td>
                    <td>${category.categoryName}</td>
                    <td>
                        <button class="btn-action" onclick="bindCategoryToProduct(${category.categoryId})">绑定</button>
                        <button class="btn-action" onclick="deleteCategory(${category.categoryId})">删除</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        });
}

// 绑定类别到产品
function bindCategoryToProduct(categoryId) {
    fetch(`/products/${productId}/bind-category/${categoryId}`, {
        method: 'POST'
    }).then(response => {
        if (response.ok) {
            alert('绑定成功！');
            closeCategoryModal();
            window.location.reload();
        } else {
            alert('绑定失败！');
        }
    });
}

// 添加类别
document.getElementById('addCategoryForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const newCategoryName = document.getElementById('newCategoryName').value;
    fetch('/product-categories', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ categoryName: newCategoryName })
    }).then(response => {
        if (response.ok) {
            alert('添加成功！');
            loadCategories(); // 重新加载类别列表
        } else {
            alert('添加失败！');
        }
    });
});

// 删除类别
function deleteCategory(categoryId) {
    fetch(`/product-categories/${categoryId}`, {
        method: 'DELETE'
    }).then(response => {
        if (response.ok) {
            alert('删除成功！');
            loadCategories(); // 重新加载类别列表
        } else {
            alert('删除失败！');
        }
    });
}

// // 加载父类别 （功能废弃）
// function loadParentCategories() {
//     fetch('/product-categories')
//         .then(response => response.json())
//         .then(categories => {
//             const parentCategorySelect = document.getElementById('parentCategory');
//             parentCategorySelect.innerHTML = '<option value="">请选择父类别</option>';
//             categories.forEach(category => {
//                 if (!category.parentCategory) {
//                     parentCategorySelect.innerHTML += `<option value="${category.categoryId}">${category.categoryName}</option>`;
//                 }
//             });
//         });
// }
//
// // 加载子类别 （功能废弃）
// function loadChildCategories(parentCategoryId) {
//     const childCategorySelect = document.getElementById('childCategory');
//     if (parentCategoryId) {
//         fetch(`/product-categories?parentId=${parentCategoryId}`)
//             .then(response => response.json())
//             .then(categories => {
//                 childCategorySelect.disabled = false;
//                 childCategorySelect.innerHTML = '<option value="">请选择子类别</option>';
//                 categories.forEach(category => {
//                     childCategorySelect.innerHTML += `<option value="${category.categoryId}">${category.categoryName}</option>`;
//                 });
//             });
//     } else {
//         childCategorySelect.disabled = true;
//         childCategorySelect.innerHTML = '<option value="">请先选择父类别</option>';
//     }
// }

// 绑定类别 （功能废弃）
// document.getElementById('bindCategoryForm').addEventListener('submit', function (event) {
//     event.preventDefault();
//     const childCategoryId = document.getElementById('childCategory').value;
//     if (childCategoryId) {
//         fetch(`/products/${productId}/bind-category/${childCategoryId}`, {
//             method: 'POST'
//         }).then(response => {
//             if (response.ok) {
//                 alert('绑定成功！');
//                 closeBindCategoryModal();
//                 window.location.reload();
//             } else {
//                 alert('绑定失败！');
//             }
//         });
//     } else {
//         alert('请选择子类别！');
//     }
// });






function setAlertThreshold() {
    const threshold = prompt("请输入库存告警值：");
    if (threshold !== null) {
        fetch(`/inventory/${productId}/alert-threshold?alertThreshold=${threshold}`, {
            method: 'PUT'
        }).then(response => {
            if (response.ok) {
                fetchProductDetails(); // 重新加载数据
            } else {
                alert("设置失败");
            }
        });
    }
}

function adjustInventory() {
    const quantity = prompt("请输入库存调整数量（正数增加，负数减少）：");
    if (quantity !== null) {
        fetch(`/inventory/${productId}/adjust?quantity=${quantity}`, {
            method: 'PUT'
        }).then(response => {
            if (response.ok) {
                fetchProductDetails(); // 重新加载数据
            } else {
                alert("调整失败");
            }
        });
    }
}

function editProduct() {
    window.location.href = `/product/edit/${productId}`;
}



// 绑定类别
function bindCategory() {
    // 提示用户选择类别
    const categoryId = prompt("请输入要绑定的类别ID：");
    if (categoryId !== null && categoryId !== "") {
        // 调用后端接口绑定类别到产品
        fetch(`/products/${productId}/bind-category/${categoryId}`, {
            method: 'POST'
        }).then(response => {
            if (response.ok) {
                alert('类别绑定成功！');
                window.location.reload(); // 刷新页面
            } else {
                alert('类别绑定失败！');
            }
        });
    }
}




// 页面加载时获取数据
document.addEventListener('DOMContentLoaded', fetchProductDetails);
