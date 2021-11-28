package com.example.subscription;

import com.example.subscription.models.Subscriptions;
import com.example.subscription.repositories.SubscriptionsRepository;
import com.example.subscription.tokens.Token;
import com.example.subscription.tokens.TokenManager;
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

    @Autowired
    private TokenManager tokenManager;

    @GetMapping("/{token}/")
    public String showSubscription(@PathVariable("token") String token, Model model){
        if(tokenManager.userIdentify(new Token(token)) == null){
            return "redirect:http://localhost:8080/authorization/";
        }
        long id = new Long(tokenManager.userIdentify(new Token(token)));

        Iterator<Subscriptions> subscriptionsIterator = subscriptionsRepository.findAll().iterator();
        Subscriptions subscriptions = null;
        while (subscriptionsIterator.hasNext()){
            Subscriptions nextSub = subscriptionsIterator.next();
            if(nextSub.getUsedBy().equals(id)){
                subscriptions = nextSub;
            }
        }

        if(subscriptions == null){
            model.addAttribute("toPayUrl", "http://localhost:8080/pay/" + token + "/");

            addModelAttributes(model, token);

            return "subBuy";
        }else {
            model.addAttribute("startTime", new Date(subscriptions.getStartTime()));
            model.addAttribute("endTime", new Date(subscriptions.getEndTime()));
            model.addAttribute("autoPay", subscriptions.getAutoPay());

            addModelAttributes(model, token);

            return "subInfo";
        }
    }

    private Model addModelAttributes(Model model, String token){
        model.addAttribute("toAuthorisation", "http://localhost:8080/authorization/");
        model.addAttribute("toProfile", "http://localhost:8080/profile/" + token + "/");
        model.addAttribute("toCatalog", "http://localhost:8080/catalog/" + token + "/");
        model.addAttribute("toSubscription", "http://localhost:8080/subscription/" + token + "/");
        model.addAttribute("toPay", "http://localhost:8080/pay/" + token + "/");
        model.addAttribute("toPenalties", "http://localhost:8080/penalties/" + token + "/");
        return model;
    }
}
