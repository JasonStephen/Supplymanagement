let currentPage = 1;
let pageSize = 10;
let totalProducts = 0;

// 获取产品数据
function fetchProducts() {
    const searchQuery = document.getElementById('search-input').value;
    const categoryId = document.getElementById('category-filter').value;
    const sortBy = document.getElementById('sort-by').value;

    fetch(`/products?page=${currentPage}&size=${pageSize}&search=${searchQuery}&category=${categoryId}&sort=${sortBy}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(products => {
            if (Array.isArray(products)) {
                totalProducts = products.length; // 临时方案
                renderProducts(products);
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
            <div>${product.category.categoryName}</div>
            <div>￥${product.price}</div>
        `;
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
