package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * xToOne
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * entity 로 반환할 경우에 대한 위험성 예제
     *
     * @Author: jsj0828
     **/
    @GetMapping("/v1/simple-orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }

    @GetMapping("/v2/simple-orders")
    public List<OrderSimpleQueryDto> ordersV2() {
        return orderRepository.findAllByString(new OrderSearch())
                .stream()
                .map(OrderSimpleQueryDto::new)
                .collect(toList());

    }

    /**
     * 페치조인 쿼리
     *
     * @Author: jsj0828
     **/
    @GetMapping("/v3/simple-orders")
    public List<OrderSimpleQueryDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream()
                .map(OrderSimpleQueryDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 최적화 페치조인 쿼리
     *
     * @Author: jsj0828
     **/
    @GetMapping("/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }
}
