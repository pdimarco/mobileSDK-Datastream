package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

//Import Adobe SDK dependencies
import com.adobe.marketing.mobile.Edge;
import com.adobe.marketing.mobile.ExperienceEvent;
import com.example.myapplication.databinding.FragmentFirstBinding;

import java.util.HashMap;
import java.util.Map;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Create payload for screen tracking
        Map<String,Object> xdmData = new HashMap<String,Object>();
        Map<String,Object> customXdm = new HashMap<String,Object>();
        Map<String,Object> webPageXdm = new HashMap<String,Object>();

        customXdm.put("page",new HashMap<String,String>(){
            {
                put("name", "First fragment");
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
                put("name","First fragment");
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

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);

                //Create payload for action tracking
                Map<String,Object> webLinkXdm = new HashMap<String,Object>();
                webLinkXdm.put("webInteraction",new HashMap<String,Object>(){
                    {
                        put("name","Next");
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