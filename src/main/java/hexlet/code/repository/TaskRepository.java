package hexlet.code.repository;

import com.querydsl.core.types.dsl.StringPath;
import hexlet.code.model.QTask;
import hexlet.code.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends
        JpaRepository<Task, Long>,
        QuerydslPredicateExecutor<Task>,
        QuerydslBinderCustomizer<QTask> {
    List<Task> findTasksByLabelsId(Long labelId);
    @Override
    default void customize(QuerydslBindings bindings, QTask task) {
        bindings.bind(String.class)
                .first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }

    Optional<Task> findByName(String name);
}
