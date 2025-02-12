document.addEventListener('DOMContentLoaded', function () {
    loadProducts();
});


// 加载产品列表
async function loadProducts() {
    try {
        const response = await fetch('/products');
        const data = await response.json();
        const productGrid = document.getElementById('product-grid');
        productGrid.innerHTML = '';


        for (const product of data) {
            // 检查产品是否有配方
            const hasComponentsResult = await hasComponents(product.productId);
            if (hasComponentsResult) {
                // 创建产品块
                const productBlock = document.createElement('div');
                productBlock.className = 'product-block';
                productBlock.innerHTML = `
                    <img src="${product.photo || '/products/uploads/products/productdefault.png'}" alt="${product.name}">
                    <div>${product.name}</div>
                    <div>库存: ${await getInventoryQuantity(product.productId)}</div>
                    <div>告警状态: ${await getAlertStatus(product.productId)}</div>
                `;

                // 绑定点击事件，打开生产面板
                productBlock.addEventListener('click', () => openProductionModal(product));

                // 将产品块添加到列表中
                productGrid.appendChild(productBlock);
            }
        }
    } catch (error) {
        console.error('Error loading products:', error);
    }
}

// 检查产品是否有配方
async function hasComponents(productId) {
    try {
        const response = await fetch(`/products/${productId}/components`);
        const data = await response.json();
        return data.length > 0;
    } catch (error) {
        console.error('Error checking components:', error);
        return false;
    }
}

// 获取产品库存数量
async function getInventoryQuantity(productId) {
    try {
        const response = await fetch(`/inventory/${productId}`);
        const data = await response.json();
        return data.quantity || 0;
    } catch (error) {
        console.error('Error fetching inventory:', error);
        return 0;
    }
}

// 获取产品告警状态
async function getAlertStatus(productId) {
    try {
        const response = await fetch(`/inventory/${productId}`);
        const data = await response.json();
        return data.quantity < data.alertThreshold ? "告警" : "正常";
    } catch (error) {
        console.error('Error fetching alert status:', error);
        return "未知";
    }
}

// 打开生产面板
async function openProductionModal(product) {
    const modal = document.getElementById('production-modal');
    const productName = document.getElementById('product-name');
    const productQuantity = document.getElementById('product-quantity');
    const productAlertStatus = document.getElementById('product-alert-status');
    const componentsTableBody = document.getElementById('components-table-body');

    // 设置产品信息
    productName.textContent = product.name;
    productName.dataset.productId = product.productId; // 添加 data-product-id 属性
    productQuantity.textContent = await getInventoryQuantity(product.productId);
    productAlertStatus.textContent = await getAlertStatus(product.productId);

    // 加载配方信息
    await loadComponents(product.productId, componentsTableBody);

    // 计算最大可生产数量
    await calculateMaxProduction(product.productId);

    // 显示模态框
    modal.style.display = 'block';
}

// 加载配方信息
async function loadComponents(productId, tableBody) {
    try {
        const response = await fetch(`/products/${productId}/components`);
        const data = await response.json();
        tableBody.innerHTML = '';
        data.forEach(component => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${component.component.name}</td>
                <td>${component.quantity}</td>
                <td>${component.unit}</td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading components:', error);
    }
}

// 计算最大可生产数量
async function calculateMaxProduction(productId) {
    try {
        const response = await fetch(`/products/${productId}/components`);
        const data = await response.json();
        let maxProduction = Infinity;
        for (const component of data) {
            const inventoryResponse = await fetch(`/inventory/${component.component.productId}`);
            const inventoryData = await inventoryResponse.json();
            const available = inventoryData.quantity;
            const required = component.quantity;
            maxProduction = Math.min(maxProduction, Math.floor(available / required));
        }
        document.getElementById('max-production').value = maxProduction;
    } catch (error) {
        console.error('Error calculating max production:', error);
    }
}

// 验证生产数量
function validateProductionQuantity() {
    const maxProduction = parseInt(document.getElementById('max-production').value, 10);
    const productionQuantity = document.getElementById('production-quantity');
    let value = parseInt(productionQuantity.value, 10);

    if (isNaN(value) || value < 1) {
        productionQuantity.value = 1;
    } else if (value > maxProduction) {
        productionQuantity.value = maxProduction;
    }
}

// 开始生产
async function startProduction() {
    const productId = document.getElementById('product-name').dataset.productId;
    const quantity = document.getElementById('production-quantity').value;
    const userId = currentUserId; // 替换为实际的获取用户 ID 的逻辑

    try {
        const response = await fetch(`/products/${productId}/produce`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                quantity: quantity,
                userId: userId
            }),
        });

        const result = await response.json();
        if (response.ok) {
            closeProductionModal();
            loadProducts(); // 重新加载产品列表
        } else {
            document.getElementById('error-message').textContent = result; // 显示错误信息
            document.getElementById('error-message').style.display = 'block';
        }
    } catch (error) {
        console.error('Error producing product:', error);
        document.getElementById('error-message').textContent = '出现错误，请稍后重试';
        document.getElementById('error-message').style.display = 'block';
    }
}

// 关闭生产面板
function closeProductionModal() {
    const modal = document.getElementById('production-modal');
    modal.style.display = 'none';
}
