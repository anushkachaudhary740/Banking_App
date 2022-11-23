package com.cognologix.BankSystemApplicationAssignment.service.services;
import com.cognologix.BankSystemApplicationAssignment.dao.CustomerRepo;
import com.cognologix.BankSystemApplicationAssignment.model.Customer;
import com.cognologix.BankSystemApplicationAssignment.service.servicesimplementation.CustomerServicesImplementation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class CustomerServicesTest {
@Autowired
private CustomerRepo customerRepo;
@Autowired
private CustomerServicesImplementation customerServicesImplementation;
    @BeforeEach
    void setUp() {
        this.customerServicesImplementation= new CustomerServicesImplementation(this.customerRepo);
    }
    @Test
    void createNewCustomer() {
        Customer customer=new Customer();
        customer.setCustomerId(101);
        customer.setCustomerName("Anushka");
        customerRepo.save(customer);
        Assertions.assertEquals(101,customer.getCustomerId());
    }
    @Test
    void getCustomerById() {
        Customer customerEntity = new Customer();
        customerEntity.setCustomerId(11);
        customerRepo.save(customerEntity);
        Boolean actualResult = customerRepo.existsById(11);
        assertThat(actualResult).isFalse();
    }
    @Test
    void findAllCustomerDetails() {
//        customerServicesImplementation.findAllCustomerDetails();
//        verify(customerRepo).findAll();
        List<Customer> customer1 = customerRepo.findAll();
        Assertions.assertEquals(4,customer1.size());
}

    @Test
    void updateCustomerDetails() {
        Customer customer = customerRepo.findById(13).get();
        customer.setCustomerName("Mohan");
        customer.setCustomerEmail("anu@gmail.com");
        customer.setCustomerMobileNumber("7896543289");
        customerRepo.save(customer);
        //customerRepo.save(customer);
    }

    @Test
    void deleteCustomer() {
            customerRepo.deleteById(14);
            Boolean result = customerRepo.existsById(14);
            Assertions.assertEquals(false,result);
    }
}