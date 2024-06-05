package org.DVM;

import org.DVM.Stock.Stock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StockTest {
    private final Stock stock = new Stock();

    @Test
    public void testCheckStock() {
        assertTrue(stock.checkStock(1, 5), "Stock should be enough");
        assertFalse(stock.checkStock(20, 6), "Stock should not be enough");
    }
}
