package com.example.shareit.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long> {
    List<Item> findByOwnerId(Long ownerId);
    @Query("SELECT i FROM Item i " +
            "WHERE lower(i.name) like lower(concat('%', :search, '%')) " +
            " or lower(i.description) like lower(concat('%', :search, '%') ) " +
            " and i.available = true")
    List<Item> getItemBySearchQuery(@Param("search") String text);

}
