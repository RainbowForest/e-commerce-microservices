package com.rainbowforest.orderservice.service;


import com.rainbowforest.orderservice.domain.Order;
import com.rainbowforest.orderservice.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTests {

    private final Long ORDER_ID = 1L;
    private final String ORDER_STATUS = "testStatus";
    private Order order;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Before
    public void setUp(){
        order = new Order();
        order.setId(ORDER_ID);
        order.setStatus(ORDER_STATUS);
    }

    @Test
    public void save_order_test(){
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order created = orderService.saveOrder(order);

        assertEquals(created.getId(), ORDER_ID);
        assertEquals(created.getStatus(), ORDER_STATUS);
        verify(orderRepository, times(1)).save(any(Order.class));
        verifyNoMoreInteractions(orderRepository);

    }


}
