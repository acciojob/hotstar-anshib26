package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo

        WebSeries optionalWebSeries = webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());

        if(optionalWebSeries != null){
            throw new Exception("Series is already present");
        }

        ProductionHouse ph = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();

        WebSeries ws = new WebSeries(webSeriesEntryDto.getSeriesName(), webSeriesEntryDto.getAgeLimit(), webSeriesEntryDto.getRating(), webSeriesEntryDto.getSubscriptionType());

        ph.setRatings((ph.getRatings()+ws.getRating())/2);

        ws.setProductionHouse(ph);
        ph.getWebSeriesList().add(ws);

        ProductionHouse savedObj = productionHouseRepository.save(ph);

        return savedObj.getWebSeriesList().get(savedObj.getWebSeriesList().size()-1).getId();

    }
}
