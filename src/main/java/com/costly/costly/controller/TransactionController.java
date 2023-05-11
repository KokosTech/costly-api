package com.costly.costly.controller;

import com.costly.costly.response.Transaction;
import com.costly.costly.service.ListService;
import com.costly.costly.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions/")
public class TransactionController {
    @Autowired
    private ListService listService;

    @Autowired
    private TransactionService transactionService;

    // GET TRANSACTION
    @GetMapping("/{id}/")
    public Transaction getTransaction(@PathVariable String id) {
        return transactionService.getTransaction(Long.parseLong(id));
    }
    // POST TRANSACTION
    @PostMapping("/")
    public Transaction createTransaction(@RequestBody com.costly.costly.request.post.Transaction newTransaction) {
        return transactionService.createTransaction(newTransaction);
    }
    // PUT TRANSACTION
    @PutMapping("/")
    public Transaction updateTransaction(@RequestBody com.costly.costly.request.put.Transaction oldTransaction) {
        return transactionService.updateTransaction(oldTransaction.getId(), oldTransaction);
    }
    // DELETE TRANSACTION
    @DeleteMapping("/{id}/")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }
}
