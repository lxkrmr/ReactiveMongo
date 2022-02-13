package com.example.reactivemongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record Account(@Id String id,
                      String owner,
                      Double value) {
}
