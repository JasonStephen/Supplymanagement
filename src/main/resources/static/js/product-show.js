let currentPage = 1;
let pageSize = 10;
let totalProducts = 0;

// 获取产品数据
function fetchProducts() {
    const searchQuery = document.getElementById('search-input').value;
    const categoryId = document.getElementById('category-filter').value;
    const sortBy = document.getElementById('sort-by').value || 'productId_desc'; // 默认值改为 productId_desc

    fetch(`/products/page?page=${currentPage}&size=${pageSize}&search=${searchQuery}&category=${categoryId}&sort=${sortBy}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.content && Array.isArray(data.content)) {
                totalProducts = data.totalElements;
                renderProducts(data.content);
                updatePagination();
            }
        })
        .catch(error => console.error('Fetch error:', error));
}


// 渲染产品块
function renderProducts(products) {
    const productGrid = document.getElementById('product-grid');
    productGrid.innerHTML = ''; // 清空当前内容

    products.forEach(product => {
        const productBlock = document.createElement('div');
        productBlock.className = 'product-block';
        productBlock.innerHTML = `
            <img src="${product.photo || '/products/uploads/products/productdefault.png'}" alt="${product.name}">
            <div>${product.name}</div>
            <div>${product.categoryName || "默认类别"}</div>
            <div>￥${product.price || "0.00"}</div> <!-- 默认值避免 null -->
        `;

        // 如果用户拥有 GOODS_SET 权限，添加删除按钮
        if (hasGoodsSetPermission) {
            const deleteButton = document.createElement('button');
            deleteButton.innerText = '删除';
            deleteButton.className = 'delete-button'; // 添加样式类
            deleteButton.addEventListener('click', (event) => {
                event.stopPropagation(); // 阻止事件冒泡
                deleteProduct(product.productId);
            });
            productBlock.appendChild(deleteButton);
        }

        productBlock.addEventListener('click', () => {
            window.location.href = `/product/details/${product.productId}`;
        });
        productGrid.appendChild(productBlock);
    });
}



// 更新分页
function updatePagination() {
    document.getElementById('current-page').textContent = currentPage;
    document.getElementById('prev-page').disabled = currentPage === 1;
    document.getElementById('next-page').disabled = currentPage * pageSize >= totalProducts;
}

function deleteProduct(productId) {
    if (confirm('确认删除该产品吗？')) {
        fetch(`/products/${productId}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    fetchProducts(); // 重新加载产品列表
                } else {
                    console.error('删除失败');
                }
            })
            .catch(error => console.error('Error:', error));
    }
}



// 分页按钮事件
document.getElementById('prev-page').addEventListener('click', () => {
    if (currentPage > 1) {
        currentPage--;
        fetchProducts();
    }
});

document.getElementById('next-page').addEventListener('click', () => {
    if (currentPage * pageSize < totalProducts) {
        currentPage++;
        fetchProducts();
    }
});

// 搜索、筛选、排序事件
document.getElementById('search-input').addEventListener('input', fetchProducts);
document.getElementById('category-filter').addEventListener('change', fetchProducts);
document.getElementById('sort-by').addEventListener('change', fetchProducts);

// 初始化
document.addEventListener('DOMContentLoaded', fetchProducts);

// 显示添加产品模态框
function showAddProductModal() {
    document.getElementById('add-product-modal').style.display = 'block';
}

// 关闭添加产品模态框
function closeAddProductModal() {
    document.getElementById('add-product-modal').style.display = 'none';
}

// 绑定表单提交事件
document.getElementById('add-product-form').addEventListener('submit', function (event) {
    event.preventDefault(); // 阻止默认提交行为
    addProduct();
});

// 添加产品
function addProduct() {
    const formData = new FormData();
    formData.append('name', document.getElementById('product-name').value);
    formData.append('description', document.getElementById('product-description').value);
    formData.append('categoryId', document.getElementById('product-category').value);
    formData.append('price', document.getElementById('product-price').value);
    formData.append('unit', document.getElementById('product-unit').value);
    const photoFile = document.getElementById('product-photo').files[0];
    if (photoFile) {
        formData.append('photo', photoFile);
    }

    fetch('/products', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                closeAddProductModal(); // 关闭模态框
                fetchProducts(); // 重新加载产品列表
            } else {
                console.error('添加产品失败');
            }
        })
        .catch(error => console.error('Error:', error));
}