package com.costly.costly.service;

import com.costly.costly.model.Transaction;
import com.costly.costly.repository.ListRepository;
import com.costly.costly.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class TransactionService {
    @Autowired
    private ListRepository listRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // GET TRANSACTION

    public com.costly.costly.response.Transaction getTransaction(Long id) {
        try {
            com.costly.costly.model.Transaction unmodifiedTransaction = transactionRepository.findTransactionById(id);
            com.costly.costly.response.Transaction newTransaction = new com.costly.costly.response.Transaction();
            newTransaction.setId(unmodifiedTransaction.getId());
            newTransaction.setValue(unmodifiedTransaction.getValue());
            newTransaction.setCurrency(unmodifiedTransaction.getCurrency());
            newTransaction.setIncome(unmodifiedTransaction.getIncome());
            newTransaction.setDescription(unmodifiedTransaction.getDescription());
            TransactionModelToResponse(unmodifiedTransaction, newTransaction);
            return newTransaction;
        } catch (Exception e) {
            if (e.getMessage().contains("No entity found for query")) {
                return null;
            }
            throw e;
        }
    }

    // POST TRANSACTION
    public com.costly.costly.response.Transaction createTransaction(com.costly.costly.request.post.Transaction newTransaction) {
        // check if listId exists - parse long from string
        Long listId = newTransaction.getListId();
        if (!listRepository.existsById(listId)) return null;

        // create new transaction
        com.costly.costly.model.Transaction transaction = new com.costly.costly.model.Transaction();
        transaction.setValue(newTransaction.getValue());
        transaction.setCurrency(newTransaction.getCurrency());
        transaction.setIncome(newTransaction.getIncome());
        transaction.setDescription(newTransaction.getDescription());
        transaction.setList(listRepository.findListById(listId));
        transactionRepository.save(transaction);

        // return new transaction with stream
        return Stream.of(transaction)
                .map(transaction1 -> {
                    com.costly.costly.response.Transaction newTransaction1 = new com.costly.costly.response.Transaction();
                    newTransaction1.setId(transaction1.getId());
                    newTransaction1.setValue(transaction1.getValue());
                    newTransaction1.setCurrency(transaction1.getCurrency());
                    newTransaction1.setIncome(transaction1.getIncome());
                    newTransaction1.setDescription(transaction1.getDescription());
                    TransactionModelToResponse(transaction1, newTransaction1);
                    return newTransaction1;
                })
                .toList()
                .get(0);
    }

    // PUT TRANSACTION
    public com.costly.costly.response.Transaction updateTransaction(Long id, com.costly.costly.request.put.Transaction updatedTransaction) {
        try {
            com.costly.costly.model.Transaction unmodifiedTransaction = transactionRepository.findTransactionById(id);
            unmodifiedTransaction.setValue(updatedTransaction.getValue());
            unmodifiedTransaction.setCurrency(updatedTransaction.getCurrency());
            unmodifiedTransaction.setIncome(updatedTransaction.getIncome());
            unmodifiedTransaction.setDescription(updatedTransaction.getDescription());
            transactionRepository.save(unmodifiedTransaction);

            // return updated transaction with stream
            return Stream.of(unmodifiedTransaction)
                    .map(transaction -> {
                        com.costly.costly.response.Transaction newTransaction = new com.costly.costly.response.Transaction();
                        newTransaction.setId(transaction.getId());
                        newTransaction.setValue(transaction.getValue());
                        newTransaction.setCurrency(transaction.getCurrency());
                        newTransaction.setIncome(transaction.getIncome());
                        newTransaction.setDescription(transaction.getDescription());
                        TransactionModelToResponse(transaction, newTransaction);
                        return newTransaction;
                    })
                    .toList()
                    .get(0);
        } catch (Exception e) {
            if (e.getMessage().contains("No entity found for query")) {
                return null;
            }
            throw e;
        }
    }

    // DELETE TRANSACTION
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findTransactionById(id);
        transaction.setDeletedAt(new java.util.Date());
        transactionRepository.save(transaction);
    }

    static void TransactionModelToResponse(Transaction unmodifiedTransaction, com.costly.costly.response.Transaction newTransaction) {
        newTransaction.setCategory(
                Stream.of(unmodifiedTransaction.getCategory())
                        .map(category -> {
                            com.costly.costly.response.Category newCategory = new com.costly.costly.response.Category();
                            newCategory.setId(category.getId());
                            newCategory.setName(category.getName());
                            newCategory.setColor(category.getColor());
                            newCategory.setIcon(category.getIcon());
                            return newCategory;
                        })
                        .toList()
                        .get(0)
        );
        newTransaction.setCreatedAt(unmodifiedTransaction.getCreatedAt());
    }
}
