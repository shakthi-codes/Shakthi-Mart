package dao;

import model.Service;

import java.util.List;

public interface ServiceDAO {

    void save(Service service);

    List<Service> findAll();

    List<Service> findByCreatorId(int creatorId);

    Service findById(int id);

    void update(Service service);

    void delete(int id);
}
