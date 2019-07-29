package com.mvp.stokaudit.customer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class CustomerPresenterTest {

    @Mock
    private CustomerView customerView;

    private CustomerPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new CustomerPresenter(customerView);
    }

    @Test
    public void getDataCustomer() {

    }

    @Test
    public void getMoreCustomer() {
    }
}