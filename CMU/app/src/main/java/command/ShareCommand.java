package command;

import response.Response;

public class ShareCommand implements Command {

    private static final long serialVersionUID = -8807331723807741905L;
    private String userName;
    private String location;
    private int correct;
    private int  total;
    private long quizzTime;

    public ShareCommand(String u, String l, int c, int t, long qt) {
        this.correct = c;
        this.total = t;
        this.location = l;
        this.userName = u;
        this.quizzTime = qt;
    }

    @Override
    public Response handle(CommandHandler chi) {
        return chi.handle(this);
    }


    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getQuizzTime() {
        return quizzTime;
    }

    public void setQuizzTime(long quizzTime) {
        this.quizzTime = quizzTime;
    }
}
