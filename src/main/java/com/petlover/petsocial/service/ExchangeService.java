package com.petlover.petsocial.service;

import com.petlover.petsocial.model.entity.Exchange;
import com.petlover.petsocial.payload.request.ExchangeDTO;
import com.petlover.petsocial.payload.request.UserDTO;


import java.util.List;


public interface ExchangeService {

    ExchangeDTO addExchange(UserDTO userDTO, Long petId, int paymentAmount);

    ExchangeDTO deleteExchange(UserDTO userDTO,Long id);

    ExchangeDTO updateExchange(UserDTO userDTO,Long id);
    ExchangeDTO editCashExchange(UserDTO userDTO,Long id, int paymentAmount);

    List<ExchangeDTO> getAllExchangeDTO(UserDTO userDTO);
    List<ExchangeDTO> getAllRemovedExchangeDTO(UserDTO userDTO);
    List<ExchangeDTO> getAllNotRemovedExchangeDTO(UserDTO userDTO);

    List<Exchange> getAllExchange(UserDTO userDTO);
    List<Exchange> getAllRemovedExchange(UserDTO userDTO);
    List<Exchange> getAllNotRemovedExchange(UserDTO userDTO);


    //sua
    List<ExchangeDTO> getAllExchangeToShow(UserDTO userDTO);
    ExchangeDTO getOneExchangeDTO(UserDTO userDTO,Long id);

    Exchange getOneExchange(UserDTO userDTO,Long id);
}
