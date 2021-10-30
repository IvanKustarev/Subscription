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
            //подписки нет
            return "subBuy";
        }else {
            model.addAttribute("startTime", new Date(subscriptions.getStartTime()));
            model.addAttribute("endTime", new Date(subscriptions.getEndTime()));
            model.addAttribute("autoPay", subscriptions.getAutoPay());
            return "subInfo";
        }
    }
}
