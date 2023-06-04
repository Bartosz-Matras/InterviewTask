package pl.matrasbartosz.zadanieatipera.entity;

import java.util.List;

public record RepositoryResponse(Repository repository, List<Branch> branch) {
}
