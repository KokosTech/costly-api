package com.costly.costly.service;

import com.costly.costly.repository.ListRepository;
import com.costly.costly.repository.UserRepository;
import com.costly.costly.request.post.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class ListService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ListRepository listRepository;


    // GET LIST

    public java.util.List<com.costly.costly.response.List> getLists(Long id) {
        try {
            java.util.List<com.costly.costly.model.List> unmodifiedLists = userRepository.findListByUserId(id);
            return unmodifiedLists.stream()
                    .map(list -> {
                        com.costly.costly.response.List newList = new com.costly.costly.response.List();
                        newList.setId(list.getId());
                        newList.setName(list.getName());
                        newList.setColor(list.getColor());
                        newList.setIcon(list.getIcon());

                        // calculate balance for each list
                        double balance = list.getTransactions().stream()
                                .filter(transaction -> transaction.getDeletedAt() == null)
                                .mapToDouble(transaction -> transaction.getIncome() ? transaction.getValue() : -transaction.getValue())
                                .sum();
                        newList.setBalance(balance);

                        return newList;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            if (e.getMessage().contains("No entity found for query")) {
                return null;
            }
            throw e;
        }
    }

    // POST LIST
    public com.costly.costly.model.List createList(List newList) {
        // check if userId exists - parse long from string
        Long userId = Long.parseLong(newList.getUserId());
        if (!userRepository.existsById(userId)) return null;

        // create new list
        com.costly.costly.model.List list = new com.costly.costly.model.List();
        list.setName(newList.getName());
        list.setColor(newList.getColor());
        list.setIcon(newList.getIcon());

        // connect list to user
        com.costly.costly.model.User user = userRepository.findById(userId).get();
        list.setOwner(user);
        user.getLists().add(list);

        // save list
        com.costly.costly.model.List savedList = listRepository.save(list);

        return savedList;
    }

    // UPDATE LIST
    public com.costly.costly.model.List updateList(Long id, com.costly.costly.request.put.List oldList) {
        // check if list exists
        if (!listRepository.existsById(id)) return null;

        // get list
        com.costly.costly.model.List list = listRepository.findById(id).get();

        // update list
        list.setName(oldList.getName());
        list.setColor(oldList.getColor());
        list.setIcon(oldList.getIcon());

        // save list
        com.costly.costly.model.List savedList = listRepository.save(list);

        return savedList;
    }

    // DELETE LIST
    public void deleteList(Long id) {
        com.costly.costly.model.List list = listRepository.findById(id).get();
        // set deleted at NOW
        list.setDeletedAt(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
        // save list
        listRepository.save(list);
    }

    // GET TRANSACTIONS
    public java.util.List<com.costly.costly.response.Transaction> getTransactions(Long id) {
        try {
            java.util.List<com.costly.costly.model.Transaction> unmodifiedTransactions = listRepository.findTransactionsByListId(id);
            return unmodifiedTransactions.stream()
                    .filter(transaction -> transaction.getDeletedAt() == null)
                    .map(transaction -> {
                        com.costly.costly.response.Transaction newTransaction = new com.costly.costly.response.Transaction();
                        newTransaction.setId(transaction.getId());
                        newTransaction.setValue(transaction.getValue());
                        newTransaction.setCurrency(transaction.getCurrency());
                        newTransaction.setIncome(transaction.getIncome());
                        TransactionService.TransactionModelToResponse(transaction, newTransaction);
                        return newTransaction;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            if (e.getMessage().contains("No entity found for query")) {
                return null;
            }
            throw e;
        }
    }
}
