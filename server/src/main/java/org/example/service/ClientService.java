package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.example.repository.ClientRepository;
import org.example.model.Client;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    public Client getClient(Long id) {
        return clientRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(id));
    }

    public List<Client> getClients() {
        return StreamSupport
                .stream(clientRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Client deleteClient(Long id) {
        Client client = getClient(id);
        clientRepository.delete(client);
        return client;
    }

    @Transactional
    public Client editClient(Long id, Client client) {
        Client clientToEdit = getClient(id);
        clientToEdit.setName(client.getName());
        clientToEdit.setSurname(client.getSurname());
        clientToEdit.setLogin(client.getLogin());
        clientToEdit.setPassword(client.getPassword());
        return clientToEdit;
    }


    public List<Client> getAllClients()
    {
        List<Client> clients = new ArrayList<>();
        Streamable.of(clientRepository.findAll())
                .forEach(clients::add);
        return clients;
    }


}