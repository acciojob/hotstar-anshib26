package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository

//      //List<WebSeries> basicWebSeries = webSeriesRepository.findBySubscriptionType(SubscriptionType.BASIC);
//      //List<WebSeries> proWebSeries = webSeriesRepository.findBySubscriptionType(SubscriptionType.PRO);
//      //List<WebSeries> eliteWebSeries = webSeriesRepository.findBySubscriptionType(SubscriptionType.ELITE);

        int count = 0;
        Optional<User> optionalUser = userRepository.findById(userId);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            SubscriptionType subType = user.getSubscription().getSubscriptionType();

            List<WebSeries> watchable = webSeriesRepository.findBySubscriptionTypeAndAgeLimitLessThanEqual(SubscriptionType.BASIC, user.getAge());

            if(subType.equals(SubscriptionType.BASIC)){

            }
            else if(subType.equals(SubscriptionType.PRO)){
                List<WebSeries> proWatchable = webSeriesRepository.findBySubscriptionTypeAndAgeLimitLessThanEqual(subType, user.getAge());
                watchable.addAll(proWatchable);
            }
            else{
                List<WebSeries> proWatchable = webSeriesRepository.findBySubscriptionTypeAndAgeLimitLessThanEqual(SubscriptionType.PRO, user.getAge());
                List<WebSeries> eliteWatchable = webSeriesRepository.findBySubscriptionTypeAndAgeLimitLessThanEqual(subType, user.getAge());
                watchable.addAll(proWatchable);
                watchable.addAll(eliteWatchable);
            }

            return watchable.size();

        }

        return null;
    }


}
