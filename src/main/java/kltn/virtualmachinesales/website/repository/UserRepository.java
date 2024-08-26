package kltn.virtualmachinesales.website.repository;

import kltn.virtualmachinesales.website.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u where u.email = :email")
    User findByEmail(String email);

    User findById(int id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
    boolean existsByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u where u.id != :userId")
    List<User> listAllUserExceptMe(@Param("userId") int userId);

    User findByUsername(String username);

    //@Query("SELECT u FROM User u WHERE u.username LIKE %:username% LIMIT 10")
    //List<User> findByUsernameContains(@Param("username") String username);

}
