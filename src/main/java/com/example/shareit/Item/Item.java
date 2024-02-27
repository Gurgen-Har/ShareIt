package com.example.shareit.Item;

import com.example.shareit.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Item {
    private Long id;
    private Long ownerId;
    @NotBlank
    private String name;
    private String description;
    private Boolean status;
    private Long requestId;
}
