package com.stelliocode.backend.service;

import com.stelliocode.backend.entity.Client;
import com.stelliocode.backend.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client createClient(String googleId, String name, String email, String phone, String profilePicture) {
        return clientRepository.findByGoogleId(googleId)
                .orElseGet(() -> {
                    Client newClient = new Client();
                    newClient.setGoogleId(googleId);
                    newClient.setName(name);
                    newClient.setEmail(email);
                    newClient.setPhone(phone);
                    newClient.setProfilePicture(profilePicture);
                    clientRepository.save(newClient);
                    return newClient;
                });
    }
}
