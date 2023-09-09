package com.trendyol.shipment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BasketShipmentManager {
    private final int THRESHOLD_FOR_SHIPMENT_SIZE = 3;

    private final List<Product> products;
    private final List<ShipmentSize> sizesMoreThanThreeTimesInBucket = new LinkedList<>();

    private final HashMap<ShipmentSize, Integer> productSizeCounts = getProductSizeCounter();

    public BasketShipmentManager(Basket basket) {
        this.products = basket.getProducts();
    }

    public ShipmentSize getBasketShipmentSize(){
        if(products.size() < THRESHOLD_FOR_SHIPMENT_SIZE || !BasketHasThreeOrMoreSameSizeItems()){
             return getBiggestProductSize();
        }else{
            return getBiggestAndMostFrequentShipmentSize();
        }
    }

    private ShipmentSize getBiggestAndMostFrequentShipmentSize() {
        ShipmentSize BiggestAndMostFrequentShipmentSize = ShipmentSize.SMALL;

        for (ShipmentSize shipmentSize: sizesMoreThanThreeTimesInBucket) {
            if( shipmentSize.ordinal() > BiggestAndMostFrequentShipmentSize.ordinal()){
                BiggestAndMostFrequentShipmentSize = shipmentSize;
            }
        }
        return BiggestAndMostFrequentShipmentSize == ShipmentSize.X_LARGE ?
                ShipmentSize.X_LARGE :
                ShipmentSize.values()[BiggestAndMostFrequentShipmentSize.ordinal() + 1];
    }

    private boolean BasketHasThreeOrMoreSameSizeItems(){
        traverseBasketAndCalculateTotalCountForEachShipmentSize();
        for(Integer size: productSizeCounts.values()){
            if(size >= 3){
                return true;
            }
        }
        return false;
    }


    private void traverseBasketAndCalculateTotalCountForEachShipmentSize(){
        for (Product product: products) {
            int count = productSizeCounts.get(product.getSize()) + 1;
            productSizeCounts.put(product.getSize(), count);

            if(count >= THRESHOLD_FOR_SHIPMENT_SIZE){
                sizesMoreThanThreeTimesInBucket.add(product.getSize());
            }
        }
    }

    private HashMap<ShipmentSize, Integer> getProductSizeCounter(){
        HashMap<ShipmentSize, Integer> productSizeCounts = new HashMap<>();

        for (ShipmentSize size: ShipmentSize.values()) {
            productSizeCounts.put(size, 0);
        }
        return productSizeCounts;
    }

    private ShipmentSize getBiggestProductSize() {
        ShipmentSize biggestProductSize = ShipmentSize.SMALL;

        for (Product product: products) {
            if(product.getSize().ordinal() > biggestProductSize.ordinal()){
                biggestProductSize = product.getSize();
            }
        }
        return biggestProductSize;
    }
}

