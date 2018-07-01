package digivjayb.jee8.webapi.srv;

import digivjayb.jee8.webapi.StudentResource;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Named;
import javax.ws.rs.container.AsyncResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RequestScoped
@Named
public class StudentService {

    private List<String> names = Arrays.asList("Jon", "Johnny", "Jagan", "Jojo", "Mike", "Dom", "Vick", "Paul");

    public StudentResource.StudentDTO getOne(Long id) {
        String name = names.get((names.size() - 1) & id.intValue());
        return new StudentResource.StudentDTO(id, name, new Date());
    }

    public List<StudentResource.StudentDTO> getAll() {
        System.out.println("getAll");
        final AtomicLong index = new AtomicLong(0L);
        return names.stream()
                .map(e -> (new StudentResource.StudentDTO(index.getAndIncrement(), e, new Date())))
                .collect(Collectors.toList());
    }
}
