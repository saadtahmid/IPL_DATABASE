package client;

import javafx.application.Platform;

public class TransferWindowRefreshThread implements Runnable {
    private ClubHomeWindowController clubHomeWindowController;
    Thread thread;

    public TransferWindowRefreshThread(ClubHomeWindowController clubHomeWindowController) {
        this.clubHomeWindowController = clubHomeWindowController;
        this.thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {

                Platform.runLater(() -> clubHomeWindowController.loadTransferWindow());
                Thread.sleep(5* 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

    }

    public Thread getThread() {
        return thread;
    }
}
