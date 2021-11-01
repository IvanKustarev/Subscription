package com.example.subscription;

import com.example.subscription.models.Subscriptions;
import com.example.subscription.repositories.SubscriptionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.Iterator;

@Controller
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionsRepository subscriptionsRepository;

    @GetMapping("/{id}/")
    public String showSubscription(@PathVariable("id") Long userId, Model model){
        Iterator<Subscriptions> subscriptionsIterator = subscriptionsRepository.findAll().iterator();
        Subscriptions subscriptions = null;
        while (subscriptionsIterator.hasNext()){
            Subscriptions nextSub = subscriptionsIterator.next();
            if(nextSub.getUsedBy().equals(userId)){
                subscriptions = nextSub;
            }
        }

        if(subscriptions == null){
            model.addAttribute("toPayUrl", "http://localhost:8085/pay/" + userId + "/");

            addModelAttributes(model, userId);

            return "subBuy";
        }else {
            model.addAttribute("startTime", new Date(subscriptions.getStartTime()));
            model.addAttribute("endTime", new Date(subscriptions.getEndTime()));
            model.addAttribute("autoPay", subscriptions.getAutoPay());

            addModelAttributes(model, userId);

            return "subInfo";
        }
    }

    private Model addModelAttributes(Model model, Long userId){
        model.addAttribute("toAuthorisation", "http://localhost:8081/authorisation/");
        model.addAttribute("toProfile", "http://localhost:8082/profile/" + userId + "/");
        model.addAttribute("toCatalog", "http://localhost:8083/catalog/" + userId + "/");
        model.addAttribute("toSubscription", "http://localhost:8084/subscription/" + userId + "/");
        model.addAttribute("toPay", "http://localhost:8085/pay/" + userId + "/");
        model.addAttribute("toPenalties", "http://localhost:8086/penalties/" + userId + "/");
        return model;
    }
}
