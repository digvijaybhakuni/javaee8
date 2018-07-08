package digivjayb.jee8.webapi;

import digivjayb.jee8.webapi.srv.StudentService;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Qualifier;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Stateless
@Path("/student")
public class StudentResource {

    @Inject
    private StudentService studentService;

    //@Inject
    private ExecutorService executorService;

    @Inject
    private Event<StudentDTO> event;


    public StudentResource() {
         this.executorService = Executors.newFixedThreadPool(5);
    }

    @GET @Path("test-text")
    public String getText(){
        return "This test String for Hello World!";
    }

    @GET @Path("/{id}")
    public StudentDTO getOne(@PathParam("id") Long id){

        final StudentDTO studentDTO = studentService.getOne(id);
        event.fireAsync(studentDTO);
        event.fire(studentDTO);
        return studentDTO;
        //return new StudentDTO(id, "Name", new Date());
    }


    @GET
    @Asynchronous
    public void list(@Suspended AsyncResponse response){
        CompletableFuture
                .supplyAsync(studentService::getAll, executorService)
                .whenComplete( (list, err) -> {
                    System.out.println("err = " + err);
                    System.out.println("err.getMessage() = " + err.getMessage());
                    System.out.println("err.getCause().gatCause() = " + err.getCause().getCause());
                    if (err != null) {
                        response.resume(err.getCause().getCause());
                    }
                    response.resume(list);
                });
//        CompletableFuture
//                .supplyAsync(studentService::getAll)
//                .thenAccept(response::resume);
//        CompletableFuture
//                .supplyAsync(studentService::getAll, executorService)
//                .thenAccept(response::resume);
    }


    public static class StudentDTO {
        private Long id;
        private String name;
        private Date dob;

        public StudentDTO() {
        }

        public StudentDTO(Long id, String name, Date dob) {
            this.id = id;
            this.name = name;
            this.dob = dob;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getDob() {
            return dob;
        }

        public void setDob(Date dob) {
            this.dob = dob;
        }

        @Override
        public String toString() {
            return "StudentDTO{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", dob=" + dob +
                    '}';
        }
    }

}
