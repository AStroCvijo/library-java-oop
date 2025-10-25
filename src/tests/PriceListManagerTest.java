package tests;

import model.entities.PriceListItem;
import model.enums.PriceListItemType;
import model.managers.PriceListManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PriceListManagerTest {

    private PriceListManager priceListManager;
    private String testFilename = "test_pricelist.csv";

    @BeforeEach
    void setUp() {
        priceListManager = new PriceListManager(testFilename);
    }

    @AfterEach
    void tearDown() {
        new File(testFilename).delete();
    }

    @Test
    void addAndGetPriceListItem() {
        PriceListItem item = new PriceListItem(1, PriceListItemType.YEARLY_SUBSCRIPTION, "Yearly", 1000, LocalDate.now(), LocalDate.now().plusYears(1));
        priceListManager.add(item);
        PriceListItem retrievedItem = priceListManager.getById(1);
        assertNotNull(retrievedItem);
        assertEquals("Yearly", retrievedItem.getDescription());
    }

    @Test
    void updatePriceListItem() {
        PriceListItem item = new PriceListItem(1, PriceListItemType.YEARLY_SUBSCRIPTION, "Yearly", 1000, LocalDate.now(), LocalDate.now().plusYears(1));
        priceListManager.add(item);
        item.setPrice(1200);
        priceListManager.update(item);
        PriceListItem retrievedItem = priceListManager.getById(1);
        assertEquals(1200, retrievedItem.getPrice());
    }

    @Test
    void deletePriceListItem() {
        PriceListItem item = new PriceListItem(1, PriceListItemType.YEARLY_SUBSCRIPTION, "Yearly", 1000, LocalDate.now(), LocalDate.now().plusYears(1));
        priceListManager.add(item);
        priceListManager.delete(1);
        assertNull(priceListManager.getById(1));
    }

    @Test
    void getAllPriceListItems() {
        priceListManager.add(new PriceListItem(1, PriceListItemType.YEARLY_SUBSCRIPTION, "Yearly", 1000, LocalDate.now(), LocalDate.now().plusYears(1)));
        priceListManager.add(new PriceListItem(2, PriceListItemType.PENALTY, "Penalty", 100, LocalDate.now(), LocalDate.now().plusYears(1)));
        List<PriceListItem> items = priceListManager.getAll();
        assertEquals(2, items.size());
    }
}
