package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.Pets;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cassandra repository for the Pets entity.
 */
@Repository
public class PetsRepository {

    @Inject
    private Session session;

    private Mapper<Pets> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(Pets.class);
        findAllStmt = session.prepare("SELECT * FROM pets");
        truncateStmt = session.prepare("TRUNCATE pets");
    }

    public List<Pets> findAll() {
        List<Pets> pets = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Pets pets = new Pets();
                pets.setId(row.getUUID("id"));
                pets.setName(row.getString("name"));
                pets.setPetCategory(row.getString("petCategory"));
                pets.setPetCategoryId(row.getUUID("petCategoryId"));
                pets.setPetSpeed(row.getDecimal("petSpeed"));
                return pets;
            }
        ).forEach(pets::add);
        return pets;
    }

    public Pets findOne(UUID id) {
        return mapper.get(id);
    }

    public Pets save(Pets pets) {
        if (pets.getId() == null) {
            pets.setId(UUID.randomUUID());
        }
        mapper.save(pets);
        return pets;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
