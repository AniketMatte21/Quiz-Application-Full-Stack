package com.quiz.quiz_backend.Service;

import com.quiz.quiz_backend.Repository.StripeResponseRepo;
import com.quiz.quiz_backend.dto.ProductRequest;
import com.quiz.quiz_backend.Entity.StripeResponse;
import com.quiz.quiz_backend.dto.SavePurchaseDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StripeService
{
    @Value("${stripe.secretKey}")
    private String secretKey;

    @Autowired
    private StripeResponseRepo stripeResponseRepo;

    @Autowired
    private QuizPurchaseService quizPurchaseService;

    //Stripe-API takes->
    //ProductName, amount,currency

    //Stripe Api gives->
    //return sessionId,sessionUrl

    public StripeResponse createOrder(ProductRequest productRequest)  {
        //API key
        Stripe.apiKey=secretKey;

//        //set ProductData i.e product name
//        SessionCreateParams.LineItem.PriceData.ProductData productData =
//                SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                .setName(productRequest.getName()).build();
//
//        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
//                .setCurrency(productRequest.getCurrency() != null ? productRequest.getCurrency() : "USD")
//                .setUnitAmount(productRequest.getAmount())
//                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                        .setName(productRequest.getName()).build())
//                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(productRequest.getCurrency()!=null?productRequest.getCurrency().toLowerCase():"inr")
                                .setUnitAmount(productRequest.getAmount()*100)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName(productRequest.getName()).build()

                                )
                                .build()
                )

                .build();

        SessionCreateParams params=
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/home-page/index.html?session_id={CHECKOUT_SESSION_ID}&paymentStatus=SUCCESS")
                        .setCancelUrl("http://localhost:8080/cancel")
                        .addLineItem(lineItem)
                        .build();

        Session session;

        try{
             session=Session.create(params);

             StripeResponse stripeResponse=new StripeResponse();
             stripeResponse.setStatus("SUCCESS");
             stripeResponse.setMessage("Payment session created");
             stripeResponse.setSessionId(session.getId());
             stripeResponse.setSessionUrl(session.getUrl());
             stripeResponseRepo.save(stripeResponse);

             return stripeResponse;

        }
        catch(StripeException e)
        {
            System.out.println(e.getStripeError());
        }

        return StripeResponse
                .builder()
                .status("FAILED")
                .message("failed to create payment session")
                .build();

    }

    public ResponseEntity<String> checkSessionId(String sessionId, SavePurchaseDTO savePurchaseDTO,int id)
    {
        Optional<StripeResponse> bySessionId = stripeResponseRepo.findBySessionId(sessionId);
        if(bySessionId.isPresent())
        {
            quizPurchaseService.savePurchased(savePurchaseDTO,id);
            stripeResponseRepo.deleteBySessionId(sessionId);
            return ResponseEntity.ok("you have successfully purchased the quiz");
        }
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Expectation failed..");
    }
}
