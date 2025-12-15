package models.jobdesk;

import java.util.UUID;

public class RequestRestock {
    private UUID requestID;
    private UUID productID;
    private int quantityToRestock;
    private RequestStatus requestStatus;
    private UUID managerID;
    private UUID stockerID;

    // buat bikin req baru
    public RequestRestock(UUID productID, int quantityToRestock, UUID managerID) {
        this.requestID = UUID.randomUUID();
        this.productID = productID;
        this.quantityToRestock = quantityToRestock;
        this.requestStatus = RequestStatus.PENDING;
        this.managerID = managerID;
        this.stockerID = null;
    }

    // buat ngambil req lama dari db
    public RequestRestock(UUID requestID, UUID productID, int quantityToRestock, RequestStatus requestStatus, UUID managerID, UUID stockerID) {
        this.requestID = requestID;
        this.productID = productID;
        this.quantityToRestock = quantityToRestock;
        this.requestStatus = requestStatus;
        this.managerID = managerID;
        this.stockerID = stockerID;
    }

    public UUID getRequestID() {
        return requestID;
    }

    public void setRequestID(UUID requestID) {
        this.requestID = requestID;
    }

    public UUID getProductID() {
        return productID;
    }

    public void setProductID(UUID productID) {
        this.productID = productID;
    }

    public int getQuantityToRestock() {
        return quantityToRestock;
    }

    public void setQuantityToRestock(int quantityToRestock) {
        this.quantityToRestock = quantityToRestock;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public UUID getManagerID() {
        return managerID;
    }

    public void setManagerID(UUID managerID) {
        this.managerID = managerID;
    }

    public UUID getStockerID() {
        return stockerID;
    }

    public void setStockerID(UUID stockerID) {
        this.stockerID = stockerID;
    }
}
