package me.mikael.graphqlstuff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserError {
    private String message;
    private List<String> path;
}
