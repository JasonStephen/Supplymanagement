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
        // 使用 marked 解析 Markdown
        markdownElement.innerHTML = marked.parse(description);

        // 限制图片宽度，防止溢出
        const images = markdownElement.querySelectorAll('img');
        images.forEach(img => {
            img.style.maxWidth = '100%'; // 图片最大宽度为父容器的 100%
            img.style.height = 'auto';   // 高度自动调整，保持比例
        });
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

// 加载配方信息
function loadComponents() {
    fetch(`/products/${productId}/components`)
        .then(response => response.json())
        .then(components => {
            const componentsList = document.getElementById('components-list');
            componentsList.innerHTML = '';
            if (components.length === 0) {
                componentsList.innerHTML = '<p>该产品没有配方</p>';
            } else {
                components.forEach(component => {
                    const componentDiv = document.createElement('div');
                    componentDiv.className = 'component-item';
                    componentDiv.innerHTML = `
                        <span>${component.component.name} - ${component.quantity} ${component.unit}</span>
                        <button class="btn-action" onclick="editComponent(${component.product.productId}, ${component.component.productId})">编辑</button>
                        <button class="btn-action" onclick="deleteComponent(${component.product.productId}, ${component.component.productId})">删除</button>
                    `;
                    componentsList.appendChild(componentDiv);
                });
            }
        });
}

// 加载所有组件（下拉菜单）
function loadAllComponents() {
    fetch('/products')
        .then(response => response.json())
        .then(products => {
            const componentSelect = document.getElementById('componentSelect');
            componentSelect.innerHTML = '<option value="">请选择组件</option>';
            products.forEach(product => {
                const option = document.createElement('option');
                option.value = product.productId;
                option.text = product.name;
                componentSelect.appendChild(option);
            });
        });
}


// 打开添加配方模态框
function openAddComponentModal() {
    loadAllComponents(); // 加载所有组件到下拉菜单
    document.getElementById('componentModalTitle').innerText = '添加配方';
    document.getElementById('componentForm').reset();
    document.getElementById('componentId').value = '';
    document.getElementById('componentModal').style.display = 'block';
}

// 关闭添加/编辑配方模态框
function closeComponentModal() {
    document.getElementById('componentModal').style.display = 'none';
}

// 编辑配方
function editComponent(productId, componentId) {
    fetch(`/product-components/product/${productId}/component/${componentId}`)
        .then(response => response.json())
        .then(component => {
            loadAllComponents(); // 加载所有组件到下拉菜单
            document.getElementById('componentModalTitle').innerText = '编辑配方';
            document.getElementById('componentId').value = component.productComponentId;
            document.getElementById('componentSelect').value = component.component.productId;
            document.getElementById('componentQuantity').value = component.quantity;
            document.getElementById('componentUnit').value = component.unit;
            document.getElementById('componentModal').style.display = 'block';
        });
}

// 删除配方
function deleteComponent(productId, componentId) {
    if (confirm('确定要删除该配方吗？')) {
        fetch(`/product-components/product/${productId}/component/${componentId}`, {
            method: 'DELETE'
        }).then(() => {
            loadComponents();
        });
    }
}

// 保存配方
document.getElementById('componentForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const componentId = document.getElementById('componentId').value;
    const selectedComponentId = document.getElementById('componentSelect').value;
    const componentQuantity = document.getElementById('componentQuantity').value;
    const componentUnit = document.getElementById('componentUnit').value;

    const method = componentId ? 'PUT' : 'POST';
    const url = componentId ? `/product-components/product/${productId}/component/${selectedComponentId}` : `/product-components`;

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            product: { productId: productId },
            component: { productId: selectedComponentId }, // 使用下拉菜单选中的组件 ID
            quantity: componentQuantity,
            unit: componentUnit
        })
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text) });
            }
            closeComponentModal();
            loadComponents();
        })
        .catch(error => {
            alert("保存失败: " + error.message);
        });
});

// 打开编辑产品模态框
function openEditProductModal() {
    fetch(`/products/${productId}`)
        .then(response => response.json())
        .then(product => {
            document.getElementById('editProductId').value = product.productId;
            document.getElementById('editProductName').value = product.name;
            document.getElementById('editProductDescription').value = product.description;
            document.getElementById('editProductPrice').value = product.price;
            document.getElementById('editProductUnit').value = product.unit;
            document.getElementById('editProductModal').style.display = 'block';
        });
}

// 关闭编辑产品模态框
function closeEditProductModal() {
    document.getElementById('editProductModal').style.display = 'none';
}

// 加载类别到编辑产品模态框的下拉菜单
function loadCategoriesForEdit() {
    fetch('/product-categories')
        .then(response => response.json())
        .then(categories => {
            const categorySelect = document.getElementById('editProductCategory');
            categorySelect.innerHTML = '<option value="">无</option>'; // 默认添加 "无" 选项
            categories.forEach(category => {
                const option = document.createElement('option');
                option.value = category.categoryId;
                option.text = category.categoryName;
                categorySelect.appendChild(option);
            });

            // 设置当前选中的类别
            fetch(`/products/${productId}`)
                .then(response => response.json())
                .then(product => {
                    const categoryId = product.category ? product.category.categoryId : '';
                    categorySelect.value = categoryId;
                });
        });
}

// 保存编辑产品
document.getElementById('editProductForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const productId = document.getElementById('editProductId').value;
    const name = document.getElementById('editProductName').value;
    const description = document.getElementById('editProductDescription').value;
    const categoryId = document.getElementById('editProductCategory').value;
    const price = document.getElementById('editProductPrice').value;
    const unit = document.getElementById('editProductUnit').value;
    const photo = document.getElementById('editProductPhoto').files[0];

    const formData = new FormData();
    formData.append('name', name);
    formData.append('description', description);
    formData.append('categoryId', categoryId || null); // 设置为 null 如果无类别
    formData.append('price', price);
    formData.append('unit', unit);
    if (photo) {
        formData.append('photo', photo);
    }

    fetch(`/products/${productId}`, {
        method: 'PUT',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text) });
            }
            closeEditProductModal();
            fetchProductDetails(); // 重新加载产品详情
            window.location.reload(); // 刷新页面
        })
        .catch(error => {
            alert("保存失败: " + error.message);
        });
});




// 页面加载时获取数据
// 页面加载时获取数据
document.addEventListener('DOMContentLoaded', function() {
    fetchProductDetails();
    loadComponents();
    loadCategoriesForEdit();
});

