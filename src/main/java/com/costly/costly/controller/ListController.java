package com.costly.costly.controller;

import com.costly.costly.request.post.List;
import com.costly.costly.service.ListService;
import com.costly.costly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lists/")
public class ListController {
    @Autowired
    private UserService userService;
    @Autowired
    private ListService listService;

    @GetMapping("/{id}/")
    public java.util.List<com.costly.costly.response.List> getLists(@PathVariable String id) {
        System.out.println("USER ID = " + id);
        return listService.getLists(Long.parseLong(id));
    }

    @GetMapping("/{id}/transactions/")
    public java.util.List<com.costly.costly.response.Transaction> getTransactions(@PathVariable String id) {
        System.out.println("LIST ID = " + id);
        return listService.getTransactions(Long.parseLong(id));
    }

    @PostMapping("/")
    public com.costly.costly.model.List createList(@RequestBody List newList) {
        System.out.println("NEW LIST = " + newList);
        return listService.createList(newList);
    }

    @PutMapping("/")
    public com.costly.costly.model.List updateList(@RequestBody com.costly.costly.request.put.List oldList) {
        System.out.println("OLD LIST = " + oldList);
        return listService.updateList(oldList.getId(), oldList);
    }

    @DeleteMapping("/{id}/")
    public void deleteList(@PathVariable Long id) {
        System.out.println("DELETE LIST = " + id);
        listService.deleteList(id);
    }
}