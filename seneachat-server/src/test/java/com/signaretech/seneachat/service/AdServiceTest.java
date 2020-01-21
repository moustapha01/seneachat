package com.signaretech.seneachat.service;

import com.signaretech.seneachat.model.PriceFilterEntry;
import com.signaretech.seneachat.persistence.dao.repo.EntAdRepo;
import com.signaretech.seneachat.persistence.dao.repo.EntCategoryRepo;
import com.signaretech.seneachat.persistence.dao.repo.EntPhotoRepo;
import com.signaretech.seneachat.persistence.dao.repo.EntSellerRepo;
import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AdServiceTest {

    private IAdService adService;

    @Mock
    private EntAdRepo adRepo;
    @Mock
    private EntSellerRepo sellerRepo;
    @Mock
    private ICategoryService categoryService;
    @Mock
    private EntCategoryRepo categoryRepo;
    @Mock
    private EntPhotoRepo photoRepo;

    @Before
    public void init() {
        this.adService = new AdServiceImpl(adRepo, sellerRepo, categoryService, categoryRepo, photoRepo);
    }

    @Test
    public void testGetPriceFilters() {

        EntAdvertisement ad1 = new EntAdvertisement();
        ad1.setPrice(1000d);

        EntAdvertisement ad2 = new EntAdvertisement();
        ad2.setPrice(2000d);

        EntAdvertisement ad3 = new EntAdvertisement();
        ad3.setPrice(3000d);

        EntAdvertisement ad4 = new EntAdvertisement();
        ad4.setPrice(4000d);

        EntAdvertisement ad5 = new EntAdvertisement();
        ad5.setPrice(5000d);

        List<EntAdvertisement> ads = new ArrayList<>();
        ads.add(ad1);
        ads.add(ad2);
        ads.add(ad3);
        ads.add(ad4);
        ads.add(ad5);

        List<PriceFilterEntry> priceFilters = adService.getPriceFilters(ads);

        assertEquals(5, priceFilters.size());
    }

    @Test
    public void whenListIsOfIntegerThenMaxCanBeDoneUsingIntegerComparator() {
        // given
        List<Integer> listOfIntegers = Arrays.asList(1, 2, 3, 4, 56, 7, 89, 10);
        Integer expectedResult = 89;

        // then
        Integer max = listOfIntegers
                .stream()
                .mapToInt(v -> v)
                .max().orElseThrow(NoSuchElementException::new);

        assertEquals("Should be 89", expectedResult, max);
    }

}
