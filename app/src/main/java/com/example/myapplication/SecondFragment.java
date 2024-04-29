package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

//Import Adobe SDK dependencies
import com.adobe.marketing.mobile.AdobeCallbackWithError;
import com.adobe.marketing.mobile.AdobeError;
import com.adobe.marketing.mobile.Edge;
import com.adobe.marketing.mobile.ExperienceEvent;
import com.adobe.marketing.mobile.optimize.DecisionScope;
import com.adobe.marketing.mobile.optimize.Optimize;
import com.adobe.marketing.mobile.optimize.Proposition;
import com.adobe.marketing.mobile.optimize.Offer;
import com.example.myapplication.databinding.FragmentSecondBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Integrate Adobe Target
        final String TAG1 = "Output Proposition Update";
        final String TAG2 = "Output Get Proposition";

        Optimize.onPropositionsUpdate(new AdobeCallbackWithError<Map<DecisionScope, Proposition>>() {
            @Override
            public void fail(final AdobeError adobeError) {
                // handle error
                Log.d(TAG1, String.valueOf(adobeError));
            }

            @Override
            public void call(final Map<DecisionScope, Proposition> propositionsMap) {
                if (propositionsMap != null && !propositionsMap.isEmpty()) {
                    // handle propositions
                    for (Map.Entry<DecisionScope, Proposition> entry : propositionsMap.entrySet()) {
                        Log.d(TAG1,entry.getKey() + ":" + entry.getValue());
                        final Proposition proposition1 = propositionsMap.get(entry.getKey());
                        final List<Offer> offers1 = proposition1.getOffers();
                        for (Integer i = 0; i < offers1.size(); i++)  {
                            final String content = offers1.get(i).getContent();
                            Log.d(TAG1, String.valueOf(content));
                        }
                    }
                }
            }
        });

        Map<String,Object> xdmData = new HashMap<String,Object>();
        Map<String,Object> customXdm = new HashMap<String,Object>();
        customXdm.put("datastreamId", "Add datastreamID 1");

        xdmData.put("_tenantId",customXdm);

        final DecisionScope decisionScope1 = new DecisionScope("Add mbox/decision scope name");

        final List<DecisionScope> decisionScopes = new ArrayList<>();
        decisionScopes.add(decisionScope1);

        Optimize.updatePropositions(decisionScopes,xdmData,null);


        //Create payload for screen tracking
        Map<String,Object> webPageXdm = new HashMap<String,Object>();

        customXdm.put("page",new HashMap<String,String>(){
            {
                put("name", "Second fragment");
            }
        });
        customXdm.put("user",new HashMap<String,String>(){
            {
                put("ecid","51357454263691022408820878425590144700");
                put("ldap","agodbole");
            }
        });

        webPageXdm.put("webPageDetails",new HashMap<String,Object>(){
            {
                put("name","Second fragment");
                put("pageViews",new HashMap<String,Double>(){
                    {
                        put("value",1.0);
                    }
                });
            }
        });

        xdmData.put("_tenantId",customXdm);
        xdmData.put("web",webPageXdm);
        xdmData.put("eventType", "web.webpagedetails.pageViews");

        ExperienceEvent experienceEvent = new ExperienceEvent.Builder()
                .setXdmSchema(xdmData)
                .build();

        // Send the Experience Event without handling the Edge Network response
        Edge.sendEvent(experienceEvent, null);

        customXdm.put("datastreamId", "Add datastreamID 2");
        xdmData.put("_tenantId",customXdm);


        experienceEvent = new ExperienceEvent.Builder()
                .setXdmSchema(xdmData)
                .build();

        // Send the Experience Event without handling the Edge Network response
        Edge.sendEvent(experienceEvent, null);

        xdmData.remove("web");
        customXdm.remove("page");
        customXdm.remove("user");

        Optimize.updatePropositions(decisionScopes,xdmData,null);

        Optimize.getPropositions(decisionScopes, new AdobeCallbackWithError<Map<DecisionScope, Proposition>>(){
            @Override
            public void fail(final AdobeError adobeError) {
                // handle error
                Log.d(TAG2, String.valueOf(adobeError));
            }

            @Override
            public void call(Map<DecisionScope, Proposition> propositionsMap) {
                if (propositionsMap != null && !propositionsMap.isEmpty()) {
                    // handle propositions
                    for (Map.Entry<DecisionScope, Proposition> entry : propositionsMap.entrySet()) {
                        Log.d(TAG2,entry.getKey() + ":" + entry.getValue());
                        final Proposition proposition1 = propositionsMap.get(entry.getKey());
                        final List<Offer> offers1 = proposition1.getOffers();
                        for (Integer i = 0; i < offers1.size(); i++)  {
                            final String content = offers1.get(i).getContent();
                            Log.d(TAG2, String.valueOf(content));
                        }
                    }
                }
            }
        });



        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);

                //Create payload for action tracking
                Map<String,Object> webLinkXdm = new HashMap<String,Object>();
                webLinkXdm.put("webInteraction",new HashMap<String,Object>(){
                    {
                        put("name","Previous");
                        put("type","other");
                        put("linkClicks",new HashMap<String,Double>(){
                            {
                                put("value",1.0);
                            }
                        });
                    }
                });

                xdmData.put("web",webLinkXdm);
                xdmData.put("eventType", "web.webinteraction.linkClicks");

                ExperienceEvent experienceEvent = new ExperienceEvent.Builder()
                        .setXdmSchema(xdmData)
                        .build();

                // Send the Experience Event without handling the Edge Network response
                Edge.sendEvent(experienceEvent, null);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}