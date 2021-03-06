package com.signaretech.seneachat.service;

import com.signaretech.seneachat.SeneachatApplication;
import com.signaretech.seneachat.config.TestConfig;
import com.signaretech.seneachat.persistence.dao.repo.EntUserRepo;
import com.signaretech.seneachat.persistence.entity.EntUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SeneachatApplication.class, TestConfig.class})
@ActiveProfiles("junit")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Ignore
public class SellerServiceTest {

    private IUserService sellerService;

    @Autowired
    private EntUserRepo sellerRepo;

    @Mock
    private IMailService mailService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;


    @Before
    public void init() {
        sellerService = new UserService(mailService, sellerRepo, passwordEncoder);
    }

    @Test
    public void testCrudSeller() {
        EntUser seller = getSeller("tapha.bop1@seneachat.com");
        sellerService.createSeller(seller);
        EntUser existingSeller = sellerService.findByEmail(seller.getUsername());

        Assert.assertEquals("Verify seller email", seller.getUsername(), existingSeller.getUsername());
        existingSeller.setCellPhone("4051111111");
        sellerService.updateSeller(existingSeller);

        EntUser updatedSeller = sellerService.findByEmail(seller.getUsername());
        Assert.assertEquals("Verify seller phone number", "4051111111", updatedSeller.getCellPhone());
    }

    @Test
    public void testRegisterSeller() throws Exception {
        EntUser seller = getSeller("tapha.bop2@seneachat.com");

        doNothing().when(mailService).sendMail(anyString(), anyString(), anyString());
        sellerService.register(seller);
        EntUser createdSeller = sellerService.findByEmail(seller.getUsername());

        verify(mailService, times(1)).sendMail(anyString(), anyString(), anyString());
        Assert.assertEquals("Verify created seller", "tapha.bop2@seneachat.com", createdSeller.getUsername());
    }

    @Test
    public void testResendActivationCode() throws Exception {
        EntUser seller = getSeller("tapha.bop3@seneachat.com");
        EntUser savedSeller = sellerService.createSeller(seller);

        doNothing().when(mailService).sendMail(anyString(), anyString(), anyString());
        sellerService.resendActivationCode("tapha.bop3@seneachat.com");
        EntUser dbSeller = sellerService.findByEmail(seller.getUsername());

        verify(mailService, times(1)).sendMail(anyString(), anyString(), anyString());
        Assert.assertNotNull("First activation code is not null", savedSeller.getActivationCode());
        Assert.assertNotNull("Second activation code is not null", dbSeller.getActivationCode());
        Assert.assertNotEquals("A new activation code was saved", savedSeller.getActivationCode(), dbSeller.getActivationCode());
    }

    @Test
    public void testAuthenticateUserSuccess() {
/*        EntSeller seller = getSeller("tapha.bop4@seneachat.com");
        EntSeller savedSeller = sellerService.createSeller(seller);

        AuthenticationResult result = sellerService.authenticateUser(savedSeller);
        Assert.assertTrue(result.isAuthenticated());
        Assert.assertNull(result.getError());*/
    }

    @Test
    public void testAuthenticateUserFail() {
/*        EntSeller seller = getSeller("tapha.bop5@seneachat.com");
        EntSeller savedSeller = sellerService.createSeller(seller);
        savedSeller.setPassword("invalid");
        AuthenticationResult result = sellerService.authenticateUser(savedSeller);
        Assert.assertFalse( result.isAuthenticated());
        Assert.assertNotNull(result.getError());*/
    }

    private EntUser getSeller(String email) {
        EntUser seller = new EntUser("Tapha", "Bop", email);
        seller.setPassword("welcome1");
        seller.setPassword2("welcome1");
        seller.setCellPhone("4053288268");
        return seller;
    }

}
