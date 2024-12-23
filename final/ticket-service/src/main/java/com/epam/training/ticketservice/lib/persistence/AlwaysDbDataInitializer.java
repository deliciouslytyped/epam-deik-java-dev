package com.epam.training.ticketservice.lib.persistence;

import com.epam.training.ticketservice.lib.pricing.persistence.BasePrice;
import com.epam.training.ticketservice.lib.pricing.persistence.BasePriceRepository;
import com.epam.training.ticketservice.lib.user.persistence.AdminRepository;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@DependsOn({"entityManagerFactory"})
@RequiredArgsConstructor
public class AlwaysDbDataInitializer implements InitializingBean {
    private final AdminRepository ar;
    private final BasePriceRepository bpr;

    @PostConstruct
    public void init() {
        System.out.println("AlwaysDbDataInitializer bean was created!");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        var dpe = PasswordEncoderFactories.createDelegatingPasswordEncoder(); //TODO Ok this raises a point in that if I dont use the standard stuff I have to do this manually
        var adm = new ApplicationAdmin(null, "admin", dpe.encode("admin"));
        ar.save(adm);

        var bp = new BasePrice(1500);
        bpr.save(bp);
    }
}
