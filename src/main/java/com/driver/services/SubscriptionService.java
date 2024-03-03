package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay

        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();

        Subscription subscription = new Subscription();

        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());

        Date d = new Date();
        subscription.setStartSubscriptionDate(d);

        if(subscriptionEntryDto.getSubscriptionType().equals(SubscriptionType.BASIC)){
            subscription.setTotalAmountPaid(500 + 200 * subscriptionEntryDto.getNoOfScreensRequired());
        }
        else if(subscriptionEntryDto.getSubscriptionType().equals(SubscriptionType.PRO)){
            subscription.setTotalAmountPaid(800 + 250 * subscriptionEntryDto.getNoOfScreensRequired());
        }
        else{
            subscription.setTotalAmountPaid(1000 + 350 * subscriptionEntryDto.getNoOfScreensRequired());
        }

        subscription.setUser(user);
        user.setSubscription(subscription);

        User savedUser = userRepository.save(user);

        return subscriptionRepository.save(subscription).getTotalAmountPaid();

    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository

        int oldCost = 0, newCost = 0;
        User user = userRepository.findById(userId).get();

        if(user.getSubscription().getSubscriptionType().equals(SubscriptionType.ELITE)){
            throw new Exception("Already the best Subscription");
        }
        else if(user.getSubscription().getSubscriptionType().equals(SubscriptionType.PRO)) {
            Date d = new Date();
            user.getSubscription().setSubscriptionType(SubscriptionType.ELITE);
            oldCost = user.getSubscription().getTotalAmountPaid();
            newCost = 1000 + 350 * user.getSubscription().getNoOfScreensSubscribed();
            user.getSubscription().setTotalAmountPaid(newCost);
            user.getSubscription().setStartSubscriptionDate(d);
        }
        else{
            Date d = new Date();
            user.getSubscription().setSubscriptionType(SubscriptionType.PRO);
            oldCost = user.getSubscription().getTotalAmountPaid();
            newCost = 800 + 250 * user.getSubscription().getNoOfScreensSubscribed();
            user.getSubscription().setTotalAmountPaid(newCost);
            user.getSubscription().setStartSubscriptionDate(d);
        }

        //userRepository.save(user);
        subscriptionRepository.save(user.getSubscription());

        return (newCost-oldCost);
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        int total = 0;
        List<Subscription> sub = subscriptionRepository.findAll();
        for(Subscription s : sub){
            total += s.getTotalAmountPaid();
        }
        return total;
    }

}
