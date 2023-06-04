package pl.matrasbartosz.zadanieatipera.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record Repository(String name, @JsonIgnore boolean fork, Owner owner) {
}
