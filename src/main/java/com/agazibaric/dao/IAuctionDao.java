package com.agazibaric.dao;

import com.agazibaric.entity.Item;

import java.util.Collection;

public interface IAuctionDao {

    public Collection<Item> getAllItems();
    public Item getItemById(int id);
    public void addItem(Item item);
    public void deleteItemById(int id);
    public void updateItem(Item item);

}
