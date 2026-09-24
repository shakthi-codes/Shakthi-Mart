package service;

import dao.ServiceDAO;
import model.Service;

import java.util.List;

public class ServiceManager {

    private final ServiceDAO serviceDAO;

    public ServiceManager(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
    }

    public void addService(Service service) {
        serviceDAO.save(service);
    }

    public List<Service> getAllServices() {
        return serviceDAO.findAll();
    }

    public List<Service> getServicesByCreator(int creatorId) {
        return serviceDAO.findByCreatorId(creatorId);
    }

    public Service getServiceById(int id) {
        return serviceDAO.findById(id);
    }

    public void updateService(Service service) {
        serviceDAO.update(service);
    }

    public void deleteService(int id) {
        serviceDAO.delete(id);
    }
}
