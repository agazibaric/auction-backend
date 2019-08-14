package com.agazibaric.service;

import com.agazibaric.dao.IAuctionDao;
import com.agazibaric.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AuctionService implements IAuctionService {

    @Autowired
    private IAuctionDao dao;

    @Override
    public Collection<Item> getAllItems() {
        return dao.getAllItems();
    }

    @Override
    public Item getItemById(int id) {
        return dao.getItemById(id);
    }

    @Override
    public void addItem(Item item) {
        dao.addItem(item);
    }

    @Override
    public void deleteItemById(int id) {
        dao.deleteItemById(id);
    }

    @Override
    public void updateItem(Item item) {
        dao.updateItem(item);
    }

}
