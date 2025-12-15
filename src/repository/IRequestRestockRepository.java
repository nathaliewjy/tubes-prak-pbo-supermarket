package repository;

import models.jobdesk.RequestRestock;
import models.jobdesk.RequestStatus;

import java.util.ArrayList;
import java.util.UUID;

public interface IRequestRestockRepository {
    void createRequest(RequestRestock req);
    void updateStatus(UUID requestID, RequestStatus newStatus);
    ArrayList<RequestRestock> getPendingRequest(UUID stockerID); // buart liat jobdesk yg blm selesai
    ArrayList<RequestRestock> getAllRequests();
}
