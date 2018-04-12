package response;

public interface ResponseHandler {
    public void handle(LoginResponse lr);
    public void handle(RegisterResponse rr);
}
