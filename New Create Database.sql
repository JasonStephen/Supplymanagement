CREATE TABLE IF NOT EXISTS Product_Component (
                                                 product_id INT,
                                                 component_id INT,
                                                 quantity INT NOT NULL,
                                                 unit VARCHAR(20) NOT NULL,
    PRIMARY KEY (product_id, component_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id),
    FOREIGN KEY (component_id) REFERENCES Product(product_id) -- ✅ 自关联
    );

ALTER TABLE Product
    ADD COLUMN unit VARCHAR(20) NOT NULL

ALTER TABLE Sales_Order
    ADD COLUMN contract_id INT,
ADD FOREIGN KEY (contract_id) REFERENCES Sales_Contract(contract_id);

ALTER TABLE Logistics_Order
    ADD COLUMN agreement_id INT,
ADD FOREIGN KEY (agreement_id) REFERENCES Logistics_Agreement(agreement_id);

ALTER TABLE Purchase_Order
    ADD COLUMN contract_id INT,
ADD FOREIGN KEY (contract_id) REFERENCES Purchase_Contract(contract_id);

-- 删除物流订单表中的三个日期字段
ALTER TABLE Logistics_Order
DROP COLUMN shipping_date,
DROP COLUMN estimated_arrival_date,
DROP COLUMN actual_arrival_date;